package com.pax.spos.utils.net.socket;

import com.pax.spos.utils.net.socket.model.SocketPara;

import java.io.IOException;

/**
 * Created by fable on 14-9-10.
 */
public class SocketUtil {
    public static SocketPara getSocketPara() throws IOException {
        return SocketParaJsonUtils.getInstance().parseJson("SocketPara.json");
    }
}
