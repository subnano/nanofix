package net.nanofix.util;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 22/03/12
 * Time: 20:21
 */
public class TimeUtil2 {

    private static long initialNanoTime;
    private static long initialCurrentTimeMillis;

    public static long getNanoTimeAsMillis(long nanoTime) {
        return nanoTime / 1000000L;
    }

    public static long currentTimeMillisFromNanoTime() {
        return getMillisFromNanoTime(System.nanoTime());
    }

    private static long getMillisFromNanoTime(long nanoTime) {
        long millis;
        if (initialCurrentTimeMillis == 0L) {
            initialCurrentTimeMillis = System.currentTimeMillis();
            millis = initialCurrentTimeMillis;
            initialNanoTime = nanoTime;
        } else {
            if (initialCurrentTimeMillis == System.currentTimeMillis()) {
                long offsetMillis = (nanoTime - initialNanoTime) / 1000000L;
                millis = initialCurrentTimeMillis + offsetMillis;
            } else {
                initialCurrentTimeMillis = System.currentTimeMillis();
                millis = initialCurrentTimeMillis;
                initialNanoTime = nanoTime;
            }
        }
        return millis;
    }

}
