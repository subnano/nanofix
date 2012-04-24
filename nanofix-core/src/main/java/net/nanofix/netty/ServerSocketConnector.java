package net.nanofix.netty;

import net.nanofix.app.AbstractComponent;
import net.nanofix.config.ConnectionConfig;
import net.nanofix.config.ServerSocketConfig;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:11
 */
public class ServerSocketConnector extends AbstractComponent implements SocketConnector {

    private int port;
    private String bindAddress;

    public ServerSocketConnector(ServerSocketConfig connectorConfig) {
        this.port = connectorConfig.getPort();
        this.bindAddress = connectorConfig.getBindAddress();
        if (bindAddress == null || bindAddress.isEmpty()) {
            bindAddress = "localhost";
        }
    }

    @Override
    public void open() {
        super.open();
    }

    @Override
    public void start() {
        super.start();
        final ServerSocketChannelFactory cf = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        final ServerBootstrap server = new ServerBootstrap(cf);
        server.setPipelineFactory(new FIXServerPipelineFactory());

        LOG.info("binding to {} port {}", bindAddress, port);
        server.bind(new InetSocketAddress(bindAddress, port));
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getBindAddress() {
        return bindAddress;
    }
}
