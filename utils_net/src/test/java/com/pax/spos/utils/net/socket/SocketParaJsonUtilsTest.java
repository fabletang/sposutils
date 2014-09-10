package com.pax.spos.utils.net.socket;

import com.pax.spos.utils.net.socket.model.SocketPara;
import org.junit.Test;

import static org.junit.Assert.*;

public class SocketParaJsonUtilsTest {

    @Test
    public void testParseJson() throws Exception {
        SocketParaJsonUtils socketParaJsonUtils=SocketParaJsonUtils.getInstance();
        SocketPara socketPara=socketParaJsonUtils.parseJson("SocketPara.json");
        assertNotNull(socketPara);
//        assertEquals("socketPara", socketPara.);
        assertTrue(socketPara.getPort()>100);
        assertTrue(socketPara.getPort()<60000);
//        System.out.println("port="+socketPara.getPort());
    }
}