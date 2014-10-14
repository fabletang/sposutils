package com.pax.spos.utils.net.socket;

import com.pax.spos.utils.net.socket.codec.BytesCodecFactory;
import com.pax.spos.utils.net.socket.codec.BytesServerHandler;
import com.pax.spos.utils.net.socket.model.SocketPara;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by fable on 14-9-11.
 */
public class BytesServerTest {
    private static BytesServerTest instance = null;

    private BytesServerTest() {
    }

    public static BytesServerTest getInstance() {
        if (instance == null) {
            instance = new BytesServerTest();
        }
        return instance;
    }
    private static SocketPara socketPara;
//    @BeforeClass
    public static void main(String[] args) throws IOException {
        //socketPara= SocketClientUtil.getSocketPara();
        socketPara=SocketParaJsonUtils.getInstance().parseJson("SocketPara.json");
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new BytesCodecFactory(true,socketPara)));
        acceptor.setHandler(new BytesServerHandler());
        acceptor.getSessionConfig().setReadBufferSize( 2048 );
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 5000 );
        acceptor.bind( new InetSocketAddress(socketPara.getPort()) );
    }

//    @Test




}
