package net.nanofix.session;

import net.nanofix.core.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * User: Mark
 * Date: 01/05/12
 * Time: 21:34
 */
public class NanoSessionManager implements SessionManager {

    private static final Logger LOG = LoggerFactory.getLogger(NanoSessionManager.class);

    private final ConcurrentMap<SessionId, Session> sessions = new ConcurrentHashMap<>();
    private final SessionFactory sessionFactory;

    public NanoSessionManager(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Session getSession(Connector connector, SessionId sessionId) {
        LOG.debug("Locating session for ({})..", sessionId);

        Session session = sessions.get(sessionId);
        if (session == null) {
            session = sessions.putIfAbsent(sessionId, sessionFactory.createSession(sessionId, connector));
            if (session == null) {
                session = sessions.get(sessionId);
            }
        }
        return session;
    }
}
