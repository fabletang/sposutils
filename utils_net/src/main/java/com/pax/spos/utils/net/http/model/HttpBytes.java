package com.pax.spos.utils.net.http.model;

import com.pax.spos.utils.ByteStringHex;
import org.apache.http.NameValuePair;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by fable on 14-9-12.
 * reqUrl String 请求字符串
 * sslKey File https请求所需要的公钥
 * nvps   NameValuePairh ttp post 请求的键值对List
 * httpStatusCode int http server 返回码， 200 为正常
 * responseBytes byte[] 返回的bytes
 * sendDate 发送日期
 * receiveDate 接收日期
 */
public class HttpBytes {
    String reqUrl;
    File sslKey;
    //post 参数
    List<NameValuePair> nvps;
    Date sendDate;
    int httpStatusCode;
    byte[] responseBytes;
    Date receiveDate;

    boolean isReadTimeout;
    boolean isConnectTimeout;

    public HttpBytes() {
//        sendDate=new Date();
    }

    @Override
    public String toString() {
        return "HttpBytes{" +
                "reqUrl='" + reqUrl + '\'' +
                ", sslKey=" + sslKey +
                ", nvps=" + nvps +
                ", sendDate=" + formatDate(sendDate) +
                ", httpStatusCode=" + httpStatusCode +
                ", responseBytes=" + ByteStringHex.bytes2HexStr(responseBytes) +
                ", receiveDate=" + formatDate(receiveDate)+
                '}';
    }

    private String formatDate(Date date){
        if (date==null){
            return null;
        }else{
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss-SSS").format(sendDate);
        }
    }

    public boolean isReadTimeout() {
        return isReadTimeout;
    }

    public void setReadTimeout(boolean isReadTimeout) {
        this.isReadTimeout = isReadTimeout;
    }

    public boolean isConnectTimeout() {
        return isConnectTimeout;
    }

    public void setConnectTimeout(boolean isConnectTimeout) {
        this.isConnectTimeout = isConnectTimeout;
    }

    public List<NameValuePair> getNvps() {
        return nvps;
    }

    public void setNvps(List<NameValuePair> nvps) {
        this.nvps = nvps;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public byte[] getResponseBytes() {
        return responseBytes;
    }

    public void setResponseBytes(byte[] responseBytes) {
        this.responseBytes = responseBytes;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public File getSslKey() {
        return sslKey;
    }

    public void setSslKey(File sslKey) {
        this.sslKey = sslKey;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }
}
