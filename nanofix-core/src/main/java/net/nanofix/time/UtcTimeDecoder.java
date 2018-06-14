package net.nanofix.time;

import io.nano.core.buffer.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class UtcTimeDecoder {

    public static long decode(final ByteBuffer buffer, final int offset) {
        final int hour = ByteBufferUtil.toInt(buffer, offset, 2);
        final int minute = ByteBufferUtil.toInt(buffer, offset + 3, 2);
        final int second = ByteBufferUtil.toInt(buffer, offset + 6, 2);
        final int millisecond = ByteBufferUtil.toInt(buffer, offset + 9, 3);
        // TODO validate numeric range
        return TimeUnit.HOURS.toMillis(hour)
                + TimeUnit.MINUTES.toMillis(minute)
                + TimeUnit.SECONDS.toMillis(second)
                + millisecond;
    }
}
