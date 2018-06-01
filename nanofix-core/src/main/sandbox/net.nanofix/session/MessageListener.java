package net.nanofix.session;

import net.nanofix.message.FIXMessage;
import net.nanofix.util.Listener;

/**
 * User: Mark
 * Date: 03/05/12
 * Time: 18:28
 */
public interface MessageListener extends Listener {
    void messageReceived(FIXMessage msg);
}
