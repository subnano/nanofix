package net.nanofix.message;

/**
 * User: Mark Wardell
 * Date: 18/10/11
 * Time: 10:44
 */
public class MessageToStringFormatter implements FIXMessageFormatter {

    public String toString(FIXMessage message) {
        return new String(message.getRawBytes());
    }
}
