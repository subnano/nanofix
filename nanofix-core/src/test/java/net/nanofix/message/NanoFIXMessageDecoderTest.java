package net.nanofix.message;

import net.nanofix.settings.SessionSettingsBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class NanoFIXMessageDecoderTest {

    private MessageStringBuilder decodeHandler = new MessageStringBuilder();
    private NanoFIXMessageDecoder decoder = new NanoFIXMessageDecoder();
    private ByteBuffer buffer =ByteBuffer.allocate(1024);

    @Test
    void junkAtStart() {
    }

    @Test
    void beginStringMissing() {
    }

    @Test
    void beginStringInvalid() {
    }

    @Test
    void bodyLengthMissing() {
    }

    @Test
    void bodyLengthTooShort() {
    }

    @Test
    void bodyLengthTooLong() {
    }

    @Test
    void bodyLengthInvalidData() {
    }

    @Test
    void bodyLengthIncorrect() {
    }

    @Test
    void msgTypeMissing() {
    }

    @Test
    void checksumMissing() {
    }

    @Test
    void checksumIncorrect() {
    }

    @Test
    void decodeHeartbeat() {
        String msgText = "8=FIX.4.3|9=72|35=0|49=CLIENT|56=BROKER|34=11|52=19700101-00:00:00.000|112=test-req-id|10=109|";
        //assertMessageMatches(msgText);
        prepareBuffer(msgText);
        decoder.decode(buffer, decodeHandler);
        Assertions.assertThat(decodeHandler.asString()).isEqualTo(msgText);
    }

    private void prepareBuffer(String msgText) {

    }

    @Test
    void decodeLogon() {
    }

    @Test
    void decodeMultipleMessages() {
    }
}