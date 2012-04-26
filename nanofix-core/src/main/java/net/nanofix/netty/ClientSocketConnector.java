package net.nanofix.netty;

import net.nanofix.message.MsgTypes;
import net.nanofix.message.Tags;
import net.nanofix.session.Session;
import net.nanofix.config.ClientSocketConfig;
import net.nanofix.message.FIXMessage;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:11
 */
public class ClientSocketConnector extends AbstractSocketConnector implements SocketConnector {

    private int port;
    private String hostname;
    private final Session session;

    public ClientSocketConnector(ClientSocketConfig connectorConfig, Session session) {
        super(connectorConfig);
        this.session = session;
        this.port = connectorConfig.getPort();
        this.hostname = connectorConfig.getHostname();
        session.setConnector(this);
    }

    @Override
    public void open() {
        super.open();
    }

    @Override
    public void start() {
        super.start();
        final NioClientSocketChannelFactory cf = new NioClientSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        final ClientBootstrap client = new ClientBootstrap(cf);
        client.setPipelineFactory(new FIXClientPipelineFactory());

        final ChannelFuture channelFut = client.connect(new InetSocketAddress(getHostname(), getPort()));
        channelFut.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                LOG.info("Connection status:" + future);
                // TODO did it connect/fail?
                setChannel(future.getChannel());
                session.start();
            }
        });
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

}
