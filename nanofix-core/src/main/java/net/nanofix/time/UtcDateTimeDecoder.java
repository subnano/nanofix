package net.nanofix.time;

import java.nio.ByteBuffer;

public class UtcDateTimeDecoder {

    public static long decode(final ByteBuffer buffer, final int offset) {
        return UtcDateDecoder.decode(buffer, offset) +
                UtcTimeDecoder.decode(buffer, offset + 9);
    }
}
