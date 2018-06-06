package net.nanofix.message.util;

import net.nanofix.util.ByteArrayUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class ChecksumCalculator {

    private static final byte[][] CHECKSUMS = new byte[1000][3];

    private ChecksumCalculator() {
        // can't touch this
    }

    static {
        for (int i = 0; i < 256; i++) {
            CHECKSUMS[i] = ByteArrayUtil.as3ByteArray(i);
        }
    }

    public static int calculateChecksum(ByteBuffer buffer, int offset, int len) {
        int checksum = 0;
        if (buffer != null) {
            System.out.println("calculateChecksum - " + new String(buffer.array(), offset, len, StandardCharsets.US_ASCII));
            System.out.println("calculateChecksum - offset: " + offset + " len: " + len);
            for (int index = offset; index < len; index++) {
                System.out.println(index + " " + String.valueOf((char)buffer.get(index)) + " " + checksum);
                checksum += buffer.get(index);
            }
        }
        return checksum % 256;
    }

    public static byte[] calculateChecksum(ByteBuffer[] buffers) {
        int checksum = 0;
        if (buffers != null) {
            for (ByteBuffer buffer : buffers) {
                if (buffer != null) {
                    for (int i = 0; i < buffer.remaining(); i++) {
                        checksum += buffer.get(i);
                    }
                }
            }
        }
        return CHECKSUMS[checksum % 256];
    }
}
