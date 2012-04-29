package net.nanofix.netty;

import net.nanofix.app.AbstractComponent;
import net.nanofix.message.FIXMessage;
import net.nanofix.session.ConnectorListener;
import net.nanofix.session.Session;
import net.nanofix.config.ConnectionConfig;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: Mark
 * Date: 25/04/12
 * Time: 06:28
 */
public class AbstractSocketConnector extends AbstractComponent implements SocketConnector {

    private String bindAddress;
    private Session session;
    private Channel channel;
    private final List<ConnectorListener> connectorListeners = new CopyOnWriteArrayList<ConnectorListener>();

    public AbstractSocketConnector(ConnectionConfig connectorConfig) {
        this.bindAddress = connectorConfig.getBindAddress();
        if (bindAddress == null || bindAddress.isEmpty()) {
            bindAddress = "localhost";
        }
    }

    @Override
    public String getBindAddress() {
        return bindAddress;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public Session getSession() {
        return session;
    }

    protected void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void addListener(ConnectorListener listener) {
        synchronized (connectorListeners) {
            if (!connectorListeners.contains(listener)) {
                connectorListeners.add(listener);
            }
        }
    }

    @Override
    public void removeListener(ConnectorListener listener) {
        synchronized (connectorListeners) {
            connectorListeners.remove(listener);
        }
    }

    protected void notifyListeners(boolean success) {
        for (ConnectorListener listener : connectorListeners) {
            listener.onConnectorStatus(this, success);
        }
    }

    @Override
    public void send(FIXMessage msg) {
        if (msg == null) {
            throw new IllegalArgumentException("nothing to send, msg is null");
        }
        ChannelFuture channelFuture = channel.write(msg);
        channelFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                LOG.info("write future:" + future);
            }
        });
    }
}
