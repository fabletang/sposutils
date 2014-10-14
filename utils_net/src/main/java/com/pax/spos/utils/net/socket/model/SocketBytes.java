package com.pax.spos.utils.net.socket.model;

import com.pax.spos.utils.ByteStringHex;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by fable on 14-9-10.
 * socket 字节流对象
 * isFitSocketPara 是否复合socketPara.json 的规范
 * bytesLen 字节流长度， 如果 isFitSocketPara==false，为字节流本身长度
 * bytesContent 如果 isFitSocketPara==true, 为除去开始表示长度字符的字节流。
 * sendDate 发送的日期
 * receiveDate 接收的日期
 * isConnectTime boolean 是否连接超时
 * isReadTime boolean 是否读超时(接收超时)
 */
public class SocketBytes {
    private boolean isFitSocketPara;
    private int bytesLen;
    private byte[] bytesContent;
    // timeinms 为socketBytes对象产生的时间，用 1970年1月1日起的毫秒的数量表示日期.
//    private long timeinms;
    private Date sendDate;
    boolean isReadTimeout;
    boolean isWriteTimeout;
    boolean isConnectTimeout;
    private Date receiveDate;


    public SocketBytes() {
//        this.sendDate = new Date();
        this.isFitSocketPara=true;
    }

    @Override
    public String toString() {
        return "socketBytes{" +
                " isFitSocketPara=" + isFitSocketPara +
                " isConnectTimeout=" + isConnectTimeout+ '\'' +
                " isReadTimeout=" + isReadTimeout+ '\'' +
                " isWriteTimeout=" + isWriteTimeout + '\'' +
                ", bytesLen=" + bytesLen + '\'' +
//                ", bytesContent=" + Arrays.toString(bytesContent) + "\n" +
                ", bytesContent_HEX=" + ByteStringHex.bytes2HexStr(bytesContent) +'\'' +
                ", sendDate=" + formatDate(sendDate) +'\'' +
                ", receiveDate=" + formatDate(receiveDate) +
//                ", receiveDate=" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss-SSS").format(receiveDate) +
                '}';
    }
    private String formatDate(Date date){
        if (date==null){
            return null;
        }else{
           return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss-SSS").format(sendDate);
        }
    }

    public boolean isConnectTimeout() {
        return isConnectTimeout;
    }

    public void setConnectTimeout(boolean isConnectTimeout) {
        this.isConnectTimeout = isConnectTimeout;
    }

    public boolean isReadTimeout() {
        return isReadTimeout;
    }

    public void setReadTimeout(boolean isReadTimeout) {
        this.isReadTimeout = isReadTimeout;
    }

    public boolean isWriteTimeout() {
        return isWriteTimeout;
    }

    public void setWriteTimeout(boolean isWriteTimeout) {
        this.isWriteTimeout = isWriteTimeout;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public boolean isFitSocketPara() {
        return isFitSocketPara;
    }

    public void setFitSocketPara(boolean isFitSocketPara) {
        this.isFitSocketPara = isFitSocketPara;
    }

    public int getBytesLen() {
        return bytesLen;
    }

    public void setBytesLen(int bytesLen) {
        this.bytesLen = bytesLen;
    }

    public byte[] getBytesContent() {
        return bytesContent;
    }

    public void setBytesContent(byte[] bytesContent) {
        this.bytesContent = bytesContent;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}
