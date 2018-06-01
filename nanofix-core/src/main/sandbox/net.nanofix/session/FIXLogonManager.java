package net.nanofix.session;

import net.nanofix.message.FIXMessage;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 16:21
 */
public interface FIXLogonManager {

    boolean logonValid(FIXMessage msg, Session session);
}
