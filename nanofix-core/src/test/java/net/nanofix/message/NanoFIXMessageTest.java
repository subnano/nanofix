package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.ByteString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static net.nanofix.util.FIXBytes.PIPE;
import static net.nanofix.util.FIXBytes.SOH;

class NanoFIXMessageTest {

    private static final ByteString SENDER_COMP_ID = ByteString.of("CLIENT");
    private static final ByteString TARGET_COMP_ID = ByteString.of("BROKER");

    private final MessageHeader header = new MessageHeader(ByteBuffer.allocate(256));
    private final ByteBuffer buffer = ByteBuffer.allocate(256);
    private final FIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private MessageDecodeHandler visitor = new LoggingDecodeHandler();

    @BeforeEach
    void setUp() {
        buffer.clear();
    }

    @Test
    void logonMessage() {
        FIXMessage msg = new NanoFIXMessage(header, buffer);
        msg.header().beginString(BeginStrings.FIX_4_2);
        msg.header().msgType(MsgTypes.Logon);
        msg.header().senderCompId(SENDER_COMP_ID);
        msg.header().targetCompId(TARGET_COMP_ID);
        msg.header().msgSeqNum(42);
        msg.header().sendingTime(0);
        msg.addIntField(Tags.EncryptMethod, 0);
        msg.addIntField(Tags.HeartBtInt, 30);
        msg.addBooleanField(Tags.ResetSeqNumFlag, true);
        msg.addStringField(Tags.Username, ByteString.of("user1"));
        byte[] bytes = ByteBufferUtil.asByteArray(msg.buffers());

//        TagVisitor visitor = new LocalTagVisitor();
        assertByteArray(
                "8=FIX.4.2|9=84|35=A|49=CLIENT|56=BROKER|34=42|52=19700101-00:00:00.000|98=0|" +
                        "108=30|141=Y|553=user1|10=148|",
                bytes);
    }

    @Test
    void heartbeatMessage() {
        FIXMessage msg = new NanoFIXMessage(header, buffer);
        msg.header().beginString(BeginStrings.FIX_4_3);
        msg.header().msgType(MsgTypes.Heartbeat);
        msg.header().senderCompId(SENDER_COMP_ID);
        msg.header().targetCompId(TARGET_COMP_ID);
        msg.header().msgSeqNum(11);
        msg.addStringField(Tags.TestReqID, ByteString.of("test-req-id"));
        byte[] bytes = ByteBufferUtil.asByteArray(msg.buffers());

//        TagVisitor visitor = new LocalTagVisitor();
        assertByteArray(
                "8=FIX.4.3|9=72|35=0|49=CLIENT|56=BROKER|34=11|52=19700101-00:00:00.000|" +
                        "112=test-req-id|10=109|",
                bytes);
    }

    private void assertByteArray(String expected, byte[] actualBytes) {
        ByteBuffer buffer = ByteBuffer.wrap(actualBytes);
        decoder.decode(buffer, visitor);
        ByteArrayUtil.replace(actualBytes, SOH, PIPE);
        String actualBytesText = new String(actualBytes, StandardCharsets.US_ASCII);
        Assertions.assertEquals(expected, actualBytesText);
    }

}