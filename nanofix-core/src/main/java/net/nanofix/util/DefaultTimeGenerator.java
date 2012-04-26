package net.nanofix.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * User: Mark
 * Date: 26/04/12
 * Time: 16:28
 */
public class DefaultTimeGenerator implements DateTimeGenerator  {

    private static final DateFormat utcTimestampFormat = newUtcFormatter("yyyyMMdd-HH:mm:ss");
    private static final DateFormat utcTimestampFormatMillis = newUtcFormatter("yyyyMMdd-HH:mm:ss.SSS");

    private static DateFormat newUtcFormatter(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    @Override
    public String getUtcTime(boolean includeMilliSeconds) {
        return getUtcTime(new Date(), includeMilliSeconds);

    }

    @Override
    public String getUtcTime(Date date, boolean includeMilliSeconds) {
        return includeMilliSeconds
                ? utcTimestampFormatMillis.format(date)
                : utcTimestampFormat.format(date);
    }
}
