package net.nanofix.time;

import io.nano.core.buffer.ByteBufferUtil;
import io.nano.core.time.TimeUtil;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import static io.nano.core.time.TimeUtil.MILLIS_PER_DAY;

public class UtcDateTimeEncoder {

    private static final int DAYS_IN_400_YEAR_CYCLE = 146097;
    private static final int DAYS_UNTIL_START_OF_UNIX_EPOCH = 719528;
    private static final int LENGTH_OF_DATE_AND_TIME = 21;

    private static final byte HYPHEN = '-';
    private static final byte COLON = ':';
    private static final byte PERIOD = '.';

    private long midnightMillis = 0L;
    private long nextMidnightMillis = 0L;

    public int encode(final long epochMillis, final ByteBuffer buffer, final int offset) {

        final boolean differentDay = isDifferentDat(epochMillis);
        if (differentDay) {
            long epochDays = TimeUtil.epochDays(epochMillis, TimeUnit.MILLISECONDS);
            encodeDate(epochDays, buffer, offset);
        }
        // encode the time
        long millisSinceMidnight = TimeUtil.millisInDay(epochMillis);
        encodeTime(millisSinceMidnight, buffer, offset + 9);
        return LENGTH_OF_DATE_AND_TIME;
    }

    private boolean isDifferentDat(long epochMillis) {
        return midnightMillis == nextMidnightMillis
                || epochMillis < midnightMillis
                || epochMillis >= nextMidnightMillis;
    }

    private void encodeDate(long epochDays, ByteBuffer buffer, int offset) {
        midnightMillis = epochDays * MILLIS_PER_DAY;
        nextMidnightMillis = midnightMillis + MILLIS_PER_DAY;

        // adjust to 0000-03-01 so leap day is at end of four year cycle
        final long zeroDay = epochDays + DAYS_UNTIL_START_OF_UNIX_EPOCH - 60;
        long yearEstimate = (400 * zeroDay + 591) / DAYS_IN_400_YEAR_CYCLE;
        long dayEstimate = TimeUtil.estimateDayOfYear(zeroDay, yearEstimate);
        if (dayEstimate < 0) {
            // fix estimate
            yearEstimate--;
            dayEstimate = TimeUtil.estimateDayOfYear(zeroDay, yearEstimate);
        }
        final int marchDay0 = (int) dayEstimate;

        // convert march-based values back to january-based
        final int marchMonth0 = (marchDay0 * 5 + 2) / 153;
        final int month = (marchMonth0 + 2) % 12 + 1;
        final int day = marchDay0 - (marchMonth0 * 306 + 5) / 10 + 1;
        final int year = (int) (yearEstimate + marchMonth0 / 10);

        ByteBufferUtil.putNumber(year, 4, buffer, offset);
        ByteBufferUtil.putNumber(month, 2, buffer, offset + 4);
        ByteBufferUtil.putNumber(day, 2, buffer, offset + 6);
        buffer.put(offset + 8, HYPHEN);
    }

    private void encodeTime(final long millisSinceMidnight, final ByteBuffer buffer, final int offset) {
        int hour = (int) TimeUnit.MILLISECONDS.toHours(millisSinceMidnight);
        long millis = millisSinceMidnight - TimeUnit.HOURS.toMillis(hour);

        int minute = (int) TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= (TimeUnit.MINUTES.toMillis(minute));

        int second = (int) TimeUnit.MILLISECONDS.toSeconds(millis);
        millis -= (TimeUnit.SECONDS.toMillis(second));

        ByteBufferUtil.putNumber(hour, 2, buffer, offset);
        buffer.put(offset + 2, COLON);
        ByteBufferUtil.putNumber(minute, 2, buffer, offset + 3);
        buffer.put(offset + 5, COLON);
        ByteBufferUtil.putNumber(second, 2, buffer, offset + 6);
        buffer.put(offset + 8, PERIOD);
        ByteBufferUtil.putNumber((int) millis, 3, buffer, offset + 9);
    }
}
