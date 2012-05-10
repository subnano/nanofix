package net.nanofix.store;

import net.nanofix.message.FIXMessage;
import net.nanofix.message.MsgTypes;
import net.nanofix.message.StandardFIXMessage;
import net.nanofix.message.Tags;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.*;

/**
 * User: Mark
 * Date: 05/05/12
 * Time: 09:12
 */
public class MemoryMessageStoreTest {
    private static final Logger LOG = LoggerFactory.getLogger(MemoryMessageStoreTest.class);

    private static final String TEST_KEY = "test-key";
    private static final String TEST_VALUE = "test-value";

    private MessageStore store;

    @Before
    public void setup() {
        store = new MemoryMessageStore();
    }

    @Test
    public void testMsgSeqNumIn() throws Exception {
        assertEquals("MsgSeqNumIn", MessageStore.UNDEFINED, store.getMsgSeqNumIn());
        store.setMsgSeqNumIn(123);
        assertEquals("MsgSeqNumIn", 123, store.getMsgSeqNumIn());
    }

    @Test
    public void testMsgSeqNumOut() throws Exception {
        assertEquals("MsgSeqNumOut", MessageStore.UNDEFINED, store.getMsgSeqNumOut());
        store.setMsgSeqNumOut(789);
        assertEquals("MsgSeqNumOut", 789, store.getMsgSeqNumOut());
    }

    @Test
    public void testAddMessage() throws Exception {
        FIXMessage msg = createTestMessage("account1");
        assertNull("Message", store.addMessage(3, msg));
        assertEquals("Message", msg, store.addMessage(3, msg));
    }

    @Test
    public void testGetMessages() throws Exception {
        FIXMessage msg1 = createTestMessage("account1");
        FIXMessage msg2 = createTestMessage("account2");
        assertNull("Message", store.addMessage(1, msg1));
        assertNull("Message", store.addMessage(2, msg2));
        List<FIXMessage> msgs = store.getMessages(1, 2);
        assertEquals("size", 2, msgs.size());
        assertEquals("Message,", msg1, msgs.get(0));
        assertEquals("Message,", msg2, msgs.get(1));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyWithNullKey() throws Exception {
        store.getProperty(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPropertyWithEmptyKey() throws Exception {
        store.getProperty("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyWithNullKey() throws Exception {
        store.setProperty(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPropertyWithEmptyKey() throws Exception {
        store.setProperty("", null);
    }

    @Test
    public void testProperty() throws Exception {
        assertNull("Property", store.getProperty(TEST_KEY));
        store.setProperty(TEST_KEY, TEST_VALUE);
        assertEquals("Property", TEST_VALUE, store.getProperty(TEST_KEY));
    }

    @Test
    public void testReset() throws Exception {
        store.setMsgSeqNumIn(123);
        store.setMsgSeqNumOut(789);
        store.setProperty(TEST_KEY, TEST_VALUE);
        store.addMessage(1, createTestMessage("account1"));

        store.reset();
        store.getMessages(1, 1).toArray();
        assertNull("Property", store.getProperty(TEST_KEY));
        assertEquals("SeqNumIn", MessageStore.UNDEFINED, store.getMsgSeqNumIn());
        assertEquals("SeqNumOut", MessageStore.UNDEFINED, store.getMsgSeqNumOut());
    }

    private FIXMessage createTestMessage(String account) {
        FIXMessage msg = new StandardFIXMessage(MsgTypes.NewOrderSingle);
        msg.setFieldValue(Tags.SenderCompID, "ABC");
        msg.setFieldValue(Tags.TargetCompID, "XYZ");
        msg.setFieldValue(Tags.Account, account);
        return msg;
    }

}
