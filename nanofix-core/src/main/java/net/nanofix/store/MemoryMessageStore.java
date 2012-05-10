package net.nanofix.store;

import com.google.common.collect.ImmutableList;
import net.nanofix.message.FIXMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * User: Mark
 * Date: 04/05/12
 * Time: 16:24
 */
public class MemoryMessageStore implements MessageStore {
    private static final Logger LOG = LoggerFactory.getLogger(MemoryMessageStore.class);

    private final Map<Integer,FIXMessage> messages;
    private final Properties properties;
    private int seqNumIn;
    private int seqNumOut;

    public MemoryMessageStore() {
        messages = new HashMap<Integer,FIXMessage>();
        properties = new Properties();
        seqNumIn = UNDEFINED;
        seqNumOut = UNDEFINED;
    }

    @Override
    public void setMsgSeqNumIn(int seqNum) {
        this.seqNumIn = seqNum;
    }

    @Override
    public int getMsgSeqNumIn() {
        return seqNumIn;
    }

    @Override
    public void setMsgSeqNumOut(int seqNum) {
        seqNumOut = seqNum;
    }

    @Override
    public int getMsgSeqNumOut() {
        return seqNumOut;
    }

    @Override
    public FIXMessage addMessage(int seqNum, FIXMessage msg) {
        return messages.put(seqNum, msg);
    }

    @Override
    public List<FIXMessage> getMessages(int startSeqNum, int endSeqNum) {
        return ImmutableList.copyOf(messages.values());
    }

    @Override
    public void setProperty(String key, String value) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Value for 'key' cannot be null or empty");
        }
        properties.setProperty(key, value);
    }

    @Override
    public String getProperty(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Value for 'key' cannot be null or empty");
        }
        return properties.getProperty(key);
    }

    @Override
    public void reset() {
        seqNumIn = UNDEFINED;
        seqNumOut = UNDEFINED;
        messages.clear();
        properties.clear();   // should we do this too?
    }
}
