package net.nanofix.session;

import net.nanofix.config.SessionConfig;
import net.nanofix.netty.SocketConnector;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:37
 */
public class ServerSession extends AbstractSession implements ConnectorListener {

    public ServerSession(SessionConfig config) {
        super(config);
    }

    @Override
    public void onConnectorStatus(SocketConnector connector, boolean success) {
        if (success) {
            LOG.info("session {} is connected", "?");
        }
        else {
            LOG.warn("session {} is disconnected", "?");
        }
    }
}
