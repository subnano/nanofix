package net.nanofix.session;

import net.nanofix.message.FIXMessage;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 16:22
 */
public class DefaultFIXLogonManager extends SimpleChannelHandler implements FIXLogonManager {

    private final Logger LOG = LoggerFactory.getLogger(DefaultFIXLogonManager.class);

    @Override
    public boolean isLogonPermitted(FIXMessage logonMessage) {
        LOG.info("isLogonPermitted: logonMessage=" + logonMessage.toString());
        return true;
    }
}
