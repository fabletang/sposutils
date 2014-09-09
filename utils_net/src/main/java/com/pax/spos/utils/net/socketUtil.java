package com.pax.spos.utils.net;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.*;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by fable on 14-9-9.
 */
public class socketUtil {
   public static Logger log = Logger.getLogger("socketUtil");


    private socketUtil(){
        log.setLevel(Level.INFO);
    }

     public static String sendTCPMessage(String message, String ipAddress, int port, String charset){
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(1000);
        connector.getSessionConfig().setUseReadOperation(true); //同步的客户端,必须设置此项,其默认为false
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ClientProtocolEncoder(charset), new ClientProtocolDecode(charset)));
        //connector.setHandler(this); //作为同步的客户端,可以不需要IoHandler,Mina会自动添加一个默认的IoHandler实现,即AbstractIoConnector
        IoSession session = null;
        Object respData = null;
        try{
            ConnectFuture connectFuture = connector.connect(new InetSocketAddress(ipAddress, port));
            connectFuture.awaitUninterruptibly();          //等待连接成功,相当于将异步执行转为同步执行
            session = connectFuture.getSession();          //获取连接成功后的会话对象
            session.write(message).awaitUninterruptibly(); //由于上面已经设置setUseReadOperation(true),故IoSession.read()方法才可用
            ReadFuture readFuture = session.read();        //因其内部使用BlockingQueue,故Server端用之可能会内存泄漏,但Client端可适当用之
            if(readFuture.awaitUninterruptibly(90, TimeUnit.SECONDS)){ //Wait until the message is received
                respData = readFuture.getMessage();                    //Get the received message
            }else{

                log.info("读取[/" + ipAddress + ":" + port + "]超时");
            }
        }catch(Exception e){
            log.warning("请求通信[/" + ipAddress + ":" + port + "]偶遇异常,堆栈轨迹如下"+e.toString());
        }finally{
            if(session != null){
                //关闭IoSession,该操作是异步的,true为立即关闭,false为所有写操作都flush后关闭
                //这里仅仅是关闭了TCP的连接通道,并未关闭Client端程序
                session.close(true);
                //客户端发起连接时,会请求系统分配相关的文件句柄,而在连接失败时记得释放资源,否则会造成文件句柄泄露
                //当总的文件句柄数超过系统设置值时[ulimit -n],则抛异常"java.io.IOException: Too many open files",导致新连接无法创建,服务器挂掉
                //所以,若不关闭的话,其运行一段时间后可能抛出too many open files异常,导致无法连接
                session.getService().dispose();
            }
        }
        return respData==null ? "MINA_SERVER_ERROR" : respData.toString();
    }


    /**
     * 客户端编码器
     * 将Client的报文编码后发送到Server
     *
     */
    private static class ClientProtocolEncoder extends ProtocolEncoderAdapter {
        private final String charset;
        public ClientProtocolEncoder(String charset){
            this.charset = charset;
        }
        @Override
        public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
            IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);
            //二者等效--><code>buffer.put(message.toString().getBytes(charset))</code>
            buffer.putString(message.toString(), Charset.forName(charset).newEncoder());
            buffer.flip();
            out.write(buffer);
        }
    }


    /**
     * 客户端解码器
     * 解码Server的响应报文给Client
     * 样例报文[000064100030010000120121101210419100000000000028`18622233125`10`]
     */
    private static class ClientProtocolDecode extends CumulativeProtocolDecoder {
        private final String charset;
        //注意这里使用了Mina自带的AttributeKey类来定义保存在IoSession中对象的键值,其可有效防止键值重复
        //通过查询AttributeKey类源码发现,它的构造方法采用的是"类名+键名+AttributeKey的hashCode"的方式
        private final AttributeKey CONTEXT = new AttributeKey(getClass(), "context");
        public ClientProtocolDecode(String charset){
            this.charset = charset;
        }
        private Context getContext(IoSession session){
            Context context = (Context)session.getAttribute(CONTEXT);
            if(null == context){
                context = new Context();
                session.setAttribute(CONTEXT, context);
            }
            return context;
        }
        @Override
        protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
            Context ctx = this.getContext(session);
            IoBuffer buffer = ctx.innerBuffer;
            int messageCount = ctx.getMessageCount();
            while(in.hasRemaining()){    //判断position和limit之间是否有元素
                buffer.put(in.get());    //get()读取buffer的position的字节,然后position+1
                if(messageCount++ == 5){ //约定:报文的前6个字符串表示报文总长度,不足6位则左侧补0
                    buffer.flip();       //Set limit=position and position=0 and mark=-1
                    //当Server的响应报文中含0x00时,Mina2.x的buffer.getString(fieldSize, decoder)方法会break
                    //该方法的处理细节,详见org.apache.mina.core.buffer.AbstractIoBuffer类的第1718行源码,其说明如下
                    //Reads a NUL-terminated string from this buffer using the specified decoder and returns it
                    //ctx.setMessageLength(Integer.parseInt(buffer.getString(6, decoder)));
                    byte[] messageLength = new byte[6];
                    buffer.get(messageLength);
                    try{
                        //请求报文有误时,Server可能返回非约定报文,此时会抛java.lang.NumberFormatException
                        ctx.setMessageLength(Integer.parseInt(new String(messageLength, charset)));
                    }catch(NumberFormatException e){
                        ctx.setMessageLength(in.limit());
                    }
                    buffer.limit(in.limit()); //让两个IoBuffer的limit相等
                }
            }
            ctx.setMessageCount(messageCount);
            if(ctx.getMessageLength() == buffer.position()){
                buffer.flip();
                byte[] message = new byte[buffer.limit()];
                buffer.get(message);
                out.write(new String(message, charset));
                ctx.reset();
                return true;
            }else{
                return false;
            }
        }
        private class Context{
            private final IoBuffer innerBuffer; //用于累积数据的IoBuffer
            private int messageCount;           //记录已读取的报文字节数
            private int messageLength;          //记录已读取的报文头标识的报文长度
            public Context(){
                innerBuffer = IoBuffer.allocate(100).setAutoExpand(true);
            }
            public int getMessageCount() {
                return messageCount;
            }
            public void setMessageCount(int messageCount) {
                this.messageCount = messageCount;
            }
            public int getMessageLength() {
                return messageLength;
            }
            public void setMessageLength(int messageLength) {
                this.messageLength = messageLength;
            }
            public void reset(){
                this.innerBuffer.clear(); //Set limit=capacity and position=0 and mark=-1
                this.messageCount = 0;
                this.messageLength = 0;
            }
        }
    }
}

