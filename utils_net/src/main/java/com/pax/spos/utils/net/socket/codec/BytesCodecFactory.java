package com.pax.spos.utils.net.socket.codec;

import com.pax.spos.utils.net.socket.model.SocketPara;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by fable on 14-9-10.
 */
public class BytesCodecFactory implements ProtocolCodecFactory {
    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;


    public BytesCodecFactory(boolean isServer,SocketPara socketPara) {
        encoder = new BytesEncoder(isServer,socketPara);
        decoder = new BytesDecoder(isServer,socketPara);
    }

    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }
}