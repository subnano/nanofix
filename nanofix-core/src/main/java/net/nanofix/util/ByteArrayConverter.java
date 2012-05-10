package net.nanofix.util;

import java.util.Arrays;

/**
 * User: Mark
 * Date: 28/04/12
 * Time: 14:44
 */
public class ByteArrayConverter {

    private static final byte ZERO = (byte) '0';

    private static final byte[][] byteArray2 = new byte[100][2];
    private static final byte[][] byteArray3 = new byte[1000][3];
    private static final byte[][] byteArray4 = new byte[10000][4];

    public static boolean useCachedValues = true;

    static {
        // populate the 2 bytes array
        for (int i=0;i<100;i++) {
            byteArray2[i] = as2ByteArray(i);
        }
        // populate the 3 bytes array
        for (int i=0;i<1000;i++) {
            byteArray3[i] = as3ByteArray(i);
        }
        // populate the 2 bytes array
        for (int i=0;i<10000;i++) {
            byteArray4[i] = as4ByteArray(i);
        }
    }

    public static byte[] asByteArray(int value) {
        if (value < 0) {
            return convertToBytesAsString(value);
        }
        if (value <= 9) {
            return as1ByteArray(value);
        }
        if (value <= 99) {
            return useCachedValues ? byteArray2[value] : as2ByteArray(value);
        }
        if (value <= 999) {
            return useCachedValues ? byteArray3[value] : as3ByteArray(value);
        }
        if (value <= 9999) {
            return useCachedValues ? byteArray4[value] : as4ByteArray(value);
        }
        else {
            return convertToBytesAsString(value);
        }
    }

    public static byte[] as1ByteArray(int value) {
        byte[] bytes = new byte[1];
        bytes[0] = (byte)(value % 10 + 48);
        return bytes;
    }

    public static byte[] as2ByteArray(int value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte)((value % 100 - value % 10) / 10   + 48);
        bytes[1] = (byte)(value % 10 + 48);
        return bytes;
    }

    public static byte[] as3ByteArray(int value) {
        byte[] bytes = new byte[3];
        bytes[0] = (byte)((value % 1000 - value % 100) / 100   + 48);
        bytes[1] = (byte)((value % 100 - value % 10) / 10   + 48);
        bytes[2] = (byte)(value % 10 + 48);
        return bytes;
    }

    public static byte[] as4ByteArray(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte)((value % 10000 - value % 1000) / 1000   + 48);
        bytes[1] = (byte)((value % 1000 - value % 100) / 100   + 48);
        bytes[2] = (byte)((value % 100 - value % 10) / 10   + 48);
        bytes[3] = (byte)(value % 10 + 48);
        return bytes;
    }

    public static byte[] convertToBytesAsString(int value) {
        return Integer.toString(value).getBytes();
    }

    public static byte[] convertToBytesAsString(long value) {
        return Long.toString(value).getBytes();
    }

    public static byte[] asByteArray(long value) {
        if (value >= 0 && value <= Integer.MAX_VALUE) {
            return asByteArray((int)value);
        }
        return convertToBytesAsString(value);
    }

}
