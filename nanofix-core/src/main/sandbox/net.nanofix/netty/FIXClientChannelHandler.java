package net.nanofix.netty;

import net.nanofix.app.Application;
import net.nanofix.message.FIXMessage;
import net.nanofix.session.Session;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;

/**
 * User: Mark
 * Date: 02/05/12
 * Time: 16:44
 */
public class FIXClientChannelHandler extends FIXChannelHandler {

    private final Session session;

    public FIXClientChannelHandler(final Application application,
                                   final Session session,
                                   final SocketConnector connector) {
        super(application, connector);
        this.session = session;
        this.setListener(session);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        session.setChannel(ctx.getChannel());
        super.channelConnected(ctx, e);
    }

    @Override
    protected void messageReceived(FIXMessage msg) {
        session.messageReceived(msg);
    }
}