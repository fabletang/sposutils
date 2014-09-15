package com.pax.spos.utils.net.http; /**
 * Created by fable on 14-9-12.
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.*;
import java.net.InetSocketAddress;

public class HttpServerUtil {
    //启动服务，监听来自客户端的请求
    public static void httpserverService() throws IOException {
        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer httpserver =provider.createHttpServer(new InetSocketAddress(6666), 10);//监听端口6666,能同时接 受100个请求
        httpserver.createContext("/myApp", new MyHttpHandler());
        httpserver.setExecutor(null);
        httpserver.start();
        System.out.println("server started");
    }
    //Http请求处理类
    static class MyHttpHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) throws IOException {
            String responseMsg = "ok";   //响应信息
            InputStream in = httpExchange.getRequestBody(); //获得输入流
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String temp = null;
            while((temp = reader.readLine()) != null) {
                System.out.println("client request:"+temp);
            }
            httpExchange.sendResponseHeaders(200, responseMsg.getBytes().length); //设置响应头属性及响应信息的长度
            OutputStream out = httpExchange.getResponseBody();  //获得输出流
            out.write(responseMsg.getBytes());
            out.flush();
            httpExchange.close();
        }
    }
    public static void main(String[] args) throws IOException {
        httpserverService();
    }
}
