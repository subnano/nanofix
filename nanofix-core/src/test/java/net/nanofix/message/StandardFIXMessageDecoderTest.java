package net.nanofix.message;

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
    private static final String M1 = "8=FIX.4.4\u00019=74\u000135=A\u000149=FIXLDNMD1\u000156=COBAFXMD\u000134=1\u000152=20111012-16:30:59\u0001108=30\u000198=0\u0001141=Y\u000110=043\u0001";

    @Before
    public void setup() {
        decoder = new StandardFIXMessageDecoder();
    }

    @Test
    public void testInvalidBeginString() throws Exception {
    }

    @Test
    public void testDecodeMessage() throws Exception {
        decoder = new StandardFIXMessageDecoder();
        FIXMessage msg = decoder.decodeMessage(M1.getBytes());
        assertThat("null msg", msg, is(not(nullValue())));
        assertThat("incorrect msgType", msg.getMsgType(), is(MsgTypes.Logon));
        assertThat("incorrect seqNum", msg.getMsgSeqNum(), is(1L));
    }

}
