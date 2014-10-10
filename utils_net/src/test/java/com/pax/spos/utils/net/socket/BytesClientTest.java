package com.pax.spos.utils.net.socket;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.net.socket.model.SocketBytes;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by fable on 14-9-11.
 */
public class BytesClientTest {
    private static ShortClient shortClient;
    public Logger LOGGER = Logger.getLogger(this.getClass().getName());
    //@BeforeClass
    public static void init() throws IOException {
       BytesServerTest.getInstance();
        shortClient=new ShortClient();
        assertNotNull(shortClient);
    }
    //@Test
    public void check() throws Exception{
        SocketBytes socketBytes=new SocketBytes();
        String hexStr="010203040506";
        byte[] bytesSend= ByteStringHex.hexStr2Bytes(hexStr);
        socketBytes.setBytesContent(bytesSend);
        LOGGER.info("send 1:"+socketBytes.toString());
        SocketBytes socketBytesReceived=shortClient.send(socketBytes);
        assertNotNull(socketBytesReceived);
        LOGGER.info("receive 1:"+socketBytesReceived);
        socketBytes=null;
        LOGGER.info("send 2:"+null);
        socketBytesReceived=shortClient.send(socketBytes);
        assertNull(socketBytesReceived);
        LOGGER.info("receive 2:"+socketBytesReceived);

        hexStr="66666640506";
        bytesSend= ByteStringHex.hexStr2Bytes(hexStr);
        socketBytes=new SocketBytes();
        socketBytes.setBytesContent(bytesSend);
        socketBytes.setFitSocketPara(true);
        LOGGER.info("send 3:"+socketBytes.toString());
        socketBytesReceived=shortClient.send(socketBytes);
        assertNotNull(socketBytesReceived);
        LOGGER.info("receive 3:"+socketBytesReceived);

    }

}
