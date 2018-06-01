package net.nanofix.message;

import net.nanofix.util.ByteString;
import net.nanofix.util.FIXBytes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

class NanoFIXMessageTest {

    private static final String SOH = new String(new byte[]{FIXBytes.SOH});
    private static final String PIPE = "\\|";
    private static final ByteString SENDER_COMP_ID = ByteString.of("CLIENT");
    private static final ByteString TARGET_COMP_ID = ByteString.of("BROKER");

    private final MessageHeader header = new MessageHeader(ByteBuffer.allocate(256));
    private final ByteBuffer buffer = ByteBuffer.allocate(256);

    @BeforeEach
    void setUp() {
        buffer.clear();
    }

    @Test
    void simpleMessageEncoding() {
        FIXMessage msg = new NanoFIXMessage(header, buffer);
        msg.header().beginString(BeginStrings.FIX_4_2);
        msg.header().msgType(MsgTypes.Logon);
        msg.header().senderCompId(SENDER_COMP_ID);
        msg.header().targetCompId(TARGET_COMP_ID);
        msg.header().msgSeqNum(42);
        msg.addIntField(Tags.EncryptMethod, 0);
        msg.addIntField(Tags.HeartBtInt, 30);
        msg.addBooleanField(Tags.ResetSeqNumFlag, true);
        msg.addStringField(Tags.Username, ByteString.of("user1"));
        byte[] bytes = ByteBufferUtil.combine(msg.buffers());

//        TagVisitor visitor = new LocalTagVisitor();
        assertByteArray(
                "8=FIX.4.2|9=0|35=A|49=CLIENT|56=BROKER|34=42|52=19700101-00:00:00.000|98=0|108=30|141=Y|553=user1|",
                bytes);
    }

    private static void assertByteArray(String expected, byte[] actualBytes) {
        String bytesText = new String(actualBytes, StandardCharsets.US_ASCII);
        String expectedEncoded = expected.replaceAll(PIPE, SOH);
        Assertions.assertEquals(expectedEncoded, bytesText);
    }

}