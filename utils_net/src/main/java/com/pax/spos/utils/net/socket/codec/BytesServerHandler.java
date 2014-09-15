package com.pax.spos.utils.net.socket.codec;

import com.pax.spos.utils.net.socket.model.SocketBytes;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

/**
 * Created by fable on 14-9-11.
 * for server test
 */
public class BytesServerHandler extends IoHandlerAdapter {
    private SocketBytes socketBytes;

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
//    System.out.println("server 收到消息:"+socketBytes);
        socketBytes = (SocketBytes) message;
        System.out.println("server 收到消息:" + socketBytes);
        byte[] dest = socketBytes.getBytesContent();
        if (socketBytes.isFitSocketPara()) {
            dest[0] = (byte) (0xAA);
        } else {

            dest[0] = (byte) (0xFF);
        }
        socketBytes.setBytesContent(dest);
        socketBytes.setSendDate(new Date());
        session.write(socketBytes);
        System.out.println("server send消息:" + socketBytes);
//        IoBuffer bbuf = (IoBuffer) message;
//        byte[] byten = new byte[bbuf.limit()];
//        bbuf.get(byten, bbuf.position(), bbuf.limit());
//        System.out.println("收到消息：" + ByteStringHex.bytes2HexStr(byten));
//        byte[] bts = new byte[10];
//        for(int i=0;i<10;i++){
//            bts[i] = (byte)i;
//        }
//        IoBuffer buffer = IoBuffer.allocate(10);
//        buffer.put(bts);
//        buffer.flip();
//        session.write(buffer);
//      // 拿到所有的客户端Session
//      Collection<IoSession> sessions = session.getService().getManagedSessions().values();
//      // 向所有客户端发送数据
//      for (IoSession sess : sessions) {
//          sess.write(buffer);
//      }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("server 会话关闭");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        System.out.println("server 会话异常");
//        super.exceptionCaught(session, cause);
        System.out.println(cause.toString());
        session.close(true);
    }

    @Override
    public void messageSent(IoSession iosession, Object obj) throws Exception {
        System.out.println("服务端消息发送");
        super.messageSent(iosession, obj);
    }

    @Override
    public void sessionCreated(IoSession iosession) throws Exception {
        System.out.println("server 会话创建");
        super.sessionCreated(iosession);
    }

    @Override
    public void sessionIdle(IoSession iosession, IdleStatus idlestatus)
            throws Exception {
        System.out.println("server 会话休眠");
//        super.sessionIdle(iosession, idlestatus);
        iosession.close(true);
    }

    @Override
    public void sessionOpened(IoSession iosession) throws Exception {
        System.out.println("server 会话打开");
        super.sessionOpened(iosession);
    }
}
