package com.pax.spos.utils.net.socket;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.net.socket.model.SocketPara;

import java.io.IOException;

/**
 * Created by fable on 14-9-10.
 */
public class SocketUtil {
    public static SocketPara getSocketPara() throws IOException {
        return SocketParaJsonUtils.getInstance().parseJson("SocketPara.json");
    }

    public static byte[] buildHeadBytes(int len, boolean isIn) throws IOException {
        SocketPara socketPara = getSocketPara();
        if (socketPara == null) return null;
        String lenType;
        byte[] dest;
        int destLen;
        if (isIn) {
            lenType = socketPara.getLenType_in();
            destLen = socketPara.getHeadLenNum_in();
        } else {
            lenType = socketPara.getLenType_out();
            destLen = socketPara.getHeadLenNum_out();
        }
        switch (lenType) {
            case "HEX": {
                dest = ByteStringHex.int2FixBytes(len, destLen);
                break;
            }
            case "BCD": {
                dest = ByteStringHex.int2FixBcd(len, destLen);
                break;
            }
            case "ASC": {
                dest = ByteStringHex.int2FixAsc(len, destLen);
                break;
            }
            default:
                dest = ByteStringHex.int2FixBytes(len, destLen);
        }
        return dest;

    }

    public static int getContentLen (byte[] bytes,boolean isIn) throws IOException {
        SocketPara socketPara = getSocketPara();
        if (socketPara == null) return 0;
        String lenType;
        byte[] headBytes;
        int headLen;
        if (isIn) {
            lenType = socketPara.getLenType_in();
            headLen = socketPara.getHeadLenNum_in();
        } else {
            lenType = socketPara.getLenType_out();
            headLen = socketPara.getHeadLenNum_out();
        }
        byte[] tmp=new byte[headLen];
        System.arraycopy(bytes,0,tmp,0,headLen);
        int dest;
        switch (lenType) {
            case "HEX": {
               dest=ByteStringHex.bytes2Int(tmp);
                break;
            }
            case "BCD": {
                dest=ByteStringHex.bcd2Int(tmp);
                break;
            }
            case "ASC": {
                dest=ByteStringHex.asc2Int(tmp);
                break;
            }
            default:
                dest=ByteStringHex.bytes2Int(tmp);
        }

        return dest;
    }

}
