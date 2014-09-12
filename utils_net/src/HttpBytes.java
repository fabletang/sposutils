import org.apache.http.NameValuePair;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by fable on 14-9-12.
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

    public HttpBytes() {
        sendDate=new Date();
    }

    @Override
    public String toString() {
        return "HttpBytes{" +
                "reqUrl='" + reqUrl + '\'' +
                ", sslKey=" + sslKey +
                ", nvps=" + nvps +
                ", sendDate=" + sendDate +
                ", httpStatusCode=" + httpStatusCode +
                ", responseBytes=" + Arrays.toString(responseBytes) +
                ", receiveDate=" + receiveDate +
                '}';
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
