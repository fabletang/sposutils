package com.pax.spos.utils.net.model;


/**
 * Created by fable on 14-9-9.
 * timeout_c 连接超时 _w 写超时 _r 读超时, 毫秒 ms
 * headLenNum_in 开始几个byte 作为长度
 * lenType_in 长度类型 HEX BCD ASC
 */
public class sockBytesPara {
    private String host;
    private int port;
    private int timeout_c;
    private int timeout_w;
    private int timeout_r;

    private int headLenNum_in;
    private String lenType_in;

    private int headLenNum_out;
    private String lenType_out;

    @Override
    public String toString() {
        return "sockBytesPara{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", timeout_c=" + timeout_c +
                ", timeout_w=" + timeout_w +
                ", timeout_r=" + timeout_r +
                ", headLenNum_in=" + headLenNum_in +
                ", lenType_in='" + lenType_in + '\'' +
                ", headLenNum_out=" + headLenNum_out +
                ", lenType_out='" + lenType_out + '\'' +
                '}';
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout_c() {
        return timeout_c;
    }

    public void setTimeout_c(int timeout_c) {
        this.timeout_c = timeout_c;
    }

    public int getTimeout_w() {
        return timeout_w;
    }

    public void setTimeout_w(int timeout_w) {
        this.timeout_w = timeout_w;
    }

    public int getTimeout_r() {
        return timeout_r;
    }

    public void setTimeout_r(int timeout_r) {
        this.timeout_r = timeout_r;
    }

    public int getHeadLenNum_in() {
        return headLenNum_in;
    }

    public void setHeadLenNum_in(int headLenNum_in) {
        this.headLenNum_in = headLenNum_in;
    }

    public String getLenType_in() {
        return lenType_in;
    }

    public void setLenType_in(String lenType_in) {
        this.lenType_in = lenType_in;
    }

    public int getHeadLenNum_out() {
        return headLenNum_out;
    }

    public void setHeadLenNum_out(int headLenNum_out) {
        this.headLenNum_out = headLenNum_out;
    }

    public String getLenType_out() {
        return lenType_out;
    }

    public void setLenType_out(String lenType_out) {
        this.lenType_out = lenType_out;
    }
}
