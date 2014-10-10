package com.pax.spos.utils.net.socket;

import com.pax.spos.utils.ByteStringHex;
import com.pax.spos.utils.net.socket.model.SocketBytes;
import com.pax.spos.utils.net.socket.model.SocketPara;
import org.junit.Before;
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
   // @Before
    public  void init() throws IOException {
       //BytesServerTest.getInstance();
       SocketPara socketPara = SocketParaJsonUtils.getInstance().parseJson("SocketPara.json");

        assertNotNull(socketPara);
        System.out.println("ByteClientTest. socketPara="+socketPara);
        shortClient=new ShortClient(socketPara);
        assertNotNull(shortClient);
    }
   // @Test
    public void check() throws Exception{
        SocketPara socketPara = SocketParaJsonUtils.getInstance().parseJson("SocketPara.json");

        assertNotNull(socketPara);
        System.out.println("ByteClientTest. socketPara="+socketPara);
        shortClient=new ShortClient(socketPara);
        assertNotNull(shortClient);

        SocketBytes socketBytes=new SocketBytes();
        String hexStr="010203040506";
        byte[] bytesSend= ByteStringHex.hexStr2Bytes(hexStr);
        socketBytes.setBytesContent(bytesSend);
        socketBytes.setFitSocketPara(true);
        LOGGER.info("send 1:"+socketBytes.toString());
        //SocketBytes socketBytesReceived=shortClient.send(socketBytes);
        SocketBytes socketBytesReceived=SocketClientUtil.shortSend(socketBytes,socketPara);
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
