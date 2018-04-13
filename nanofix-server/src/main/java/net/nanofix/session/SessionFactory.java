package net.nanofix.session;

import net.nanofix.core.connector.Connector;

/**
 * User: Mark
 * Date: 20/04/12
 * Time: 18:37
 */
public interface SessionFactory {
    Session createSession(SessionId sessionId, Connector connector);
}
