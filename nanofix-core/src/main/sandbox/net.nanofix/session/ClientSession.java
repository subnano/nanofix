package net.nanofix.session;

import net.nanofix.app.NanofixRuntimeException;
import net.nanofix.config.SessionConfig;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.MsgTypes;
import net.nanofix.message.Tags;
import net.nanofix.util.FIXMessageUtil;
import org.jboss.netty.channel.ChannelHandler;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:37
 */
public class ClientSession extends AbstractSession {

    private boolean resendRequestSent;

    public ClientSession(SessionConfig config) {
        super(config);
    }

    @Override
    public void open() {
        super.open();
        // make sure we have a connector
        if (connector == null) {
            throw new NanofixRuntimeException("Session connector is null");
        }
        connector.open();
    }

    @Override
    public void start() {
        super.start();
        connector.start();
    }

    @Override
    public void stop() {
        super.stop();
        connector.stop();
    }

    @Override
    public void close() {
        super.close();
        connector.close();
    }

    @Override
    public void connectionStatus(ChannelHandler handler, boolean state) {
        if (state) {
            LOG.info("session {} is connected", getSessionID());
            sendLogonMessage();
        }
        else {
            LOG.warn("session {} is disconnected", getSessionID());
            setLogonState(LogonState.Disconnected);

            // stop heartbeat timer
            heartbeatManager.stop(this);
        }

        // let super notify listeners
        super.connectionStatus(handler, state);
    }

    private void sendLogonMessage() {
        LOG.info("sending Logon message..");

        // check for seq reset
        boolean resetSeqNum = config.isResetSeqNum();
        if (resetSeqNum) {
            setSeqNumOut(0);
        }

        send(fixMessageFactory.createLogonMessage(resetSeqNum));

        setLogonState(LogonState.LogonSent);
    }

    @Override
    public void messageReceived(FIXMessage msg) {
        if (FIXMessageUtil.isAdminMessage(msg.getMsgType())) {
            processAdminMessage(msg);
        }
        else if (logonState == LogonState.LogonComplete) {
            super.messageReceived(msg);
        }
        else {
            throw new FatalProtocolException("Logon message expected");
        }
    }

    private void processAdminMessage(FIXMessage msg) {
        try {
            if (msg.getMsgType().equals(MsgTypes.Logon)) {
                processLogonMessage(msg);
            }

            else {

                // we shouldn't process anything else until a Logon
                if (logonState != LogonState.LogonComplete) {
                    throw new FatalProtocolException("Logon message expected");
                }
                else if (msg.getMsgType().equals(MsgTypes.TestRequest)) {
                    String testReqId = msg.getStringFieldValue(Tags.TestReqID);
                    send(fixMessageFactory.createHeartbeatMessage(testReqId));
                }
                else if (msg.getMsgType().equals(MsgTypes.ResendRequest)) {
                }
                else if (msg.getMsgType().equals(MsgTypes.Reject)) {
                }
                else if (msg.getMsgType().equals(MsgTypes.SequenceReset)) {
                }
                else if (msg.getMsgType().equals(MsgTypes.Logout)) {
                }
            }
        } catch (Exception e) {
            LOG.warn("Exception caught processing message", e);
            sendLogoutAndDisconnect();
        } catch (FIXMessageException e) {
            LOG.warn("Exception caught processing message", e);
            sendLogoutAndDisconnect();
        }
    }

    private void sendLogoutAndDisconnect() {
        LOG.info("sending Logout message..");
        send(fixMessageFactory.createLogoutMessage());
        setLogonState(LogonState.LogoutSent);
        // TODO wait for response then disconnect
    }

    private void processLogonMessage(FIXMessage msg) throws FIXMessageException {

        // check if Logon received before one is sent
        if (logonState == LogonState.None) {
            throw new FatalProtocolException("Logon response received before our Logon sent");
        }
        else if (logonState == LogonState.LogonSent) {
            // TODO validate the logon response message

            // make sure it is valid
            long seqNum = msg.getMsgSeqNum();
            if (seqNum <= 0) {
                throw new FIXMessageException("SeqNum (" + seqNum + ") not a positive integer");
            }

            // must be at least what we expect
            if (seqNum < expectedSeqNumIn()) {
                throw new FIXMessageException("SeqNum too low, expected " + expectedSeqNumIn());
            }

            // too high? send a ResendRequest
            if (seqNum > expectedSeqNumIn()) {
                sendResendRequest(expectedSeqNumIn(), seqNum);
            }

            // start heartbeat timer
            heartbeatManager.start(this);

            // update the logon state
            LOG.info("logon complete for {}", getSessionID());
            setLogonState(LogonState.LogonComplete);
        }
    }

    protected void sendResendRequest(long expectedSeqNum, long receivedSeqNum) {
        LOG.warn("expected SeqNum {} but received {}", expectedSeqNum, receivedSeqNum);
        resendRequestSent = true;
        FIXMessage msg = fixMessageFactory.createResendRequestMessage(expectedSeqNum, receivedSeqNum);
        send(msg);
    }

}
