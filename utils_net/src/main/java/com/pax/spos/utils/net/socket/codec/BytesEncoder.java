package com.pax.spos.utils.net.socket.codec;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.net.socket.SocketParaJsonUtils;
import com.pax.spos.utils.net.socket.SocketUtil;
import com.pax.spos.utils.net.socket.model.SocketBytes;
import com.pax.spos.utils.net.socket.model.SocketPara;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.util.logging.Logger;

/**
 * Created by fable on 14-9-10.
 */
public class BytesEncoder implements ProtocolEncoder {
//     private final static Logger LOGGER = LoggerFactory.getLogger(MReqEncoder.class);
    public static Logger LOGGER= Logger.getLogger("BytesEncoder");
    // private boolean LH;
    private boolean isServer;
    /**
//     * @param crcType l6LH 16HL NONE
     */
//    public BytesEncoder(String crcType) {
//        this.crcType = crcType;
//    }
    public BytesEncoder(boolean isServer) {
        this.isServer=isServer;
    }

        public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
            SocketBytes socketBytes=(SocketBytes)message;
            if (socketBytes.getBytesContent()==null)return;

        byte[] bytesOut;
        //todo justSocketBytes
        if (socketBytes.isFitSocketPara()){
            SocketPara socketPara= SocketUtil.getSocketPara();
            if (socketPara==null) return;
            int bytesContentLen=socketBytes.getBytesContent().length;
            if (bytesContentLen<1)return;
            int headLen;
            if (isServer){
                headLen=socketPara.getHeadLenNum_in();
            }else {
                headLen=socketPara.getHeadLenNum_out();
            }
            int len= headLen + bytesContentLen;
            if (len<1)return;
            bytesOut=new byte[len];
            byte[] lenBytes= ByteStringHex.int2FixBytes(bytesContentLen,headLen);
            if (lenBytes.length<1)return;
            System.arraycopy(lenBytes,0,bytesOut,0,lenBytes.length);
            System.arraycopy(socketBytes.getBytesContent(),0,bytesOut,lenBytes.length,bytesContentLen);
        }else{
            int bytesContentLen=socketBytes.getBytesContent().length;
            if (bytesContentLen<1)return;
            bytesOut=socketBytes.getBytesContent();
        }
        IoBuffer buffer = IoBuffer.allocate(bytesOut.length, false);
        buffer.put(bytesOut);
        buffer.flip();
        out.write(buffer);
        buffer.free();
        LOGGER.info("--ByteEnCoder:isServer-"+isServer+" Hex:"+ByteStringHex.bytes2HexStr(bytesOut));
    }

    public void dispose(IoSession session) throws Exception {
        // nothing to dispose
    }
}
