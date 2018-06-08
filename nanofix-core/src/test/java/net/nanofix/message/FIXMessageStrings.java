package net.nanofix.message;

import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.FIXBytes;

public class FIXMessageStrings {

    static final String HEARTBEAT = "8=FIX.4.3|9=72|35=0|49=CLIENT|56=BROKER|34=11|52=19700101-00:00:00.000|112=test-req-id|10=109|";

    static byte[] asValidByteArray(String msgString) {
        return ByteArrayUtil.asByteArray(msgString.replaceAll("\\|", String.valueOf((char) FIXBytes.SOH)));
    }
}
