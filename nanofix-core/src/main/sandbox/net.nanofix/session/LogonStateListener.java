package net.nanofix.session;

import net.nanofix.util.Listener;

/**
 * User: Mark
 * Date: 03/05/12
 * Time: 22:21
 */
public interface LogonStateListener extends Listener {
    void logonStateChanged(Session session, Session.LogonState state);
}
