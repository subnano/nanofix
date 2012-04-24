package net.nanofix.session;

import net.nanofix.util.TimeUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mark
 * Date: 27/03/12
 * Time: 06:12
 */
public class SessionIDTest {

    private static final Logger LOG = LoggerFactory.getLogger(SessionIDTest.class);

    private static final String BEGIN_STRING = "FIX.4.2";
    private static final String SENDER_COMP_ID = "SENDER_COMP_ID";
    private static final String SENDER_SUB_ID = "SENDER_SUB_ID";
    private static final String SENDER_LOCATION_ID = "SENDER_LOCATION_ID";
    private static final String TARGET_COMP_ID = "TARGET_COMP_ID";
    private static final String TARGET_SUB_ID = "TARGET_SUB_ID";
    private static final String TARGET_LOCATION_ID = "TARGET_LOCATION_ID";

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

    @Test
    public void testShortConstructor() throws Exception {
        SessionID sessionID = getShortSessionID();
        Assert.assertEquals(BEGIN_STRING, sessionID.getBeginString());
        Assert.assertEquals(SENDER_COMP_ID, sessionID.getSenderCompID());
        Assert.assertEquals(SessionID.UNDEFINED, sessionID.getSenderSubID());
        Assert.assertEquals(SessionID.UNDEFINED, sessionID.getSenderLocationID());
        Assert.assertEquals(TARGET_COMP_ID, sessionID.getTargetCompID());
        Assert.assertEquals(SessionID.UNDEFINED, sessionID.getTargetSubID());
        Assert.assertEquals(SessionID.UNDEFINED, sessionID.getTargetLocationID());
        Assert.assertEquals(BEGIN_STRING + ":" + SENDER_COMP_ID + "~" + TARGET_COMP_ID,
                sessionID.toString());
    }

    @Test
    public void testMediumConstructor() throws Exception {
        SessionID sessionID = getMediumSessionID();
        Assert.assertEquals(BEGIN_STRING, sessionID.getBeginString());
        Assert.assertEquals(SENDER_COMP_ID, sessionID.getSenderCompID());
        Assert.assertEquals(SENDER_SUB_ID, sessionID.getSenderSubID());
        Assert.assertEquals(SessionID.UNDEFINED, sessionID.getSenderLocationID());
        Assert.assertEquals(TARGET_COMP_ID, sessionID.getTargetCompID());
        Assert.assertEquals(TARGET_SUB_ID, sessionID.getTargetSubID());
        Assert.assertEquals(SessionID.UNDEFINED, sessionID.getTargetLocationID());
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
    
    public void benchMarkComparisonTest() {
        Assert.assertTrue(benchmarkTest1() > benchmarkTest2());
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
