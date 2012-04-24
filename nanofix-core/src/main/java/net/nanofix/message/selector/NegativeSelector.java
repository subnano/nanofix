package net.nanofix.message.selector;

import net.nanofix.message.FIXMessage;

/**
 * Used to negate the result of the underlying selector.
 * For example, the underlying selector would return all messages matching a selection criteria, so this selector would
 * return all messages that don't match the selection criteria.
 */
public class NegativeSelector implements MessageSelector {

    private final MessageSelector selector;

    public NegativeSelector(MessageSelector selector) {
        if (selector == null) {
            throw new IllegalArgumentException("Argument 'selector' is null");
        }
        this.selector = selector;
    }

    @Override
    public boolean isSelected(FIXMessage msg) {
        return !selector.isSelected(msg);
    }
}
