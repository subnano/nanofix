package net.nanofix.netty;

import net.nanofix.app.AbstractComponent;
import net.nanofix.session.Session;
import net.nanofix.config.ConnectionConfig;

/**
 * User: Mark
 * Date: 25/04/12
 * Time: 06:28
 */
public class AbstractSocketConnector extends AbstractComponent implements SocketConnector {

    private String bindAddress;
    private Session session;

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

}
