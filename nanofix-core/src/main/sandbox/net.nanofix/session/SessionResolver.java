package net.nanofix.session;

import net.nanofix.app.Application;
import net.nanofix.config.SessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mark
 * Date: 01/05/12
 * Time: 21:34
 */
public class SessionResolver {

    private static final Logger LOG = LoggerFactory.getLogger(SessionResolver.class);

    private final Application application;

    public SessionResolver(Application application) {
        this.application = application;
    }

    public Session resolve(SessionID sessionID) {
        LOG.debug("resolving session for SessionID({})..", sessionID);

        // first we need to reverse the compIds
        SessionID reverseSessionID = sessionID.reverse();

        for (Session session : application.getSessions()) {
            if (matches(session, reverseSessionID))
                return session;
        }
        return null;
    }

    private boolean matches(Session session, SessionID sessionID) {
        return session.getSessionID().equals(sessionID);
    }
}
