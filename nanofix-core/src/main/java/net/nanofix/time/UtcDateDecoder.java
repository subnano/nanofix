package net.nanofix.time;

import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.time.TimeUtil;

import java.nio.ByteBuffer;

public class UtcDateDecoder {

    public static long decode(final ByteBuffer buffer, final int offset) {
        final int year = ByteBufferUtil.toInt(buffer, offset, 4);
        final int month = ByteBufferUtil.toInt(buffer, offset + 4, 2);
        final int day = ByteBufferUtil.toInt(buffer, offset + 6, 2);
        // TODO validate numeric range
        long epochDay = TimeUtil.toEpochDay(year, month, day);
        return epochDay * TimeUtil.MILLIS_PER_DAY;
    }
}
