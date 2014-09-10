package com.pax.spos.utils.net.socket.codec;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.net.socket.SocketUtil;
import com.pax.spos.utils.net.socket.model.SocketBytes;
import com.pax.spos.utils.net.socket.model.SocketPara;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by fable on 14-9-10.
 */
public class BytesDecoder extends CumulativeProtocolDecoder {
    public static Logger LOGGER= Logger.getLogger("BytesDecoder");
    private boolean isServer;
    public BytesDecoder(boolean isServer) {
        this.isServer=isServer;
    }

    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        byte[] buf = readBuffer(in);
        in.free();
        if (buf==null) return false;
        int bufLen = buf.length;
        if (bufLen < 1) {
            return false;
        }
        SocketPara socketPara= SocketUtil.getSocketPara();
        if (socketPara==null) return false;
        int headLen;
        if (isServer){
            headLen=socketPara.getHeadLenNum_out();
        }else{
            headLen=socketPara.getHeadLenNum_in();
        }
        if (headLen>bufLen||headLen<1) return false;
        byte[] headBytes= new byte[headLen];
        System.arraycopy(buf,0,headBytes,0,headLen-1);
        //todo len by type
        int len= ByteStringHex.bytes2Int(headBytes);
        SocketBytes socketBytes=new SocketBytes();
        Date date=new Date();
        if (len==bufLen-headLen){
           socketBytes.setFitSocketPara(true);
            byte[] content=new byte[bufLen-headLen];
            System.arraycopy(buf,headLen,content,0,bufLen-headLen);
           socketBytes.setBytesContent(content);
        }else{
            socketBytes.setFitSocketPara(false);
            socketBytes.setBytesContent(buf);
        }
        socketBytes.setBytesLen(len);
        socketBytes.setCreatedDate(date);

        out.write(socketBytes);
        return true;

    }

    private byte[] readBuffer(IoBuffer in) throws IOException {
        if (in == null) return null;
        int length = in.limit();
        // LOGGER.info("readBuffer in.limit()="+length);
        byte[] bytes = new byte[length];
        in.get(bytes);
        in.flip();
        //  in.free();
        //ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        // return ImageIO.read(bais);
        return bytes;
    }
}
