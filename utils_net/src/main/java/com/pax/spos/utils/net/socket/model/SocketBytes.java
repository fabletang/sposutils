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
 * createDate 为socketBytes对象产生的日期
 */
public class SocketBytes {
    private boolean isFitSocketPara;
    private int bytesLen;
    private byte[] bytesContent;
    // timeinms 为socketBytes对象产生的时间，用 1970年1月1日起的毫秒的数量表示日期.
//    private long timeinms;
    private Date createdDate;

    @Override
    public String toString() {
        return "socketBytes{" +
                "isFitSocketPara=" + isFitSocketPara +
                ", bytesLen=" + bytesLen +
                ", bytesContent=" + Arrays.toString(bytesContent) +
                ", bytesContent_HEX=" + ByteStringHex.bytes2HexStr(bytesContent) +
                ", createdDate=" +new SimpleDateFormat("yyyy-MM-dd hh:mm:ss-SSS").format(createdDate)+
                '}';
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
