package net.nanofix.session;

import net.nanofix.config.SessionConfig;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.MsgTypes;
import net.nanofix.message.Tags;
import net.nanofix.netty.SocketConnector;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:37
 */
public class ClientSession extends AbstractSession {

    public ClientSession(SessionConfig config) {
        super(config);
    }

    @Override
    public void open() {
        super.open();
        getConnector().addListener(this);
        getConnector().open();
    }

    @Override
    public void start() {
        super.start();
        getConnector().start();
    }

    private void sendLogonMessage() {
        LOG.info("sending Logon message..");
        send(createLogonMessage());
    }

    @Override
    public void stop() {
        getConnector().stop();
        super.stop();
    }

    @Override
    public void close() {
        getConnector().removeListener(this);
        super.close();
    }

    private FIXMessage createLogonMessage() {
        FIXMessage msg = getFIXMessageFactory().createMessage(MsgTypes.Logon);
        // 8=FIX.4.09=6135=A34=149=BANZAI52=20120331-10:25:1556=EXEC98=0108=3010=255
        // BeginString = FIX...
        // BodyLen = 0
        // MsgType
        // Sender/TargetCompID
        // SeqNum
        // SubIDs
        // PosDup/Resend
        // SendingTime
        // app tags
        addHeader(msg);
        msg.setFieldValue(Tags.EncryptMethod, 0);
        msg.setFieldValue(Tags.HeartBtInt, getConfig().getHeartbeatInterval());

        // check for reset on logon
        if (getConfig().isResetSeqNum()) {
            msg.setFieldValue(Tags.ResetSeqNumFlag, true);
        }
        return msg;
    }

    @Override
    public void onConnectorStatus(SocketConnector connector, boolean success) {
        if (success) {
            LOG.info("session {} is connected", "?");
            sendLogonMessage();
        }
        else {
            LOG.warn("session {} is disconnected", "?");
        }
    }
}
