package net.nanofix.message.selector;

import net.nanofix.message.FIXMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Mark
 * Date: 31/03/12
 * Time: 11:38
 */
public class MsgTypeSelector implements MessageSelector {

    private final List<String> msgTypes = new ArrayList<String>();

    public MsgTypeSelector(String msgType) {
        add(msgType);
    }

    public MsgTypeSelector(List<String> msgTypes) {
        if (msgTypes == null)
            throw new IllegalArgumentException("Argument 'msgTypes' is null");
        if (msgTypes.isEmpty())
            throw new IllegalArgumentException("Argument 'msgTypes' is empty");

        for (String msgType : msgTypes) {
            add(msgType);
        }
    }

    private void add(String msgType) {
        if (msgType == null) {
            throw new IllegalArgumentException("Argument 'msgType' is null");
        }
        if (msgType.isEmpty()) {
            throw new IllegalArgumentException("Argument 'msgType' is an empty string");
        }
        if (msgTypes.contains(msgType)) {
            throw new IllegalArgumentException("Duplicate msgType specified '" + msgType + "'");
        }
        msgTypes.add(msgType);
    }

    @Override
    public boolean isSelected(FIXMessage msg) {
        return msgTypes.contains(msg.getMsgType());
    }
}
