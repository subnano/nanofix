package net.nanofix.message;

import net.nanofix.settings.SessionSettings;
import net.nanofix.util.ByteString;
import net.nanofix.util.DateTimeGenerator;
import net.nanofix.util.DefaultTimeGenerator;

/**
 * Created by IntelliJ IDEA.
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 08:21
 */
public class DefaultFIXMessageFactory implements FIXMessageFactory {

    private static final boolean recordTimestamps = true;

    private final SessionSettings settings;
    private final boolean resetSeqNumOnLogon;
    private final MessageKey messageKey;
    private DateTimeGenerator timeGenerator;

    public DefaultFIXMessageFactory(SessionSettings settings) {
        this.settings = settings;
        // TODO time generator created by a factory based on settings
        this.messageKey = new MessageKey();
        this.timeGenerator = new DefaultTimeGenerator();
        this.resetSeqNumOnLogon = settings.isResetSeqNumOnLogon();
    }

    @Override
    public FIXMessage createMessage() {
        FIXMessage msg = new NanoFIXMessage(null, null);
        if (recordTimestamps) {
            //msg.header().setTimestamp(System.nanoTime());
        }
        return msg;
    }

    @Override
    public FIXMessage createMessage(MsgType msgType) {
        FIXMessage msg = createMessage();
        //msg.setMsgType(msgType);
        return msg;
    }

    @Override
    public FIXMessage createLogonMessage() {
        FIXMessage message = createMessage();
        //message.setMessageKey();
        FIXMessage msg = createMessage();
        message.header().msgType(MsgTypes.Logon);
        message.addIntField(Tags.EncryptMethod, 0);
        message.addIntField(Tags.HeartBtInt, settings.getHeartbeatInterval());

        // check for reset on logon
        if (resetSeqNumOnLogon) {
            message.addBooleanField(Tags.ResetSeqNumFlag, true);
        }
        return msg;
    }

    @Override
    public FIXMessage createHeartbeatMessage() {
        return createMessage(MsgTypes.Heartbeat);
    }

    @Override
    public FIXMessage createHeartbeatMessage(ByteString testReqId) {
        FIXMessage msg = createHeartbeatMessage();
        msg.addStringField(Tags.TestReqID, testReqId);
        return msg;
    }

    @Override
    public FIXMessage createLogoutMessage() {
        return createLogoutMessage(null);
    }

    public FIXMessage createLogoutMessage(ByteString text) {
        FIXMessage msg = createMessage(MsgTypes.Logon);
        if (text != null) {
            msg.addStringField(Tags.Text, text);
        }
        return msg;
    }

    @Override
    public FIXMessage createResendRequestMessage(long expectedSeqNum, long receivedSeqNum) {
        FIXMessage msg = createMessage(MsgTypes.ResendRequest);
        msg.addLongField(Tags.BeginSeqNo, expectedSeqNum);
        msg.addLongField(Tags.EndSeqNo, receivedSeqNum - 1);
        return msg;
    }

}
