package net.nanofix.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXMessageDecoder;
import net.nanofix.session.Session;
import net.nanofix.settings.SessionSettings;
import net.nanofix.socket.SocketConnector;
import net.nanofix.socket.SocketListener;
import net.nanofix.socket.SocketState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:11
 */
public class NettySocketConnector implements SocketConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettySocketConnector.class);

//    private int port;
//    private String hostname;
    private final Session session;
    private final SessionSettings settings;
    private final SocketListener listener;
    private final Bootstrap bootstrap;
    private final ConnectFutureListener connectFutureListener;

    private Channel channel;

//    public ClientSocketConnector() {
//        super(application, connectorConfig);
//        this.session = session;
//        this.port = connectorConfig.getPort();
//        this.hostname = connectorConfig.getHostname();
//        session.setConnector(this);
//    }

    public NettySocketConnector(Session session, SessionSettings settings, SocketListener listener) {
        this.session = session;
        this.settings = settings;
        this.listener = listener;
        this.bootstrap = newBootstrapInstance();
        this.connectFutureListener = new ConnectFutureListener();
    }

    @Override
    public String getBindAddress() {
        LOGGER.warn("Not implemented yet!");
        return null;
    }

    @Override
    public void connect() {
        tryConnect();
    }

    @Override
    public void disconnect() {
        channel.disconnect();
    }

    @Override
    public void send(FIXMessage msg) {
        if (msg == null) {
            throw new IllegalArgumentException("nothing to send, msg is null");
        }
        if (channel == null) {
            throw new IllegalArgumentException("nothing to send, msg is null");
        }
        ChannelFuture channelFuture = channel.write(msg);
        channelFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                LOGGER.debug("write future:" + future);
            }
        });
    }

    private Bootstrap newBootstrapInstance() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new FIXFrameDecoder()
                        //new FIXMessageDecoder()
                );
            }
        });
        return bootstrap;
    }

    private void tryConnect() {
        listener.stateUpdated(SocketState.Connecting);
        // TODO enable binding to non-default NIC
        final ChannelFuture channelFut = bootstrap.connect(new InetSocketAddress(settings.getHost(), settings.getPort()));
        channelFut.addListener(connectFutureListener);
    }


    private class ConnectFutureListener implements ChannelFutureListener {
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            LOGGER.info("Connection status (done={},success={})", channelFuture.isDone(), channelFuture.isSuccess());
            NettySocketConnector.this.channel = channelFuture.channel();
            // TODO did it connect/fail?
            //notifyListeners(future.isSuccess());
            SocketState state = channelFuture.isSuccess() ? SocketState.Connected : SocketState.Disconnected;
            // notify with more details - socket? other?
            listener.stateUpdated(state);
        }
    }
}
