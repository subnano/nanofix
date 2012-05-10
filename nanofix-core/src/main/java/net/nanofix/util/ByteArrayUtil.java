package net.nanofix.util;

/**
 * User: Mark Wardell
 * Date: 18/10/11
 * Time: 08:04
 */
public class ByteArrayUtil {

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

    public static long toLong(byte[] bytes) throws NumberFormatException {
        return toLong(bytes, 0, bytes.length);
    }
}
