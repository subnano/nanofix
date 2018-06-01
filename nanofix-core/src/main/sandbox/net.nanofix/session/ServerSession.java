package net.nanofix.session;

import net.nanofix.config.SessionConfig;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.MsgTypes;
import net.nanofix.message.Tags;
import net.nanofix.util.FIXMessageUtil;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:37
 */
public class ServerSession extends AbstractSession {

    public ServerSession(SessionConfig config) {
        super(config);
    }

    @Override
    public void connectionStatus(ChannelHandler handler, boolean state) {
        if (state) {
            LOG.info("session {} is connected", getSessionID());
        }
        else {
            LOG.warn("session {} is disconnected", getSessionID());
            setLogonState(LogonState.Disconnected);
        }
    }

    /**
     * Start the session connecting it with the given channel
     * @param channel
     */
    public void start(Channel channel) {
        setChannel(channel);
        start();
    }

    @Override
    public void messageReceived(FIXMessage msg) {
        if (msg.getMsgType().equals(MsgTypes.Logon)) {
            processLogonMessage(msg);
        }
        else {
            super.messageReceived(msg);
        }
    }

    private void processLogonMessage(FIXMessage msg) {

        setLogonState(LogonState.LogonReceived);

        // do we need to reset?
        boolean resetSeqNum = config.isResetSeqNum()
                || FIXMessageUtil.getBooleanFieldValue(msg, Tags.ResetSeqNumFlag, false);

        // create and send the message
        send(fixMessageFactory.createLogonMessage(resetSeqNum));

        // update the state
        setLogonState(LogonState.LogonSent);

        // since we are the server we assume logon process is complete
        setLogonState(LogonState.LogonComplete);
        LOG.info("logon complete for {}", getSessionID());
    }
}
