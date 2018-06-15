package net.nanofix.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

class UtcDateTimeEncoderTest {

    private static final int TIMESTAMP_LENGTH = 21;

    private UtcDateTimeEncoder encoder;
    private ByteBuffer buffer;
    private DateFormat formatter;

    @BeforeEach
    void setUp() {
        encoder = new UtcDateTimeEncoder();
        buffer = ByteBuffer.allocate(32);
        formatter = new SimpleDateFormat("yyyyMMdd-HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void encodeEpoch() throws Exception {
        assertEncode("19700101-00:00:00.000");
    }

    @Test
    void encodeMillenium() throws Exception {
        assertEncode("20000101-00:00:00.001");
    }

    @Test
    void encodeLeapYear() throws Exception {
        assertEncode("20200229-14:56:12.987");
    }

    private void assertEncode(String expected) throws Exception {
        long timeAsMillis = formatter.parse(expected).getTime();
        encoder.encode(timeAsMillis, buffer, 0);
        String actualString = new String(buffer.array(), 0, TIMESTAMP_LENGTH, StandardCharsets.US_ASCII);
        Assertions.assertThat(actualString).isEqualTo(expected);
    }
}