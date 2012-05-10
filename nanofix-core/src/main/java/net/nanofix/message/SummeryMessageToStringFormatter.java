package net.nanofix.message;

/**
 * User: Mark Wardell
 * Date: 18/10/11
 * Time: 10:44
 */
public class SummeryMessageToStringFormatter implements FIXMessageFormatter {

    public String toString(FIXMessage msg) {
        return MsgNames.get(msg.getMsgType()) + "(" + msg.getMsgType() + ")";
    }
}
