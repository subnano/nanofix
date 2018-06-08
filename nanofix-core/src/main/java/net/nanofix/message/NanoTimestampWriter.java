package net.nanofix.message;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class NanoTimestampWriter implements TimestampWriter {

    private static final TimestampWriter TIMESTAMP_MILLIS_WRITER = new NanoTimestampWriter(TimeUnit.MILLISECONDS);
    private static final TimestampWriter NOOP_WRITER = (b, v) -> { /* NO-OP */};

    private final TimeUnit timeUnit;

    public NanoTimestampWriter(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public static TimestampWriter getWriter(TimeUnit timeUnit) {
        if (TimeUnit.MILLISECONDS == timeUnit) {
            return TIMESTAMP_MILLIS_WRITER;
        }
        return NOOP_WRITER;
    }

    @Override
    public void accept(ByteBuffer byteBuffer, long timestamp) {
        long days = timeUnit.toDays(timestamp);
    }
}
