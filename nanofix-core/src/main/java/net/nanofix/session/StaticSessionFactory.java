package net.nanofix.session;

import net.nanofix.config.ConnectionConfig;
import net.nanofix.config.ServerSocketConfig;
import net.nanofix.config.SessionConfig;
import net.nanofix.util.Listener;
import net.nanofix.util.ListenerSupport;

import java.util.List;

/**
 * User: Mark
 * Date: 20/04/12
 * Time: 18:38
 */
public class StaticSessionFactory extends ListenerSupport<SessionFactoryListener> implements SessionFactory {


    @Override
    public Session createSession(SessionConfig sessionConfig) {
        if (sessionConfig == null) {
            throw new IllegalArgumentException("SessionConfig is null");
        }
        List<ConnectionConfig> connectors = sessionConfig.getConnectors();
        Session session = connectors.get(0) instanceof ServerSocketConfig
                ? new ServerSession(sessionConfig)
                : new ClientSession(sessionConfig);

        // notify listeners
        for (SessionFactoryListener listener : getListeners()) {
            listener.sessionCreated(this, session);
        }

        return session;
    }

    // TODO add support to create from CompIDs / SessionIDs

    // TODO add support for destroySession
}
