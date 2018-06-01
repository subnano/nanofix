package net.nanofix.session;

import net.nanofix.message.*;
import net.nanofix.util.TestHelper;
import net.nanofix.util.TimeUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;
/**
 * User: Mark
 * Date: 27/03/12
 * Time: 06:12
 */
public class SessionIDTest extends TestHelper {

    private static final Logger LOG = LoggerFactory.getLogger(SessionIDTest.class);

    static final int LOOP_COUNT = 10000000;

    private static SessionID getShortSessionID() {
        return new SessionID(BEGIN_STRING, SENDER_COMP_ID, TARGET_COMP_ID);
    }

    private static SessionID getMediumSessionID() {
        return new SessionID(BEGIN_STRING, SENDER_COMP_ID, SENDER_SUB_ID, TARGET_COMP_ID, TARGET_SUB_ID);
    }

    private static SessionID getLongSessionID() {
        return new SessionID(BEGIN_STRING, SENDER_COMP_ID, SENDER_SUB_ID, SENDER_LOCATION_ID,
                TARGET_COMP_ID, TARGET_SUB_ID, TARGET_LOCATION_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNulls() throws Exception {
        new SessionID(null, null, null);
    }

    @Test
    public void testShortConstructor() throws Exception {
        SessionID sessionID = getShortSessionID();
        Assert.assertEquals(BEGIN_STRING, sessionID.getBeginString());
        Assert.assertEquals(SENDER_COMP_ID, sessionID.getSenderCompID());
        Assert.assertNull("senderSubID", sessionID.getSenderSubID());
        Assert.assertNull("senderLocationID", sessionID.getSenderLocationID());
        Assert.assertEquals(TARGET_COMP_ID, sessionID.getTargetCompID());
        Assert.assertNull("targetSubID", sessionID.getTargetSubID());
        Assert.assertNull("targetLocationID", sessionID.getTargetLocationID());
        Assert.assertEquals(BEGIN_STRING + ":" + SENDER_COMP_ID + "~" + TARGET_COMP_ID,
                sessionID.toString());
    }

    @Test
    public void testMediumConstructor() throws Exception {
        SessionID sessionID = getMediumSessionID();
        Assert.assertEquals(BEGIN_STRING, sessionID.getBeginString());
        Assert.assertEquals(SENDER_COMP_ID, sessionID.getSenderCompID());
        Assert.assertEquals(SENDER_SUB_ID, sessionID.getSenderSubID());
        Assert.assertNull("senderLocationID", sessionID.getSenderLocationID());
        Assert.assertEquals(TARGET_COMP_ID, sessionID.getTargetCompID());
        Assert.assertEquals(TARGET_SUB_ID, sessionID.getTargetSubID());
        Assert.assertNull("targetLocationID", sessionID.getTargetLocationID());
        Assert.assertEquals(BEGIN_STRING + ":" + SENDER_COMP_ID + "." + SENDER_SUB_ID
                + "~" + TARGET_COMP_ID + "." + TARGET_SUB_ID,
                sessionID.toString());
    }

    @Test
    public void testLongConstructor() throws Exception {
        SessionID sessionID = getLongSessionID();
        Assert.assertEquals(BEGIN_STRING, sessionID.getBeginString());
        Assert.assertEquals(SENDER_COMP_ID, sessionID.getSenderCompID());
        Assert.assertEquals(SENDER_LOCATION_ID, sessionID.getSenderLocationID());
        Assert.assertEquals(TARGET_COMP_ID, sessionID.getTargetCompID());
        Assert.assertEquals(TARGET_SUB_ID, sessionID.getTargetSubID());
        Assert.assertEquals(TARGET_LOCATION_ID, sessionID.getTargetLocationID());
        Assert.assertEquals(BEGIN_STRING + ":" + SENDER_COMP_ID + "." + SENDER_SUB_ID + "." + SENDER_LOCATION_ID
                + "~" + TARGET_COMP_ID + "." + TARGET_SUB_ID + "." + TARGET_LOCATION_ID,
                sessionID.toString());
    }

    public String testStringBuilderToString(SessionID sessionId) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(sessionId.getBeginString())
                .append(":")
                .append(sessionId.getSenderCompID());
        if (sessionId.getSenderSubID() != null)
            sb.append(".").append(sessionId.getSenderSubID());
        if (sessionId.getSenderLocationID() != null)
            sb.append(".").append(sessionId.getSenderLocationID());
        sb.append("/").append(sessionId.getTargetCompID());
        if (sessionId.getTargetSubID() != null)
            sb.append(".").append(sessionId.getTargetSubID());
        if (sessionId.getTargetLocationID() != null)
                sb.append(".").append(sessionId.getTargetLocationID());
        return sb.toString();
    }

    @Test
    public void testParseMessage() {
        FIXMessage msg = new StandardFIXMessage(MsgTypes.Logon);
        msg.setFieldValue(Tags.BeginString, FIXVersion.FIX42);
        msg.setFieldValue(Tags.SenderCompID, SENDER_COMP_ID);
        msg.setFieldValue(Tags.TargetCompID, TARGET_COMP_ID);

        SessionID sessionID = SessionID.parse(msg);
        assertEquals("senderCompID", SENDER_COMP_ID, sessionID.getSenderCompID());
        assertEquals("targetCompID", TARGET_COMP_ID, sessionID.getTargetCompID());
        assertEquals("toString",
                BEGIN_STRING + ":" + SENDER_COMP_ID + "~" + TARGET_COMP_ID,
                sessionID.toString());

        // try other fields too
        msg.setFieldValue(Tags.SenderSubID, SENDER_SUB_ID);
        msg.setFieldValue(Tags.SenderLocationID, SENDER_LOCATION_ID);
        msg.setFieldValue(Tags.TargetSubID, TARGET_SUB_ID);
        msg.setFieldValue(Tags.TargetLocationID, TARGET_LOCATION_ID);

        sessionID = SessionID.parse(msg);
        assertEquals("senderCompID", SENDER_COMP_ID, sessionID.getSenderCompID());
        assertEquals("senderSubID", SENDER_SUB_ID, sessionID.getSenderSubID());
        assertEquals("senderLocationID", SENDER_LOCATION_ID, sessionID.getSenderLocationID());
        assertEquals("targetCompID", TARGET_COMP_ID, sessionID.getTargetCompID());
        assertEquals("targetSubID", TARGET_SUB_ID, sessionID.getTargetSubID());
        assertEquals("targetLocationID", TARGET_LOCATION_ID, sessionID.getTargetLocationID());
        Assert.assertEquals(BEGIN_STRING + ":" + SENDER_COMP_ID + "." + SENDER_SUB_ID + "." + SENDER_LOCATION_ID
                + "~" + TARGET_COMP_ID + "." + TARGET_SUB_ID + "." + TARGET_LOCATION_ID,
                sessionID.toString());
    }

    @Test
    public void testReverse() {
        SessionID sessionID = getLongSessionID();
        SessionID reverseSessionID = sessionID.reverse();
        assertEquals("senderCompID", TARGET_COMP_ID, reverseSessionID.getSenderCompID());
        assertEquals("senderSubID", TARGET_SUB_ID, reverseSessionID.getSenderSubID());
        assertEquals("senderLocationID", TARGET_LOCATION_ID, reverseSessionID.getSenderLocationID());
        assertEquals("targetCompID", SENDER_COMP_ID, reverseSessionID.getTargetCompID());
        assertEquals("targetSubID", SENDER_SUB_ID, reverseSessionID.getTargetSubID());
        assertEquals("targetLocationID", SENDER_LOCATION_ID, reverseSessionID.getTargetLocationID());
    }

    private long benchmarkTest1() {
        long start = System.nanoTime();
        for (int i=0;i< LOOP_COUNT;i++) {
            new SessionID("FIX.4.2", "SENDER", "TARGET").toString();
        }
        long elapsed = TimeUtil.getNanoTimeAsMillis(System.nanoTime() - start);
        LOG.debug("test #1 complete in {} millis", elapsed);
        return elapsed;
    }

    private long benchmarkTest2() {
        long start = System.nanoTime();
        for (int i=0;i< LOOP_COUNT;i++) {
            testStringBuilderToString(new SessionID("FIX.4.2", "SENDER", "TARGET"));
        }
        long elapsed = TimeUtil.getNanoTimeAsMillis(System.nanoTime() - start);
        LOG.debug("test #2 complete in {} millis", elapsed);
        return elapsed;
    }
}
