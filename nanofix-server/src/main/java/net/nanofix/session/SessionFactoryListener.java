package net.nanofix.session;

import net.nanofix.util.Listener;

/**
 * User: Mark
 * Date: 02/05/12
 * Time: 07:33
 */
public interface SessionFactoryListener extends Listener {
    void sessionCreated(Object source, Session session);
    void sessionDestroyed(Object source, Session session);
}
