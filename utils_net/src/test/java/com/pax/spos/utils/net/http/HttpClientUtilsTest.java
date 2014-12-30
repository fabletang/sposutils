package com.pax.spos.utils.net.http;

import com.pax.spos.utils.net.http.HttpClientUtil;
import com.pax.spos.utils.net.http.model.HttpBytes;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

public class HttpClientUtilsTest {
    HttpBytes httpBytes;
   @Before
   public void init(){
      httpBytes=new HttpBytes();
      httpBytes.setReqUrl("http://127.0.0.1:6666/myApp");
   }
   //@Test
   public void testHttpGet() throws Exception {
     httpBytes= HttpClientUtil.sendGetReq(httpBytes);
     System.out.println(httpBytes);
   }
    //@Test
    public void testHttpPost() throws Exception {
        httpBytes=HttpClientUtil.sendPostReq(httpBytes);
        System.out.println(httpBytes);
    }
    //@Test
    public void testHttpsGet() throws Exception {
//        httpBytes.setReqUrl("https://localhost:10433/json");
//        httpBytes= HttpClientUtil.sendSSLGettReq(httpBytes);
//        System.out.println(httpBytes);

       // public final static void main(String[] args) throws Exception {
        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
//        FileInputStream instream = new FileInputStream(new File("my.keystore"));
//        try {
//            trustStore.load(instream, "nopassword".toCharArray());
//        } finally {
//            instream.close();
//        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
//                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {

            HttpGet httpget = new HttpGet("https://localhost:10433/json");

            System.out.println("executing request" + httpget.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
