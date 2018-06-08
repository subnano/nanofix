package net.nanofix.netty;

import io.netty.buffer.ByteBuf;

import static net.nanofix.util.ByteArrayUtil.isDigit;


public final class BufferUtil {


    private BufferUtil() {
        // can't touch this
    }

    public static boolean hasBytes(ByteBuf buf, byte[] expected, int start) {
        for (int i = 0; i < expected.length; i++) {
            if (buf.getByte(start + i) != expected[i]) {
                return false;
            }
        }
        return true;
    }

    public static int toInteger(ByteBuf buffer, int offset, int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Positive length expected");
        }
        byte aByte;
        boolean positive = true;
        int number = 0;
        for (int i = offset; i < offset + length; i++) {
            aByte = buffer.getByte(i);
            if (i == offset && aByte == '+') {
                positive = true;
            } else if (i == offset && aByte == '-') {
                positive = false;
            } else if (!isDigit(aByte)) {
                throw new NumberFormatException("Integer number expected at position " + i + " not " + aByte + ")");
            } else {
                number = (number * 10) + aByte - '0';
            }
        }
        return positive ? number : 0 - number;

    }
}
