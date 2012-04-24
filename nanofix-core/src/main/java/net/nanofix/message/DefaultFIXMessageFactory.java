package net.nanofix.message;

/**
 * Created by IntelliJ IDEA.
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 08:21
 */
public class DefaultFIXMessageFactory implements FIXMessageFactory {

    private static final boolean recordTimestamps = true;

    public FIXMessage createMessage(String msgType) {
        FIXMessage msg = new StandardFIXMessage();
        msg.setMsgType(msgType);
        return msg;
    }

    public FIXMessage createMessage() {
        FIXMessage msg = new StandardFIXMessage();
        if (recordTimestamps) {
            msg.setTimestamp(System.nanoTime());
        }
        return msg; 
    }
}
