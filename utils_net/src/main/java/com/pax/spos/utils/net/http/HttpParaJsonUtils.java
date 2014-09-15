package com.pax.spos.utils.net.http;

import com.google.gson.Gson;
import com.pax.spos.utils.net.http.model.HttpPara;
import com.pax.spos.utils.net.http.model.HttpPara;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by fable on 14-9-2.
 * 解释tag.json 文件
 * For ClazzUtils
 */
public class HttpParaJsonUtils {
    private static HttpParaJsonUtils instance = null;
    private HttpPara httpPara;

    private HttpParaJsonUtils() {
    }

    public static HttpParaJsonUtils getInstance() {
        if (instance == null) {
            instance = new HttpParaJsonUtils();
        }
        return instance;
    }

    public HttpPara parseJson(String httpParaJsonPath) throws IOException {
        if (httpPara != null) {
            return httpPara;
        }

        if (httpParaJsonPath == null || httpParaJsonPath.length() < 6 || !httpParaJsonPath.endsWith(".json")) {
            return null;
        }
        InputStream is = this.getClass().getResourceAsStream(httpParaJsonPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Gson gson = new Gson();
        if (!br.ready()) return null;
        this.httpPara = gson.fromJson(br, HttpPara.class);
        return httpPara;
    }
}
