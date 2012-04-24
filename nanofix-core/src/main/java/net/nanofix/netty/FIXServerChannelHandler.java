package net.nanofix.netty;

import net.nanofix.message.FIXMessage;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mark
 * Date: 02/04/12
 * Time: 16:40
 */
public class FIXServerChannelHandler extends SimpleChannelUpstreamHandler {
    private final Logger LOG;

    public FIXServerChannelHandler() {
        LOG = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {

        // convert to a FIXMessage first
        if (!(e.getMessage() instanceof FIXMessage)) {
            throw new ChannelException("Message type (" + e.getMessage() + ") not  supported");
        }
        FIXMessage message = (FIXMessage) e.getMessage();
        onMessage(message);

    }

    public void onMessage(FIXMessage message) {
    }

}