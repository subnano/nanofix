package net.nanofix.message;

import net.nanofix.util.ByteString;

/**
 * Created by IntelliJ IDEA.
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 08:21
 */
public interface FIXMessageFactory {
    FIXMessage createMessage();

    FIXMessage createMessage(MsgType msgType);

    FIXMessage createLogonMessage();

    FIXMessage createHeartbeatMessage();

    FIXMessage createHeartbeatMessage(ByteString testReqId);

    FIXMessage createLogoutMessage();

    FIXMessage createResendRequestMessage(long expectedSeqNum, long receivedSeqNum);

}
