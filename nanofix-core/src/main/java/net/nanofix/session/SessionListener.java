package net.nanofix.session;

import net.nanofix.message.FIXMessage;

public interface SessionListener {

    void stateUpdated(Session session, SessionState state);

    void messageReceived(Session session, FIXMessage message);

}
