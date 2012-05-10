package net.nanofix.util;

import org.junit.Ignore;

/**
 * User: Mark
 * Date: 01/05/12
 * Time: 17:41
 */
@Ignore
public class TestHelper {

    public static final String BEGIN_STRING = "FIX.4.2";
    public static final String SENDER_COMP_ID = "SENDER_COMP_ID";
    public static final String SENDER_SUB_ID = "SENDER_SUB_ID";
    public static final String SENDER_LOCATION_ID = "SENDER_LOCATION_ID";
    public static final String TARGET_COMP_ID = "TARGET_COMP_ID";
    public static final String TARGET_SUB_ID = "TARGET_SUB_ID";
    public static final String TARGET_LOCATION_ID = "TARGET_LOCATION_ID";

    public static void printResults(String msg, long loops, long elapsed) {
        System.out.println(loops + " x " + msg + " took " + elapsed + "ms @ "
                + (double)elapsed * 1000 * 1000 / (double)loops + " ns");
    }

}
