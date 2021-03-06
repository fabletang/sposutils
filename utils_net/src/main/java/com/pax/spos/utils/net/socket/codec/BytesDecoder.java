package com.pax.spos.utils.net.socket.codec;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.net.socket.SocketClientUtil;
import com.pax.spos.utils.net.socket.model.SocketBytes;
import com.pax.spos.utils.net.socket.model.SocketPara;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by fable on 14-9-10.
 */
public class BytesDecoder extends CumulativeProtocolDecoder {
    public static Logger LOGGER = Logger.getLogger("BytesDecoder");
    private boolean isServer;
    private SocketPara socketPara;

    public BytesDecoder(boolean isServer,SocketPara socketPara) {
        this.isServer = isServer;
        this.socketPara=socketPara;
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        LOGGER.setLevel(Level.WARNING);
        byte[] buf = readBuffer(in);
//        in.flip();
        in.free();
        if (buf == null) return false;
        int bufLen = buf.length;
        if (bufLen < 1) {
            return false;
        }
        LOGGER.info("doDecode isServer:" + isServer + " inBytes=" + ByteStringHex.bytes2HexStr(buf));
//        SocketPara socketPara = SocketClientUtil.getSocketPara();
        if (socketPara == null) return false;
        int headLen;
        if (isServer) {
            headLen = socketPara.getHeadLenNum_out();
        } else {
            headLen = socketPara.getHeadLenNum_in();
        }
        if (headLen > bufLen || headLen < 1) return false;
//        byte[] headBytes= new byte[headLen];
//        System.arraycopy(buf,0,headBytes,0,headLen-1);
//        int len= ByteStringHex.bytes2Int(headBytes);
        int len = SocketClientUtil.getContentLen(buf, !isServer);
        SocketBytes socketBytes = new SocketBytes();
//        Date date = new Date();
        if (len == bufLen - headLen) {
            socketBytes.setFitSocketPara(true);
            byte[] content = new byte[bufLen - headLen];
            System.arraycopy(buf, headLen, content, 0, bufLen - headLen);
            socketBytes.setBytesContent(content);
//            socketBytes.setBytesLen(content.length);
        } else {
            socketBytes.setFitSocketPara(false);
            socketBytes.setBytesContent(buf);
//            socketBytes.setBytesLen(bufLen);
        }
        socketBytes.setBytesLen(len);
        socketBytes.setBytesLen(socketBytes.getBytesContent().length);
//        socketBytes.setSendDate(date);

        out.write(socketBytes);
        LOGGER.info("doDecode isServer:" + isServer + " out =" + socketBytes);
        socketBytes = null;
//        return true;
        return false;

    }

    private byte[] readBuffer(IoBuffer in) throws IOException {
        if (in == null) return null;
        int length = in.limit();
        LOGGER.info("readBuffer in.limit()=" + length);
        byte[] bytes = new byte[length];
        in.get(bytes);
        in.flip();
        //  in.free();
        //ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        // return ImageIO.read(bais);
        return bytes;
    }
}
