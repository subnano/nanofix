package net.nanofix.util;

import java.util.Date;

/**
 * User: Mark
 * Date: 26/04/12
 * Time: 18:28
 */
public interface DateTimeGenerator {

    String getUtcTime(boolean includeMilliSeconds);

    String getUtcTime(Date date, boolean includeMilliSeconds);
}
