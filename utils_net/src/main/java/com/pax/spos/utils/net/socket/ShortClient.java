package com.pax.spos.utils.net.socket;

import com.pax.spos.utils.net.socket.codec.BytesClientHandler;
import com.pax.spos.utils.net.socket.codec.BytesCodecFactory;
import com.pax.spos.utils.net.socket.model.SocketBytes;
import com.pax.spos.utils.net.socket.model.SocketPara;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by fable on 14-9-11.
 */
public class ShortClient {
    //      private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ShortClient.class);
    public static Logger LOGGER = Logger.getLogger("BytesEncoder");

    private String host = "localhost";
    private int port = 3001;
    private int timeout_w = 30;
    private int timeout_r = 150;
    private int timeout_c = 70;
    private IoConnector connector;
    private IoSession session = null;
    private ProtocolCodecFilter protocolCodecFilter;
    private SocketPara socketPara;
    private IoHandler ioHandler;


    public ShortClient() throws IOException {
        this.socketPara = SocketClientUtil.getSocketPara();
        this.protocolCodecFilter = new ProtocolCodecFilter(new BytesCodecFactory(false));
        this.ioHandler = new BytesClientHandler();
        this.host = socketPara.getHost();
        this.port = socketPara.getPort();
        this.timeout_r = socketPara.getTimeout_r();
        this.timeout_w = socketPara.getTimeout_w();
        this.timeout_c = socketPara.getTimeout_c();

//        this.session=getSession();
        // init();
    }

    public ShortClient(SocketPara socketPara, ProtocolCodecFilter protocolCodecFilter) {
        this.socketPara = socketPara;
        this.protocolCodecFilter = protocolCodecFilter;
        this.host = socketPara.getHost();
        this.port = socketPara.getPort();
        this.timeout_r = socketPara.getTimeout_r();
        this.timeout_w = socketPara.getTimeout_w();
        this.timeout_c = socketPara.getTimeout_c();
        // init();
    }

    public ShortClient(SocketPara socketPara, ProtocolCodecFilter protocolCodecFilter, IoHandler ioHandler) {
        this.socketPara = socketPara;
        this.protocolCodecFilter = protocolCodecFilter;
        this.ioHandler = ioHandler;
        this.host = socketPara.getHost();
        this.port = socketPara.getPort();
        this.timeout_r = socketPara.getTimeout_r();
        this.timeout_w = socketPara.getTimeout_w();
        this.timeout_c = socketPara.getTimeout_c();
        // init();
    }

    public ShortClient(String host, int port) {
        this.host = host;
        this.port = port;
        protocolCodecFilter = new ProtocolCodecFilter(new BytesCodecFactory(false));
        // init();

    }

    public ShortClient(String host, int port, ProtocolCodecFilter protocolCodecFilter) {
        this.host = host;
        this.port = port;
        this.protocolCodecFilter = protocolCodecFilter;
        // init();
    }

    public ShortClient(String host, int port, ProtocolCodecFilter protocolCodecFilter, int timeout_w, int timeout_r) {
        this.host = host;
        this.port = port;
        this.protocolCodecFilter = protocolCodecFilter;
        this.timeout_r = timeout_r;
        this.timeout_w = timeout_w;
        this.timeout_c = timeout_w;
        //  init();
    }

    /**
     * @param host
     * @param port
     * @param protocolCodecFilter
     * @param timeout_c           connect timeout ms
     * @param timeout_w           write timeout ms
     * @param timeout_r           read timeout ms
     */
    public ShortClient(String host, int port, ProtocolCodecFilter protocolCodecFilter, int timeout_c, int timeout_w, int timeout_r) {
        this.host = host;
        this.port = port;
        this.protocolCodecFilter = protocolCodecFilter;
        this.timeout_r = timeout_r;
        this.timeout_w = timeout_w;
        this.timeout_c = timeout_c;
    }

    private IoSession getSession() {
        LOGGER.setLevel(Level.WARNING);
        if (session != null && session.isConnected()) {
            return session;
        }
        connector = new NioSocketConnector();
//        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec", protocolCodecFilter);
//        connector.setHandler(new BytesClientHandler());
//        connector.setHandler(ioHandler);
        //阻塞
        connector.getSessionConfig().setUseReadOperation(true);
//        connector.getSessionConfig().setReadBufferSize(105);
        connector.setConnectTimeoutMillis(timeout_c);
        // init();
        ConnectFuture future;
        try {
            future = connector.connect(new InetSocketAddress(host, port));
            if (future.awaitUninterruptibly(timeout_c)) {

            } else {
                LOGGER.warning("connect " + host + ":" + port + " timeout ms:" + timeout_c);
                // future.cancel();
                return null;
            }
            session = future.getSession();
            long sessionId;
            if (session != null) {
                sessionId = session.getId();
                LOGGER.info("sessionId:" + sessionId + " connect succes:" + host + ":" + port);
                return session;
            } else {
                LOGGER.info("session fault for " + host + ":" + port);
                return null;
            }

        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
            // clear();
            return null;
        }
    }

    public SocketBytes send(SocketBytes socketBytesSend) {
        if (socketBytesSend == null || socketBytesSend.getBytesContent() == null || socketBytesSend.getBytesContent().length < 1) {
            LOGGER.warning("------- shortclient socketBytesSend 不合法 ----");
            return null;
        }
        session = getSession();
        if (session == null || !session.isConnected()) {
            LOGGER.warning("------- shortclient socket 无法建立-----");
            return null;
        }
        try {
            socketBytesSend.setCreatedDate(new Date());
            session.write(socketBytesSend);
            ReadFuture readFuture;
            readFuture = session.read();
            if (readFuture.awaitUninterruptibly(timeout_r)) {
                Object message = readFuture.getMessage();
                // LOGGER.info("sessionId:"+sessionId+ "| "+message);
                if (message != null) {
//                    byte[] bytesReceived = (byte[]) (message);
//                    LOGGER.info("sessionId:" + sessionId + " +read:" + ByteStringHex.bytes2HexStr(bytesReceived));
                    SocketBytes socketBytesReceived = (SocketBytes) message;
                    LOGGER.info("-- ShortClient-" + " socketBytesReceived:" + socketBytesReceived);
                    // readFuture.getSession().resumeWrite();
                    if (socketBytesReceived != null) {
                        socketBytesReceived.setCreatedDate(new Date());
                    }
                    return socketBytesReceived;
                }
            }
        } catch (Exception e) {

        } finally {
//            session.close(true);
            clear();
        }
        return (SocketBytes) session.read().getMessage();
    }


    private void clear() {
        if (session != null) {
            session.close(true);
            session.getService().dispose();
        }
        if (connector != null) {
            connector.dispose();
        }
        // LOGGER.info("sessionId:"+session.getId()+" close++ succes:" + host + ":" + port);
    }
}
