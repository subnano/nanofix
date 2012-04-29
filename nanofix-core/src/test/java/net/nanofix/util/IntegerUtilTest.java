package net.nanofix.util;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import static org.junit.Assert.*;
import org.mockito.internal.matchers.ArrayEquals;

/**
 * User: Mark
 * Date: 28/04/12
 * Time: 14:52
 */
public class IntegerUtilTest {

    private static final int TEST_LOOPS = 10 * 1000 * 1000;

    @Test
    public void testConvertToBytes() throws Exception {
        assertThat(IntegerUtil.asByteArray(3), new ArrayEquals(new byte[]{'0' + 3}));
        assertThat(IntegerUtil.asByteArray(34), new ArrayEquals(new byte[]{ '0'+3, '0'+4 }));
        assertThat(IntegerUtil.asByteArray(345), new ArrayEquals(new byte[]{ '0'+3, '0'+4, '0'+5 }));
        assertThat(IntegerUtil.asByteArray(3456), new ArrayEquals(new byte[]{ '0'+3, '0'+4, '0'+5, '0'+6 }));
    }

    @Test
    public void testConvertToBytesAsString() throws Exception {

    }

    @Test
    public void testConvertToBytesPerformance() {
        IntegerUtil.useCachedValues = false;
        Stopwatch stopwatch = new Stopwatch().start();
        byte[] bytes = null;
        for (int i=0; i<=TEST_LOOPS; i++) {
            bytes = IntegerUtil.asByteArray(i % 10000);
        }
        printResults("convertToBytes", stopwatch.elapsedMillis());
    }

    @Test
    public void testConvertToBytesCachedPerformance() {
        IntegerUtil.useCachedValues = true;
        Stopwatch stopwatch = new Stopwatch().start();
        byte[] bytes = null;
        for (int i=0; i<=TEST_LOOPS; i++) {
            bytes = IntegerUtil.asByteArray(i % 10000);
        }
        printResults("convertToBytesCached", stopwatch.elapsedMillis());
    }

    @Test
    public void testConvertToBytesAsStringPerformance() {
        Stopwatch stopwatch = new Stopwatch().start();
        byte[] bytes = null;
        for (int i=0; i<=TEST_LOOPS; i++) {
            bytes = IntegerUtil.convertToBytesAsString(i % 10000);
        }
        printResults("convertToBytesAsString", stopwatch.elapsedMillis());
    }

    private void printResults(String msg, long elapsed) {
        System.out.println(TEST_LOOPS + " x " + msg + " took " + elapsed + "ms @ "
                + (double)elapsed * 1000 * 1000 / (double)TEST_LOOPS + " ns");
    }

}
