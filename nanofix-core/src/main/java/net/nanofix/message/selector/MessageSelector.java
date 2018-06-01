package net.nanofix.message.selector;

import net.nanofix.message.FIXMessage;

/**
 * User: Mark
 * Date: 31/03/12
 * Time: 11:37
 */
public interface MessageSelector {

    /**
     * Return true when the given message is selected
     * @param msg given FIXMessage
     * @return true when selected, else false
     */
    boolean select(FIXMessage msg);
}
