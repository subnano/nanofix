package net.nanofix.util;

/**
 * User: Mark Wardell
 * Date: 18/10/11
 * Time: 08:04
 */
public class ByteArrayUtil {

    private static final byte[][] BYTE_ARRAY_1 = new byte[10][1];
    private static final byte[][] BYTE_ARRAY_2 = new byte[100][2];
    private static final byte[][] BYTE_ARRAY_3 = new byte[1000][3];
    private static final byte[][] BYTE_ARRAY_4 = new byte[10000][4];

    private static final boolean INTEGER_CACHE_DISABLED = Boolean.getBoolean("io.nano.integer.cache.disabled");

    private static final byte ZERO = (byte) '0';

    static {
        // populate the 1 byte array
        for (int i=0;i<10;i++) {
            BYTE_ARRAY_1[i] = ByteArrayUtil.as1ByteArray(i);
        }
        // populate the 2 bytes array
        for (int i=0;i<100;i++) {
            BYTE_ARRAY_2[i] = ByteArrayUtil.as2ByteArray(i);
        }
        // populate the 3 bytes array
        for (int i=0;i<1000;i++) {
            BYTE_ARRAY_3[i] = ByteArrayUtil.as3ByteArray(i);
        }
        // populate the 2 bytes array
        for (int i=0;i<10000;i++) {
            BYTE_ARRAY_4[i] = ByteArrayUtil.as4ByteArray(i);
        }
    }

    public static boolean isDigit(byte aByte) {
        return aByte >= '0' && aByte <= '9';
    }

    public static int toInteger(byte bytes[]) {
        return toInteger(bytes, 0, bytes.length);
    }

    public static int toInteger(byte bytes[], int offset, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Positive length expected");
        }
        boolean positive = true;
        int number = 0;
        for (int i=offset; i<offset+length; i++) {
            if (i==offset && bytes[i] == '+') {
                positive = true;
            }
            else if (i==offset && bytes[i] == '-') {
                positive = false;
            }
            else if (!isDigit(bytes[i])) {
                throw new NumberFormatException("Integer number expected (" + new String(bytes, offset, length) + ")");
            }
            else {
                number = (number * 10) + bytes[i] - '0';
            }
        }
        return positive ? number : 0-number;
    }

    public static long toLong(byte[] bytes) throws NumberFormatException {
        return toLong(bytes, 0, bytes.length);
    }

    public static long toLong(byte bytes[], int offset, int length) throws NumberFormatException {
        if (length <= 0) {
            throw new IllegalArgumentException("Positive length expected");
        }
        boolean positive = true;
        long number = 0L;
        for (int i=offset; i<offset+length; i++) {
            if (i==offset && bytes[i] == '+') {
                positive = true;
            }
            else if (i==offset && bytes[i] == '-') {
                positive = false;
            }
            else if (!isDigit(bytes[i])) {
                throw new NumberFormatException("Long number expected (" + new String(bytes, offset, length) + ")");
            }
            else {
                number = (number * 10) + bytes[i] - '0';
            }
        }
        return positive ? number : 0-number;
    }

    /**
     * Only works for US_ASCII and UTF-8 charset encoded strings.
     * Strips the higher order bits from other charset encoded characters.
     */
    public static byte[] asByteArray(String string) {
        int len = string == null ? 0 : string.length();
        byte[] bytes = new byte[len];
        if (len > 0) {
            for (int i=0; i<len; i++) {
                bytes[i] = (byte) string.charAt(i);
            }
        }
        return bytes;
    }

    public static byte[] asByteArray(int value) {
        if (value < 0) {
            return intToBytesAsString(value);
        }
        if (value <= 9) {
            return INTEGER_CACHE_DISABLED ? as1ByteArray(value) : BYTE_ARRAY_1[value];
        }
        if (value <= 99) {
            return INTEGER_CACHE_DISABLED ? as2ByteArray(value) : BYTE_ARRAY_2[value];
        }
        if (value <= 999) {
            return INTEGER_CACHE_DISABLED ? as3ByteArray(value) : BYTE_ARRAY_3[value];
        }
        if (value <= 9999) {
            return INTEGER_CACHE_DISABLED ? as4ByteArray(value) : BYTE_ARRAY_4[value];
        }
        else {
            return intToBytesAsString(value);
        }
    }

    public static byte[] asByteArray(long value) {
        if (value >= 0 && value <= Integer.MAX_VALUE) {
            return asByteArray((int)value);
        }
        return longToBytesAsString(value);
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

    static byte[] intToBytesAsString(int value) {
        return Integer.toString(value).getBytes();
    }

    private static byte[] longToBytesAsString(long value) {
        return Long.toString(value).getBytes();
    }

    /**
     * Replace all occurrences of <code>find</code> in 'bytes' with replace
     */
    public static String replace(byte[] bytes, byte find, byte replace) {
        if (bytes != null) {
            for (int i=0; i<bytes.length; i++) {
                if (bytes[i] == find) bytes[i] = replace;
            }
        }
        return null;
    }
}
