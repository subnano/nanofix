package net.nanofix.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.nanofix.core.connector.ConnectionListener;
import net.nanofix.core.connector.ConnectionState;
import net.nanofix.core.connector.MessageListener;
import net.nanofix.core.netty.AbstractConnector;
import net.nanofix.session.ServerChannelInitializer;
import net.nanofix.message.DefaultFIXMessageFactory;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXMessageFactory;
import net.nanofix.session.*;
import net.nanofix.store.MemoryMessageStoreFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:11
 */
public class ServerConnector extends AbstractConnector<FIXMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerConnector.class);

    private final InetSocketAddress localAddress;
    private final ServerBootstrap bootstrap;
    private MessageListener<FIXMessage> messageListener;
    private FIXMessageFactory fixMessageFactory;
    private SessionManager sessionManager;
    private FIXLogonManager fixLogonManager;

    public ServerConnector(InetSocketAddress localAddress, SessionFactoryListener sessionFactoryListener) {
        this(localAddress,
                new DefaultFIXMessageFactory(),
                new NanoSessionManager(new DynamicSessionFactory(sessionFactoryListener, new MemoryMessageStoreFactory())),
                new AnonymousFIXLogonManager());
    }

    public ServerConnector(InetSocketAddress localAddress,
                           FIXMessageFactory messageFactory,
                           SessionManager sessionManager,
                           FIXLogonManager fixLogonManager) {

        this.localAddress = localAddress;
        this.fixMessageFactory = messageFactory;
        this.sessionManager = sessionManager;
        this.fixLogonManager = fixLogonManager;
        this.bootstrap = new ServerBootstrap();
    }

    @Override
    public ConnectionState connect(long timeoutMillis) {
        throw new UnsupportedOperationException("connect(timeoutMillis)");
    }

    @Override
    public ConnectionState connect(long timeoutMillis, MessageListener<FIXMessage> messageListener) {
        throw new UnsupportedOperationException("connect(timeoutMillis,messageListener)");
    }

    @Override
    public void connect(ConnectionListener connectionListener, MessageListener<FIXMessage> messageListener) {
        this.messageListener = messageListener;
        LOGGER.trace("connecting...");
        initChannel(connectionListener, messageListener);
    }

    private void initChannel(ConnectionListener<ChannelHandlerContext> connectionListener,
                             MessageListener<FIXMessage> messageListener) {
        LOGGER.trace("initializing...");
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        NioEventLoopGroup childGroup = new NioEventLoopGroup();
        ServerChannelInitializer channelInitializer = new ServerChannelInitializer(
                this,
                fixMessageFactory,
                new ServerConnectionListener(connectionListener),
                messageListener,
                sessionManager,
                fixLogonManager);
        bootstrap.group(parentGroup, childGroup); // todo revisit threading model
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(channelInitializer);
        ChannelFuture channelFut = bootstrap.bind(localAddress);
        //channelFut.addListener(new SocketChannelListener());
    }

    private class ServerConnectionListener implements ConnectionListener<ChannelHandlerContext> {
        private final ConnectionListener<ChannelHandlerContext> delegate;

        public ServerConnectionListener(ConnectionListener<ChannelHandlerContext> connectionListener) {
            this.delegate = connectionListener;
        }

        @Override
        public void connectionStateChanged(ConnectionState<ChannelHandlerContext> state) {
            if (state.isConnected()) {
                Channel channel = state.source().channel();
                setChannel(channel);
                LOGGER.info("Connected {}", channel);
            } else {
                setChannel(null);
                LOGGER.info("Not connected: {}", state);
            }
            // we need to add the encoder!!! (and decoder??)
            if (delegate != null) {
                delegate.connectionStateChanged(state);
            }
        }
    }
}
