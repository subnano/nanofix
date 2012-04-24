package net.nanofix.message.selector;

import net.nanofix.message.FIXMessage;

/**
 * User: Mark
 * Date: 31/03/12
 * Time: 11:37
 */
public interface MessageSelector {
    boolean isSelected(FIXMessage msg);
}
