package com.pax.spos.utils.net.socket;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.net.socket.model.SocketBytes;
import com.pax.spos.utils.net.socket.model.SocketPara;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fable on 14-9-10.
 */
public class SocketClientUtil {
    private static SocketPara socketPara;

    public static void setSocketPara(SocketPara socketPara) {
        SocketClientUtil.socketPara = socketPara;
    }

    public static SocketPara getSocketPara() {
        return socketPara;
    }
//    public static InputStream socketParaInputSteam;
//
//    public static InputStream getSocketParaInputSteam() {
//        return socketParaInputSteam;
//    }

//    public static void setSocketParaInputSteam(InputStream socketParaInputSteam) {
//        SocketClientUtil.socketParaInputSteam = socketParaInputSteam;
//    }

//    public static SocketPara getSocketPara() throws IOException {
//        if (socketParaInputSteam==null){
//            return SocketParaJsonUtils.getInstance().parseJson("SocketPara.json");
//        }else{
//            return SocketParaJsonUtils.getInstance().parseJson(socketParaInputSteam);
//        }
//    }
    private static byte[] getHeadBytesByLenType(int len,int destLen,String lenType){
        if (lenType==null||lenType.equals("")) return ByteStringHex.int2FixBytes(len, destLen);
        if (lenType.equals("HEX")){
            return  ByteStringHex.int2FixBytes(len, destLen);
        }
        if (lenType.equals("BCD")){
            return  ByteStringHex.int2FixBcd(len, destLen);
        }
        if (lenType.equals("ASC")){
            return  ByteStringHex.int2FixAsc(len, destLen);
        }
        return null;
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
        dest=getHeadBytesByLenType(len,destLen,lenType);
        System.out.println("----- buildHeadBytes dest="+ByteStringHex.bytes2HexStr(dest));
//        switch (lenType) {
//            case "HEX": {
//                dest = ByteStringHex.int2FixBytes(len, destLen);
//                break;
//            }
//            case "BCD": {
//                dest = ByteStringHex.int2FixBcd(len, destLen);
//                break;
//            }
//            case "ASC": {
//                dest = ByteStringHex.int2FixAsc(len, destLen);
//                break;
//            }
//            default:
//                dest = ByteStringHex.int2FixBytes(len, destLen);
//        }
        return dest;

    }

    public static int getContentLen(byte[] bytes, boolean isIn) throws IOException {
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
        byte[] tmp = new byte[headLen];
        System.arraycopy(bytes, 0, tmp, 0, headLen);
        int dest = 0;
//        switch (lenType) {
//            case "HEX": {
//                dest = ByteStringHex.bytes2Int(tmp);
//                break;
//            }
//            case "BCD": {
//                dest = ByteStringHex.bcd2Int(tmp);
//                break;
//            }
//            case "ASC": {
//                dest = ByteStringHex.asc2Int(tmp);
//                break;
//            }
//            default:
//                dest = ByteStringHex.bytes2Int(tmp);
//        }

            if (lenType==null||lenType.equals("")) return ByteStringHex.bytes2Int(tmp);
            if (lenType.equals("HEX")){
                dest = ByteStringHex.bytes2Int(tmp);
            }
            if (lenType.equals("BCD")){
                dest = ByteStringHex.bcd2Int(tmp);
            }
            if (lenType.equals("ASC")){
                dest = ByteStringHex.asc2Int(tmp);
            }
        return dest;
    }

    /**
     * socket 短链接发送
     * <p/>
     * socket 字节流对象
     * isFitSocketPara 是否复合socketPara.json 的规范
     * bytesLen 字节流长度， 如果 isFitSocketPara==false，为字节流本身长度
     * bytesContent 如果 isFitSocketPara==true, 为除去开始表示长度字符的字节流。
     * sendDate 发送的日期
     * receiveDate 接收的日期
     * isConnectTime boolean 是否连接超时
     * isReadTime boolean 是否读超时(接收超时)
     *
     * @param socketBytesSend 待发送的 SocketBytes 对象,
     *                        isFitSocketPara(默认为false)/bytesContent (byte[])不能为空
     * @return SocketBytes 对象, 先根据socketPara.json规范来解释byte[],
     *                          符合的话剔除head,把内容放入bytesContent,
     *                          长度不符合，setFixSocketPara(false), 把全部byte[]放入bytesContent
     * @throws IOException
     */
    public static SocketBytes shortSend(SocketBytes socketBytesSend) throws IOException {
        if (socketPara==null) return null;
        ShortClient shortClient = new ShortClient(socketPara);
        return shortClient.send(socketBytesSend);
    }
    public static SocketBytes shortSend(SocketBytes socketBytesSend,String host,int port) throws IOException {
        if (socketPara==null) return null;
        ShortClient shortClient = new ShortClient(host,port,socketPara);
        return shortClient.send(socketBytesSend);
    }

    public static SocketBytes shortSend(SocketBytes socketBytesSend,SocketPara socketPara) throws IOException {
        setSocketPara(socketPara);
        if (socketPara==null) return null;
        ShortClient shortClient = new ShortClient(socketPara);
        return shortClient.send(socketBytesSend);
    }
    public static SocketBytes shortSend(SocketBytes socketBytesSend,String host,int port,SocketPara socketPara) throws IOException {
        setSocketPara(socketPara);
        if (socketPara==null) return null;
        ShortClient shortClient = new ShortClient(host,port,socketPara);
        return shortClient.send(socketBytesSend);
    }
}
