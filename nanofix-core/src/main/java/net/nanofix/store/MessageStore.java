package net.nanofix.store;

import net.nanofix.message.FIXMessage;

import java.util.Collection;
import java.util.List;

/**
 * User: Mark
 * Date: 04/05/12
 * Time: 16:24
 */
public interface MessageStore {

    public static final int UNDEFINED = -1;

    void setMsgSeqNumIn(int seqNum);

    int getMsgSeqNumIn();

    void setMsgSeqNumOut(int seqNum);

    int getMsgSeqNumOut();

    FIXMessage addMessage(int seqNum, FIXMessage msg);

    List<FIXMessage> getMessages(int startSeqNum, int endSeqNum);

    void setProperty(String key, String value);

    String getProperty(String key);

    void reset();

}
