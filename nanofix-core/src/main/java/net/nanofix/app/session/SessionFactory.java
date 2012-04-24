package net.nanofix.app.session;

import net.nanofix.config.SessionConfig;

/**
 * User: Mark
 * Date: 20/04/12
 * Time: 18:37
 */
public interface SessionFactory {
    Session createSession(SessionConfig sessionConfig);
}
