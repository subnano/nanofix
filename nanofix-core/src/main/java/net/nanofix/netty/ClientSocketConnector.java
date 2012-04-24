package net.nanofix.netty;

import net.nanofix.app.AbstractComponent;
import net.nanofix.config.ClientSocketConfig;
import net.nanofix.config.ConnectionConfig;
import net.nanofix.message.FIXMessage;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:11
 */
public class ClientSocketConnector extends AbstractComponent implements SocketConnector {

    private int port;
    private String bindAddress;
    private String hostname;
    private Channel channel;

    public ClientSocketConnector(ClientSocketConfig connectorConfig) {
        this.port = connectorConfig.getPort();
        this.bindAddress = connectorConfig.getBindAddress();
        if (bindAddress == null || bindAddress.isEmpty()) {
            bindAddress = "localhost";
        }
        this.hostname = connectorConfig.getHostname();
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
                channel = future.getChannel();
                sendLogonMessage();
            }
        });
    }

    private void sendLogonMessage() {
        LOG.info("sending Logon message..");
        ChannelFuture channelFuture = channel.write(createLogonMessage());
        channelFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                LOG.info("Write future:" + future);
            }
        });

    }

    private FIXMessage createLogonMessage() {
        msg = fixMessageFactory.???
        return null;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getBindAddress() {
        return bindAddress;
    }

    public String getHostname() {
        return hostname;
    }
}
