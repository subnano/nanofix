package net.nanofix.app.session;

import net.nanofix.config.ConnectionConfig;
import net.nanofix.config.ServerSocketConfig;
import net.nanofix.config.SessionConfig;

import java.util.List;

/**
 * User: Mark
 * Date: 20/04/12
 * Time: 18:38
 */
public class DefaultSessionFactory implements SessionFactory {

    @Override
    public Session createSession(SessionConfig sessionConfig) {
        if (sessionConfig == null) {
            throw new IllegalArgumentException("SessionConfig is null");
        }
        List<ConnectionConfig> connectors = sessionConfig.getConnectors();
        return connectors.get(0) instanceof ServerSocketConfig
                ? new ServerSession(sessionConfig)
                : new ClientSession(sessionConfig);
    }
}
