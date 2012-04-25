package net.nanofix.message;

/**
 * Created by IntelliJ IDEA.
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 08:21
 */
public interface FIXMessageFactory {
    FIXMessage createMessage();
    FIXMessage createMessage(String msgType);
}
