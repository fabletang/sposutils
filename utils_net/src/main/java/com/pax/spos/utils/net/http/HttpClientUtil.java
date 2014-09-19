package com.pax.spos.utils.net.http;

import com.pax.spos.utils.net.http.model.HttpBytes;
import com.pax.spos.utils.net.http.model.HttpPara;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.util.Date;
import java.util.List;

/**
 * Created by fable on 14-9-12.
 * http 请求的4种方法， http get/post和 https get/post
 * httpParaInputSteam 用于解决 android 不能访问jar目录的HttpPara.json的问题
 * httpBytes 解释
 * reqUrl String 请求字符串
 * sslKey File https请求所需要的公钥
 * nvps   NameValuePairh ttp post 请求的键值对List
 * httpStatusCode int http server 返回码， 200 为正常
 * responseBytes byte[] 返回的bytes
 * sendDate 发送日期
 * receiveDate 接收日期
 * isConnectTime boolean 是否连接超时
 * isReadTime boolean 是否读超时(接收超时)
 */
public class HttpClientUtil {

    /**
     * 解释 HttpPara.json 得到 http连接参数
     * @return
     * @throws IOException
     */
    public static int timeout_c;
    public static int timeout_r;
    public static HttpPara httpPara;
    public static RequestConfig requestConfig;

    public static InputStream httpParaInputSteam;

    public static InputStream getHttpParaInputSteam() {
        return httpParaInputSteam;
    }

    public static void setHttpParaInputSteam(InputStream httpParaInputSteam) {
        HttpClientUtil.httpParaInputSteam = httpParaInputSteam;
    }

