package net.nanofix.util;

import com.google.common.base.Stopwatch;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * User: Mark
 * Date: 24/03/12
 * Time: 14:53
 */
public class ByteArrayUtilTest {

    @Test
    public void testIsDigit() throws Exception {
        Assert.assertTrue(ByteArrayUtil.isDigit((byte)'0'));
        Assert.assertTrue(ByteArrayUtil.isDigit((byte)'9'));
        Assert.assertFalse(ByteArrayUtil.isDigit((byte) 'A'));
    }

    @Test
    public void testToInteger() throws Exception {
        Assert.assertEquals(12, ByteArrayUtil.toInteger(new byte[] { 49, 50 }));
        Assert.assertEquals(123, ByteArrayUtil.toInteger(new byte[] { 49, 50, 51 }));
        Assert.assertEquals(1234, ByteArrayUtil.toInteger(new byte[] { 49, 50, 51, 52 }));
        Assert.assertEquals(-1234, ByteArrayUtil.toInteger(new byte[] { 45, 49, 50, 51, 52 }));
    }

    @Test
    public void testBenchmark() {
        final int TEST_LOOPS = 1000 * 1000 * 1000;

        Stopwatch stopwatch = new Stopwatch().start();
        for (int i=0; i<=TEST_LOOPS; i++) {
            long result = 0 - 12345L;
        }
        TestHelper.printResults("subtract", TEST_LOOPS, stopwatch.elapsedMillis());
        stopwatch.stop();

        stopwatch.start();
        for (int i=0; i<=TEST_LOOPS; i++) {
            long result = 12345L * -1;
        }
        TestHelper.printResults("multiply", TEST_LOOPS, stopwatch.elapsedMillis());
    }
}
