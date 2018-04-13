package net.nanofix.session;

import net.nanofix.config.SessionConfig;
import net.nanofix.message.DefaultFIXMessageFactory;
import net.nanofix.message.FIXMessage;

public class TestMessageFactory extends DefaultFIXMessageFactory {

    private int outboundSeqNum = 0;

    public TestMessageFactory(SessionConfig sessionConfig) {
        super(sessionConfig);
    }

    @Override
    public FIXMessage createMessage(String msgType) {
        FIXMessage msg = super.createMessage(msgType);
        msg.setMsgSeqNum(++outboundSeqNum);
        return msg;
    }
}
