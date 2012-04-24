package net.nanofix.util;

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
        Assert.assertEquals(123, ByteArrayUtil.toInteger(toBytes(123)));
    }

    private static byte[] toBytes(int value) {
        return BigInteger.valueOf(value).toByteArray();
    }
}
