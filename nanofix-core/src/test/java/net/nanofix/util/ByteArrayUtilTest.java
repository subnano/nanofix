package net.nanofix.util;

import io.nano.core.clock.SystemClock;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.time.Clock;

/**
 * User: Mark
 * Date: 24/03/12
 * Time: 14:53
 */
public class ByteArrayUtilTest {

    SystemClock clock = new SystemClock();

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

        long start = clock.currentTimeMillis();
        for (int i=0; i<=TEST_LOOPS; i++) {
            long result = 0 - 12345L;
        }
        TestHelper.printResults("subtract", TEST_LOOPS, clock.currentTimeMillis() - start);

        start = clock.currentTimeMillis();
        for (int i=0; i<=TEST_LOOPS; i++) {
            long result = 12345L * -1;
        }
        TestHelper.printResults("multiply", TEST_LOOPS, clock.currentTimeMillis() - start);
    }
}
