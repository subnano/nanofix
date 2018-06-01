package net.nanofix.netty;

import net.nanofix.app.AbstractComponent;
import net.nanofix.app.Application;
import net.nanofix.message.FIXMessage;
import net.nanofix.session.Session;
import net.nanofix.config.ConnectionConfig;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * User: Mark
 * Date: 25/04/12
 * Time: 06:28
 */
public class AbstractSocketConnector extends AbstractComponent implements SocketConnector {

    private String bindAddress;
    private Session session;
    private Channel channel;
    protected final Application application;

    public AbstractSocketConnector(Application application, ConnectionConfig connectorConfig) {
        this.application = application;
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
