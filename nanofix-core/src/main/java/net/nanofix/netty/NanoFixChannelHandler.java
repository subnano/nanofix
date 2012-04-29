package net.nanofix.netty;

import net.nanofix.session.ConnectorListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: Mark
 * Date: 27/04/12
 * Time: 07:10
 */
public class NanoFixChannelHandler extends SimpleChannelHandler {

    private final Logger LOG;
    private final SocketConnector connector;
    private final ConnectorListener listener;
    private final AtomicBoolean active = new AtomicBoolean(false);

    public NanoFixChannelHandler(final SocketConnector connector,
                                 final ConnectorListener listener) {
        this.connector = connector;
        this.listener = listener;
        LOG = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void channelOpen(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        LOG.debug("channelOpen");
        super.channelOpen(ctx, e);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        InetSocketAddress address = (InetSocketAddress) e.getValue();
        LOG.debug("channelConnected from " + address.toString());
        super.channelConnected(ctx, e);
        if (!isActive()) {
            setActive(true);
            if (listener != null) {
                listener.onConnectorStatus(connector, true);
            }
        }
    }

    @Override
    public void channelDisconnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        LOG.debug("channelDisconnected");
        super.channelDisconnected(ctx, e);
        if (isActive()) {
            setActive(false);
            if (listener != null) {
                listener.onConnectorStatus(connector, false);
            }
        }
    }

    @Override
    public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        LOG.debug("channelInterestChanged: " + e.toString());
        super.channelInterestChanged(ctx, e);
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception
    {
        synchronized (this) {
            if (!isActive()) {
                return;
            }
            LOG.warn("exceptionCaught ({}): {}", isActive() ? "active" : "inactive", e.toString());

            // We don't want to log this - since it is normal for this to happen during failover/reconnect
            // and we don't want to spew out stack traces in that event
            // The user has access to this exception anyway via the application exception initial cause

//            HornetQException me = new HornetQException(HornetQException.INTERNAL_ERROR, "Netty exception");
//            me.initCause(e.getCause());
//            try
//            {
//                listener.connectionException(e.getChannel().getId(), me);
//                active = false;
//            }
//            catch (Exception ex)
//            {
//                HornetQChannelHandler.log.error("failed to notify the listener:", ex);
//            }
        }
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean value) {
        active.set(value);
    }
}
