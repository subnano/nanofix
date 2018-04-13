package net.nanofix.session;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import net.nanofix.core.connector.ConnectionListener;
import net.nanofix.core.connector.Connector;
import net.nanofix.core.connector.MessageListener;
import net.nanofix.core.netty.FIXFrameDecoder;
import net.nanofix.core.netty.FIXMessageCodec;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXMessageFactory;
import net.nanofix.netty.AnonymousSessionHandler;
import net.nanofix.session.FIXLogonManager;
import net.nanofix.session.SessionManager;
import net.nanofix.util.ChannelHandlerNames;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:26
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final Connector connector;
    private final FIXMessageFactory fixMessageFactory;
    private final ConnectionListener connectionListener;
    private final MessageListener<FIXMessage> messageListener;
    private final SessionManager sessionManager;
    private final FIXLogonManager fixLogonManager;

    public ServerChannelInitializer(Connector connector,
                                    FIXMessageFactory fixMessageFactory,
                                    ConnectionListener connectionListener,
                                    MessageListener<FIXMessage> messageListener,
                                    SessionManager sessionManager,
                                    FIXLogonManager fixLogonManager) {
        this.connector = connector;
        this.fixMessageFactory = fixMessageFactory;
        this.connectionListener = connectionListener;
        this.messageListener = messageListener;
        this.sessionManager = sessionManager;
        this.fixLogonManager = fixLogonManager;
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(ChannelHandlerNames.FRAME_DECODER, new FIXFrameDecoder());
        ch.pipeline().addLast(ChannelHandlerNames.MESSAGE_CODEC, new FIXMessageCodec(fixMessageFactory, messageListener));
        ch.pipeline().addLast(ChannelHandlerNames.CHANNEL_HANDLER, new AnonymousSessionHandler(
                connector, connectionListener, messageListener, sessionManager, fixLogonManager));
    }
}
