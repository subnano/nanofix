package net.nanofix.message;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 23/03/12
 * Time: 06:36
 */
public class FIXVersion {

    public static final String FIX40 = "FIX.4.0";
    public static final String FIX41 = "FIX.4.1";
    public static final String FIX42 = "FIX.4.2";
    public static final String FIX43 = "FIX.4.3";
    public static final String FIX44 = "FIX.4.4";

    /**
     * FIX 5.0 does not have a begin string.
     */
    public static final String FIX50 = "FIX.5.0";

    // FIXT.x.x support

    public static final String FIXT = "FIXT.";
    public static final String FIXT11 = FIXT + "1.1";
}
