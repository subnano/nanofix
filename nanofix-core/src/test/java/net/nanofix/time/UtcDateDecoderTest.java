package net.nanofix.time;

import io.nano.core.buffer.ByteBufferUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

class UtcDateDecoderTest {

    private ByteBuffer buffer;
    private DateFormat formatter;

    @BeforeEach
    void setUp() {
        buffer = ByteBuffer.allocate(16);
        formatter = new SimpleDateFormat("yyyyMMdd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void decodeEpoch() throws Exception {
        assertDecode("19700101");
    }

    @Test
    void decodeLeapYear() throws Exception {
        assertDecode("20200229");
    }

    @Test
    void decodeChristmas() throws Exception {
        assertDecode("20151225");
    }

    private void assertDecode(String expected) throws Exception {
        long expectedMillis = formatter.parse(expected).getTime();
        buffer.clear();
        buffer.put(expected.getBytes(StandardCharsets.US_ASCII), 0, expected.length());
        long epochMillis = UtcDateDecoder.decode(buffer, 0);
        Assertions.assertThat(epochMillis).isEqualTo(expectedMillis);
    }

}