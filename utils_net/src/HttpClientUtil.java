import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by fable on 14-9-12.
 */
public class HttpClientUtil {
    public static HttpBytes sendGetReq(HttpBytes httpBytes) throws Exception {
        if (httpBytes==null || httpBytes.getReqUrl().length()<10){
            return null;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(httpBytes.getReqUrl());
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
            } finally {
                response1.close();
            }

        } finally {
            httpclient.close();
        }
        return httpBytes;
    }

    public static HttpBytes sendPostReq(HttpBytes httpBytes) throws Exception {
        if (httpBytes==null || httpBytes.getReqUrl().length()<10){
            return null;
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        HttpPost httpPost = new HttpPost(httpBytes.getReqUrl());
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
        } finally {
            response2.close();
        }
        } finally {
            httpclient.close();
        }
        return httpBytes;
    }
}
