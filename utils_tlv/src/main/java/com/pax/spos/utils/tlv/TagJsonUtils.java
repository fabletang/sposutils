package com.pax.spos.utils.tlv;

import com.google.gson.Gson;
import com.pax.spos.utils.tlv.model.TagJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by fable on 14-9-2.
 * 解释tag.json 文件
 * For ClazzUtils
 */
public class TagJsonUtils {
    private static TagJsonUtils instance = null;
    private TagJson tagJson;

    private TagJsonUtils() {
    }

    public static TagJsonUtils getInstance() {
        if (instance == null) {
            instance = new TagJsonUtils();
        }
        return instance;
    }

    public TagJson parseJson(String tagjsonPath) throws IOException {
        if (tagJson != null) return tagJson;
        if (tagjsonPath == null || tagjsonPath.length() < 6 || !tagjsonPath.endsWith(".json")) {
            return null;
        }
//        FileReader file=new FileReader(tagjsonPath);
        //读jar包
//        if (!file.ready()){
//            file=new FileReader(new InputStreamReader(is).toString());
//            file=new FileReader(is.toString());
//        }
        InputStream is = this.getClass().getResourceAsStream(tagjsonPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Gson gson = new Gson();
        if (!br.ready()) return null;
        this.tagJson = gson.fromJson(br, TagJson.class);
        return tagJson;
    }

    /**
     * for androin read assets/*.json
     *
     * @param tagjson
     * @return
     * @throws IOException
     */
    public TagJson parseJson(InputStream tagjson) throws IOException {

        if (tagJson != null) return tagJson;
        if (tagjson == null || tagjson.available() < 6) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(tagjson));
        Gson gson = new Gson();
        if (!br.ready()) return null;
        this.tagJson = gson.fromJson(br, TagJson.class);
        return tagJson;
    }
}
