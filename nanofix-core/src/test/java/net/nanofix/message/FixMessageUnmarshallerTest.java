package net.nanofix.message;

import junit.framework.TestCase;

/**
 * User: Mark
 * Date: 26/03/12
 * Time: 16:22
 */
public class FixMessageUnmarshallerTest extends TestCase {
    public void testToFIXMessage() throws Exception {
        String m = "8=FIX.4.2\0019=53\00135=A\00190=4\00191=ABCD\001"
                + "98=0\001384=2\001372=D\001385=R\001372=8\001385=S\00110=241\001";
        FixMessageUnmarshaller unmarshaller = new FixMessageUnmarshaller();
        FIXMessage msg = unmarshaller.toFIXMessage(m.getBytes(), new StandardFIXMessage());
    }

    public void testToFIXMessageHeader() throws Exception {

    }
}
