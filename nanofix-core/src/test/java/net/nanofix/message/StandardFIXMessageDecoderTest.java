package net.nanofix.message;

import net.nanofix.netty.StandardFIXMessageDecoder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * User: Mark
 * Date: 29/03/12
 * Time: 21:09
 */
public class StandardFIXMessageDecoderTest {

    private StandardFIXMessageDecoder decoder;
    private static final String M1 = "8=FIX.4.0\u00019=61\u000135=A\u000134=1\u000149=BANZAI\u000152=20120331-10:25:15\u000156=EXEC\u000198=0\u0001108=30\u000110=255\u0001";

    @Before
    public void setup() {
        decoder = new StandardFIXMessageDecoder(new DefaultFIXMessageFactory());
    }

    @Test
    public void testInvalidBeginString() throws Exception {
    }

    @Test
    public void testDecodeMessage() throws Exception {
        FIXMessage msg = decoder.decode(M1.getBytes());
        assertThat("null msg", msg, is(not(nullValue())));
        assertThat("incorrect msgType", msg.getMsgType(), is(MsgTypes.Logon));
        assertThat("incorrect seqNum", msg.getMsgSeqNum(), is(1L));
    }

}
