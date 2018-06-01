package net.nanofix.session;

import net.nanofix.message.FIXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mark
 * Date: 03/05/12
 * Time: 18:40
 */
public class LoggingListener implements MessageListener, LogonStateListener {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingListener.class);

    @Override
    public void messageReceived(FIXMessage msg) {
        LOG.info("message received {}", msg.toString());
    }

    @Override
    public void logonStateChanged(Session session, Session.LogonState state) {
        LOG.info("logon state changed Session={} State={}", session.getSessionID(), state);
    }
}
