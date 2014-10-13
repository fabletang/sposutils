package com.pax.spos.utils.net.socket.codec;

import com.pax.spos.utils.net.socket.model.SocketBytes;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

/**
 * Created by fable on 14-9-11.
 * for server test
 */
public class BytesClientHandler extends IoHandlerAdapter {
    private SocketBytes socketBytes;

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        session.close(true);
//        System.out.println("Client 收到消息:" + socketBytes);
//        if (message==null){
//            session.close(true);
//        }
//        socketBytes = (SocketBytes) message;
//        socketBytes.setSendDate(new Date());
//        byte[] dest = socketBytes.getBytesContent();
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("Client 会话关闭");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        System.out.println("Client 会话异常");
//        super.exceptionCaught(session, cause);
//        session.close(true);
    }

    @Override
    public void messageSent(IoSession iosession, Object obj) throws Exception {
        System.out.println("Client 消息发送");
//        super.messageSent(iosession, obj);
//        iosession.close(true);
    }

    @Override
    public void sessionCreated(IoSession iosession) throws Exception {
        System.out.println("Client 会话创建");
        super.sessionCreated(iosession);
    }

    @Override
    public void sessionIdle(IoSession iosession, IdleStatus idlestatus)
            throws Exception {
        System.out.println("Client 会话休眠");
        super.sessionIdle(iosession, idlestatus);
    }

    @Override
    public void sessionOpened(IoSession iosession) throws Exception {
        System.out.println("Client 会话打开");
        super.sessionOpened(iosession);
    }
}