    /**
     * 得到并设置http连接参数，如果httpPara==null或者timeout_c==0, timeout_c=5秒, timeout_r=10秒
     * @return
     * @throws IOException
     */
    public static HttpPara getHttpPara() throws IOException {
        if (httpPara==null){
            if (httpParaInputSteam==null){
                httpPara=HttpParaJsonUtils.getInstance().parseJson("HttpPara.json");
            }else{
                httpPara=HttpParaJsonUtils.getInstance().parseJson(httpParaInputSteam);
            }
            if (httpPara==null || httpPara.getTimeout_c()==0){
                timeout_c=5000;
            }else {
                timeout_c=httpPara.getTimeout_c();
            }
            if (httpPara==null || httpPara.getTimeout_r()==0){
                timeout_r=10000;
            }else {
                timeout_r=httpPara.getTimeout_r();
            }
            requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeout_c)
                    .setConnectionRequestTimeout(timeout_r)
                    .build();
        }
        return httpPara;
    }

    /**
     * 发送 http get 请求
     * @param httpBytes reqUrl 不能为空
     * @return htppBytes 具体见 com.pax.spas.utils.net.http.model
     * @throws Exception
     */
    public static HttpBytes sendGetReq(HttpBytes httpBytes) throws Exception {
        if (httpBytes==null || httpBytes.getReqUrl().length()<10){
            return null;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            Date sendDate=new Date();
            httpBytes.setSendDate(sendDate);
            HttpGet httpGet = new HttpGet(httpBytes.getReqUrl());
            if(requestConfig==null){httpPara=getHttpPara();}
            httpGet.setConfig(requestConfig);

            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            int statusCode=response1.getStatusLine().getStatusCode();
            httpBytes.setReceiveDate(new Date());
            httpBytes.setHttpStatusCode(statusCode);
//            if (response1.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
//                httpBytes.setHttpStatusCode(statusCode);
////                return httpBytes;
//            }
            try {
            //    System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
               byte[] dest=EntityUtils.toByteArray(entity1);
                httpBytes.setResponseBytes(dest);
                EntityUtils.consume(entity1);
            }
            catch (SocketTimeoutException ex) {
                httpBytes.setReadTimeout(true);
            }finally {
                response1.close();
            }
        }
         catch (SocketTimeoutException ex){
             httpBytes.setConnectTimeout(true);

        } finally {
            httpclient.close();
        }
        return httpBytes;
    }

    /**
     * 发送 http post 请求
     *
     * httpBytes 解释
     * reqUrl String 请求字符串
     * sslKey File https请求所需要的公钥
     * nvps   NameValuePairh ttp post 请求的键值对List
     * httpStatusCode int http server 返回码， 200 为正常
     * responseBytes byte[] 返回的bytes
     * sendDate 发送日期
     * receiveDate 接收日期
     * isConnectTime boolean 是否连接超时
     * isReadTime boolean 是否读超时(接收超时)
     * @param httpBytes reqUrl 不能为空
     * @return htppBytes 具体见 com.pax.spas.utils.net.http.model
     * @throws Exception
     */
    public static HttpBytes sendPostReq(HttpBytes httpBytes) throws Exception {
        if (httpBytes==null || httpBytes.getReqUrl().length()<10){
            return null;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            Date sendDate=new Date();
            httpBytes.setSendDate(sendDate);
        HttpPost httpPost = new HttpPost(httpBytes.getReqUrl());
            if(requestConfig==null){httpPara=getHttpPara();}
            httpPost.setConfig(requestConfig);
        List<NameValuePair> nvps = httpBytes.getNvps();
//        nvps.add(new BasicNameValuePair("username", "vip"));
//        nvps.add(new BasicNameValuePair("password", "secret"));
            if (nvps!=null&&nvps.size()>0) {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            }
        CloseableHttpResponse response2 = httpclient.execute(httpPost);
            int statusCode=response2.getStatusLine().getStatusCode();
            httpBytes.setReceiveDate(new Date());
            httpBytes.setHttpStatusCode(statusCode);
        try {
//            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            byte[] dest=EntityUtils.toByteArray(entity2);
            httpBytes.setResponseBytes(dest);
            EntityUtils.consume(entity2);
        }
        catch (SocketTimeoutException ex) {
            httpBytes.setReadTimeout(true);
        } finally {
            response2.close();
        }
        }
        catch (SocketTimeoutException ex){
            httpBytes.setConnectTimeout(true);
        } finally {
            httpclient.close();
        }
        return httpBytes;
    }
    /**
     * 发送 https post 请求
     * @param httpBytes reqUrl 不能为空, sslKey(file) 不能为空
     * @return htppBytes 具体见 com.pax.spas.utils.net.http.model
     * @throws Exception
     */
    public static HttpBytes sendSSLPostReq(HttpBytes httpBytes) throws Exception {
        if (httpBytes==null || httpBytes.getReqUrl().length()<10){
            return null;
        }
        if (httpBytes.getSslKey()==null ||httpBytes.getSslKey().length()<128){
            return null;
        }

        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(httpBytes.getSslKey());
        try {
            trustStore.load(instream, "nopassword".toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            Date sendDate=new Date();
            httpBytes.setSendDate(sendDate);

            HttpPost httpPost = new HttpPost(httpBytes.getReqUrl());
            if(requestConfig==null){httpPara=getHttpPara();}
            httpPost.setConfig(requestConfig);
            List<NameValuePair> nvps = httpBytes.getNvps();
            if (nvps!=null&&nvps.size()>0) {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            }
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            int statusCode=response2.getStatusLine().getStatusCode();
            httpBytes.setReceiveDate(new Date());
            httpBytes.setHttpStatusCode(statusCode);
            try {
//            System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // and ensure it is fully consumed
                byte[] dest=EntityUtils.toByteArray(entity2);
                httpBytes.setResponseBytes(dest);
                EntityUtils.consume(entity2);
            }
            catch (SocketTimeoutException ex) {
                httpBytes.setReadTimeout(true);
            } finally {
                response2.close();
            }
        }
        catch (SocketTimeoutException ex){
            httpBytes.setConnectTimeout(true);
        } finally {
            httpclient.close();
        }
        return httpBytes;
    }
    /**
     * 发送 https get 请求
     * @param httpBytes reqUrl 不能为空, sslKey(file) 不能为空
     * @return htppBytes 具体见 com.pax.spas.utils.net.http.model
     * @throws Exception
     */
    public static HttpBytes sendSSLGettReq(HttpBytes httpBytes) throws Exception {
        if (httpBytes==null || httpBytes.getReqUrl().length()<10){
            return null;
        }
        if (httpBytes.getSslKey()==null ||httpBytes.getSslKey().length()<128){
            return null;
        }

        KeyStore trustStore  = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(httpBytes.getSslKey());
        try {
            trustStore.load(instream, "nopassword".toCharArray());
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            Date sendDate=new Date();
            httpBytes.setSendDate(sendDate);
            HttpGet httpGet = new HttpGet(httpBytes.getReqUrl());
            if(requestConfig==null){httpPara=getHttpPara();}
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.
            int statusCode=response1.getStatusLine().getStatusCode();
            httpBytes.setReceiveDate(new Date());
            httpBytes.setHttpStatusCode(statusCode);
            try {
//            System.out.println(response2.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // and ensure it is fully consumed
                byte[] dest=EntityUtils.toByteArray(entity1);
                httpBytes.setResponseBytes(dest);
                EntityUtils.consume(entity1);
            }
            catch (SocketTimeoutException ex) {
                httpBytes.setReadTimeout(true);
            } finally {
                response1.close();
            }
        }
        catch (SocketTimeoutException ex){
            httpBytes.setConnectTimeout(true);
        } finally {
            httpclient.close();
        }
        return httpBytes;
    }
}
