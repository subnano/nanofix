package net.nanofix.message;

import net.nanofix.session.Session;
import net.nanofix.session.SessionID;
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
    private DateTimeGenerator timeGenerator;
    private final Session session;

    public DefaultFIXMessageFactory() {
        this(null);
    }

    public DefaultFIXMessageFactory(Session session) {
        this.session = session;
        timeGenerator = new DefaultTimeGenerator();
    }

    @Override
    public FIXMessage createMessage() {
        FIXMessage msg = new StandardFIXMessage();
        if (recordTimestamps) {
            msg.setTimestamp(System.nanoTime());
        }
        return msg;
    }

    @Override
    public FIXMessage createMessage(String msgType) {
        FIXMessage msg = createMessage();
        msg.setMsgType(msgType);
        return msg;
    }

    @Override
    public FIXMessage createHeartbeatMessage() {
        return createMessage(MsgTypes.Heartbeat);
    }

    @Override
    public FIXMessage createHeartbeatMessage(String testReqId) {
        FIXMessage msg = createHeartbeatMessage();
        msg.setFieldValue(Tags.TestReqID, testReqId);
        return msg;
    }

    @Override
    public FIXMessage createLogonMessage(boolean resetSeqNum) {
        FIXMessage msg = createMessage(MsgTypes.Logon);
        // BeginString = FIX...
        // BodyLen = 0
        // MsgType
        // Sender/TargetCompID
        // SeqNum
        // SubIDs
        // PosDup/Resend
        // SendingTime
        // app tags

        addHeaderFields(msg);
        msg.setFieldValue(Tags.EncryptMethod, 0);
        msg.setFieldValue(Tags.HeartBtInt, session.getConfig().getHeartbeatInterval());

        // check for reset on logon
        if (resetSeqNum) {
            msg.setFieldValue(Tags.ResetSeqNumFlag, true);
        }
        return msg;
    }

    @Override
    public FIXMessage createLogoutMessage() {
        return createLogoutMessage(null);
    }

    public FIXMessage createLogoutMessage(String text) {
        FIXMessage msg = createMessage(MsgTypes.Logon);
        addHeaderFields(msg);
        // TODO not sure what other fields to add
        if (text != null) {
            msg.setFieldValue(Tags.Text, text);
        }
        return msg;
    }

    @Override
    public FIXMessage createResendRequestMessage(long expectedSeqNum, long receivedSeqNum) {
        FIXMessage msg = createMessage(MsgTypes.ResendRequest);
        addHeaderFields(msg);
        msg.setFieldValue(Tags.BeginSeqNo, expectedSeqNum);
        msg.setFieldValue(Tags.EndSeqNo, receivedSeqNum - 1);
        return msg;
    }

    protected void addHeaderFields(FIXMessage msg) {
        addCounterParties(msg);
        addSeqNum(msg, -1);
        addSessionIdentifiers(msg);
        addSendingTime(msg);
    }

    private void addCounterParties(FIXMessage msg) {
        if (session != null) {
            SessionID sessionID = session.getSessionID();
            msg.setFieldValue(Tags.SenderCompID, sessionID.getSenderCompID());
            msg.setFieldValue(Tags.TargetCompID, sessionID.getTargetCompID());
        }
    }

    private void addSeqNum(FIXMessage msg, int seqNum) {
        msg.setFieldValue(Tags.MsgSeqNum, seqNum);
    }

    private void addSessionIdentifiers(FIXMessage msg) {
    }

    protected void addSendingTime(FIXMessage msg) {
        msg.setFieldValue(Tags.SendingTime, timeGenerator.getUtcTime(isUseMillisInTimeStamp()));
    }

    protected boolean isUseMillisInTimeStamp() {
        // TODO check for FIX version 4.2+ here
        return session != null && session.getConfig().isUseMillisInTimeStamp();
    }


}
