package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class NanoFIXMessageDecoderTest {

    private MessageStringBuilder stringBuilder = new MessageStringBuilder();
    private NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    private MessageDecodeHandler decodeHandler;

    @BeforeEach
    void setUp() {
        decodeHandler = spy(new LoggingDecodeHandler());
    }

    @Test
    void junkAtStart() {
    }

    @Test
    void beginStringMissing() {
        prepareBuffer("9=12|35=A");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(0, "Message must start with with the correct begin string 8=FIX.");
    }

    @Test
    void beginStringInvalid() {
        prepareBuffer("8=NIX.4.0|9=12|35=A");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(0, "Message must start with with the correct begin string 8=FIX.");
    }

    @Test
    void bodyLengthMissing() {
        prepareBuffer("8=FIX.4.1|35=A|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(10, "BodyLength(9) must be the second field in the message");
    }

    @Test
    void bodyLengthTooShort() {
        prepareBuffer("8=FIX.4.1|9=1|35=A|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(10, "BodyLength(9) value is invalid");
    }

    @Test
    void bodyLengthTooLong() {
        prepareBuffer("8=FIX.4.1|9=999888777666|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(10, "BodyLength(9) value is invalid");
    }

    @Test
    @Disabled("May need to change the length validation for this test to pass")
    void bodyLengthInvalidData() {
        prepareBuffer("8=FIX.4.1|9=1x|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(10, "BodyLength(9) should be the second field in the message");
    }

    @Test
    void bodyLengthIncorrect() {
        prepareBuffer("8=FIX.4.1|9=18|35=A|49=ACME|");
        decoder.decode(buffer, decodeHandler);
//        verify(decodeHandler)
//                .onError(10,"BodyLength(9) value is invalid");
    }

    @Test
    void msgTypeMissing() {
        prepareBuffer("8=FIX.4.1|9=24|49=ACME|");
        decoder.decode(buffer, decodeHandler);
        verify(decodeHandler)
                .onError(15, "MsgType(35) must be the third field in the message");
    }

    @Test
    void checksumMissing() {
    }

    @Test
    void checksumIncorrect() {
    }

    @Test
    void decodeHeartbeat() {
        String msgText = FIXMessageStrings.HEARTBEAT;
        prepareBuffer(msgText);
        decoder.decode(buffer, stringBuilder);
        Assertions.assertThat(stringBuilder.asString()).isEqualTo(msgText);
    }

    @Test
    void decodeLogon() {
    }

    @Test
    void decodeMultipleMessages() {
    }

    private void prepareBuffer(String msgText) {
        buffer.clear();
        byte[] bytes = FIXMessageStrings.asValidByteArray(msgText);
        ByteBufferUtil.putBytes(buffer, bytes);
    }
}