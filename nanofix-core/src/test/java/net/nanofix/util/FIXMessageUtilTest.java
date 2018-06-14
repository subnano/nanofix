package net.nanofix.util;

import net.nanofix.message.Tags;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.*;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 22/03/12
 * Time: 16:25
 */
public class FIXMessageUtilTest {

    private static final Logger LOG = LoggerFactory.getLogger(FIXMessageUtilTest.class);

    static final int LOOP_COUNT = 10000000;
    static final String adminMsgTypes = "0A12345";
    static final String[] adminMsgTypeArray = { "0","A","1","2","3","4","5"};

    @Test
    public void testIsAdminMessage() {
        assertTrue(FIXMessageUtil.isAdminMessage("0"));
        assertTrue(FIXMessageUtil.isAdminMessage("A"));
        assertTrue(FIXMessageUtil.isAdminMessage("1"));
        assertTrue(FIXMessageUtil.isAdminMessage("2"));
        assertTrue(FIXMessageUtil.isAdminMessage("3"));
        assertTrue(FIXMessageUtil.isAdminMessage("4"));
        assertTrue(FIXMessageUtil.isAdminMessage("5"));
        assertFalse(FIXMessageUtil.isAdminMessage("6"));
        assertFalse(FIXMessageUtil.isAdminMessage("a"));
        assertFalse(FIXMessageUtil.isAdminMessage("B"));
    }

    @Test
    public void testHeaderFields() {
        assertTrue(FIXMessageUtil.isHeaderField(Tags.BeginString));
        assertTrue(FIXMessageUtil.isHeaderField(Tags.BodyLength));
        assertTrue(FIXMessageUtil.isHeaderField(Tags.MsgType));
        assertTrue(FIXMessageUtil.isHeaderField(Tags.SenderCompID));
        assertTrue(FIXMessageUtil.isHeaderField(Tags.TargetCompID));
        assertTrue(FIXMessageUtil.isHeaderField(Tags.MsgSeqNum));
        assertTrue(FIXMessageUtil.isHeaderField(Tags.PossDupFlag));
        assertTrue(FIXMessageUtil.isHeaderField(Tags.PossResend));
        assertTrue(FIXMessageUtil.isHeaderField(Tags.SendingTime));
    }

    @Test
    public void testFix5HeaderFields() {
        assertTrue(FIXMessageUtil.isHeaderField(Tags.ApplVerID));
        assertTrue(FIXMessageUtil.isHeaderField(Tags.CstmApplVerID));
    }

    @Test
    public void testTrailerFields() {
        assertThat(FIXMessageUtil.isTrailerField(Tags.SignatureLength), is(true));
        assertThat(FIXMessageUtil.isTrailerField(Tags.Signature), is(true));
        assertThat(FIXMessageUtil.isTrailerField(Tags.CheckSum), is(true));
    }

    private long testIsAdminMessageAsString(String[] testMsgTypes) {
        long start = System.nanoTime();
        for (int i=0;i< LOOP_COUNT;i++) {
            for (String msgType : testMsgTypes) {
                FIXMessageUtil.isAdminMessage(msgType);
            }
        }
        long elapsed = TimeUtil2.getNanoTimeAsMillis(System.nanoTime() - start);
        LOG.debug("string test complete in {} millis", elapsed);
        return elapsed; 
    }
    
    private long testIsAdminMessage(String[] testMsgTypes) {
        long start = System.nanoTime();
        for (int i=0;i< LOOP_COUNT;i++) {
            for (String msgType : testMsgTypes) {
                isAdminMessageAsString(msgType);
            }
        }
        long elapsed = TimeUtil2.getNanoTimeAsMillis(System.nanoTime() - start);
        LOG.debug("case test complete in {} millis", elapsed);
        return elapsed;
    }

    private static boolean isAdminMessageAsString(String msgType) {
        return msgType.length() == 1 && "0A12345".contains(msgType);
    }

}
