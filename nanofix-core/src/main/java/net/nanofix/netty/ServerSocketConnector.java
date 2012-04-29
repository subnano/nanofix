package net.nanofix.netty;

import net.nanofix.config.ServerSocketConfig;
import net.nanofix.session.ConnectorListener;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.ServerSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:11
 */
public class ServerSocketConnector extends AbstractSocketConnector implements SocketConnector {

    private int port;

    public ServerSocketConnector(ServerSocketConfig connectorConfig) {
        super(connectorConfig);
        this.port = connectorConfig.getPort();
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
        FIXServerPipelineFactory pipelineFactory = new FIXServerPipelineFactory();
        try {
            pipelineFactory.getPipeline().addFirst("handler", new NanoFixChannelHandler(this, new ConnectorListener() {
                @Override
                public void onConnectorStatus(SocketConnector connector, boolean success) {
                }
            }));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        final ServerBootstrap server = new ServerBootstrap(cf);
//        server.setPipelineFactory(pipelineFactory);
          server.setPipeline(Channels.pipeline(
                  new NanoFixChannelHandler(this, new ConnectorListener() {
                      @Override
                      public void onConnectorStatus(SocketConnector connector, boolean success) {
                      }
                  })
          ));

        LOG.info("binding to {} port {}", getBindAddress(), getPort());
        server.bind(new InetSocketAddress(getBindAddress(), getPort()));
    }

    public int getPort() {
        return port;
    }

}
