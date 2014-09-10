package com.pax.spos.utils.net.socket;

import com.google.gson.Gson;
import com.pax.spos.utils.net.socket.model.SocketPara;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by fable on 14-9-2.
 * 解释tag.json 文件
 * For ClazzUtils
 */
public class SocketParaJsonUtils {
    private static SocketParaJsonUtils instance = null;

    private SocketParaJsonUtils() {
    }

    public static SocketParaJsonUtils getInstance() {
        if (instance == null) {
            instance = new SocketParaJsonUtils();
        }
        return instance;
    }

    public SocketPara parseJson(String socketParaJsonPath) throws IOException {
        if (socketParaJsonPath == null || socketParaJsonPath.length() < 6 || !socketParaJsonPath.endsWith(".json")) {
            return null;
        }
        InputStream is = this.getClass().getResourceAsStream(socketParaJsonPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Gson gson = new Gson();
        if (!br.ready()) return null;
        return gson.fromJson(br, SocketPara.class);
    }
}
