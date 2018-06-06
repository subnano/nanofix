package net.nanofix.netty;

import net.nanofix.app.Application;
import net.nanofix.message.FIXMessage;
import net.nanofix.session.ConnectionListener;
import net.nanofix.session.FatalProtocolException;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: Mark
 * Date: 27/04/12
 * Time: 07:10
 */
public abstract class FIXChannelHandler extends SimpleChannelHandler {

    protected final Logger LOG;
    protected final Application application;
    protected final SocketConnector connector;
    protected ConnectionListener listener;

    private final AtomicBoolean active = new AtomicBoolean(false);

    protected Channel channel;

    public FIXChannelHandler(final Application application,
                             final SocketConnector connector) {
        this.application = application;
        this.connector = connector;
        LOG = LoggerFactory.getLogger(this.getClass());
    }

    public ConnectionListener getListener() {
        return listener;
    }

    public void setListener(ConnectionListener listener) {
        this.listener = listener;
    }

    @Override
    public void channelOpen(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        LOG.debug("channelOpen");
        this.channel = ctx.getChannel();
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
                listener.connectionStatus(this, true);
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
                listener.connectionStatus(this, false);
            }
        }
    }

    @Override
    public void channelInterestChanged(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        LOG.debug("channelInterestChanged: " + e.toString());
        super.channelInterestChanged(ctx, e);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

        // convert to a FIXMessage first
        if (!(e.getMessage() instanceof FIXMessage)) {
            throw new ChannelException("Message type (" + e.getMessage() + ") not  supported");
        }
        messageReceived((FIXMessage) e.getMessage());
    }

    protected abstract void messageReceived(FIXMessage msg);

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final ExceptionEvent e) throws Exception
    {
        if (!isActive()) {
            return;
        }
        if (e.getCause() instanceof FatalProtocolException) {
            LOG.error("{}, initiating disconnect", e.toString(), e.getCause());
            disconnect();
        }
        LOG.warn("exceptionCaught (" + (isActive() ? "active" : "inactive") + ") - " + e.toString(), e.getCause());

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

//    @Override
//    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
//        LOGGER.info("messageReceived: {}", e.getMessage());
//        super.messageReceived(ctx, e);
//    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean value) {
        active.set(value);
    }

    /**
     * A method that can be called in the handler to disconnect the channel in certain conditions
     */
    public void disconnect() {

        // make sure we have a channel that needs disconnecting
        if ((this.channel == null) || !this.channel.isConnected()) {
            return;
        }

        // TODO disconnect here but need to know session stuff
        LOG.warn("disconnecting..");

        // flush all pending writes before requesting close
        // beware, it's still possible to get hit with ChannelClosedException
        this.channel.write(ChannelBuffers.EMPTY_BUFFER)
                .awaitUninterruptibly()
                .getChannel().close()
                .awaitUninterruptibly();
    }

}
