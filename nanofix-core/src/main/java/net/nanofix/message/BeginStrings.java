package net.nanofix.message;

import net.nanofix.util.ByteString;

public final class BeginStrings {

    private BeginStrings() {
        // can't touch this
    }

    public static ByteString FIX_4_2 = ByteString.of("FIX.4.2");
    public static ByteString FIX_4_3 = ByteString.of("FIX.4.3");
    public static ByteString FIX_4_4 = ByteString.of("FIX.4.4");
}
