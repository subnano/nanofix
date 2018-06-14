package net.nanofix.time;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

class UtcTimeDecoderTest {

    private ByteBuffer buffer;
    private DateFormat formatter;

    @BeforeEach
    void setUp() {
        buffer = ByteBuffer.allocate(16);
        formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void decode() throws Exception {
        assertDecode("00:00:00.000");
        assertDecode("09:59:59.999");
        assertDecode("01:02:03.004");
    }

    private void assertDecode(String expected) throws Exception {
        long expectedMillis = formatter.parse(expected).getTime();
        buffer.clear();
        buffer.put(expected.getBytes(StandardCharsets.US_ASCII), 0, expected.length());
        long epochMillis = UtcTimeDecoder.decode(buffer, 0);
        Assertions.assertThat(epochMillis).isEqualTo(expectedMillis);
    }

}
