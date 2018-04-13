package net.nanofix.session;

import net.nanofix.message.FIXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simply allows anyone to logon
 * User: Mark
 * Date: 24/04/12
 * Time: 16:22
 */
public class AnonymousFIXLogonManager implements FIXLogonManager {

    private final Logger LOG = LoggerFactory.getLogger(AnonymousFIXLogonManager.class);

    @Override
    public boolean logonValid(FIXMessage msg, Session session) {
        return true;
    }
}
