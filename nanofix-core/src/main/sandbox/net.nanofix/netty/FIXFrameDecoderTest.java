package net.nanofix.netty;

import com.google.common.base.Stopwatch;
import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.TestHelper;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.embedder.CodecEmbedderException;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import static org.hamcrest.core.Is.*;

import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.jboss.netty.buffer.ChannelBuffers.wrappedBuffer;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * User: Mark
 * Date: 13/10/11
 * Time: 04:37
 */
public class FIXFrameDecoderTest {

    private static final int TEST_LOOPS = 10 * 1000 * 1000;

    private DecoderEmbedder<ChannelBuffer> decoder;

    private static final String M1 = "8=FIX.4.4\u00019=132\u000135=D\u000134=4\u000149=BANZAI\u000152=20120331-10:26:33.264\u000156=EXEC\u000111=1333189593005\u000121=1\u000138=100\u000140=1\u000154=1\u000155=GOOG.N\u000159=0\u000160=20120331-10:26:33.257\u000110=219\u0001";
    private static final String M2 = "8=FIX.4.4\u00019=123\u000135=8\u000134=4\u000149=EXEC\u000152=20120331-10:26:33.507\u000156=BANZAI\u00016=0\u000111=1333189593005\u000114=0\u000117=1\u000137=1\u000139=0\u000154=1\u000155=GOOG.N\u0001150=2\u0001151=100\u000110=181\u0001";
    private static final String M3_1 = "8=FIX.4.4\u00019=132\u000135=D\u000134=4\u000149=BANZAI\u000152=20120331-";
    private static final String M3_2 = "10:26:33.264\u000156=EXEC\u000111=1333189593005\u000121=1\u000138=100\u000140=1\u000154=1\u000155=GOOG.N\u000159=0\u000160=20120331-10:26:33.257\u000110=219\u0001";

    @Before
    public void setUp() {
        decoder = new DecoderEmbedder<ChannelBuffer>(new FIXFrameDecoder());
    }

    @Test
    public void testValidMessage() {
        decoder.offer(wrappedBuffer(M1.getBytes()));
        assertThat(decoder.poll(), is(wrappedBuffer(M1.getBytes())));
    }

    @Test
    public void testJoinedMessage() {
        decoder.offer(wrappedBuffer((M1 + M2).getBytes()));
        assertThat(decoder.poll(), is(wrappedBuffer(M1.getBytes())));
        assertThat(decoder.poll(), is(wrappedBuffer(M2.getBytes())));
    }

    @Test
    public void testSplitMessage() {
        decoder.offer(wrappedBuffer((M3_1).getBytes()));
        assertThat(decoder.poll(), is(isNull()));
        decoder.offer(wrappedBuffer((M3_2).getBytes()));
        assertThat(decoder.poll(), is(wrappedBuffer((M3_1 + M3_2).getBytes())));
    }

    @Test(expected = CodecEmbedderException.class)
    public void testInvalidBeginString() {
        decoder.offer(wrappedBuffer("8=ABC.4.2\u00019=24\u000135=x".getBytes()));
        fail("expected CorruptedFrameException");
        decoder.poll();
    }

    @Test
    public void testFIXTBeginString() {
//        embedder.offer(wrappedBuffer("8=FIXT.1.1\0019=12\00135=X\001108=30\00110=049\001".getBytes()));
//        embedder.poll();
    }

    @Test
    public void testMissingLength() {
        String msgText = "8=FIX.4.4\u000135=A\u0001";
        try {
            decoder.offer(wrappedBuffer(msgText.getBytes()));
            decoder.poll();
        } catch(CodecEmbedderException e) {
            assertThat(e.getCause(), is(ProtocolException.class));
        }
    }

    @Test
    public void testZeroBodyLength() {
        String msgText = "8=FIX.4.4\u00019=0\u000135=A\u0001";
        try {
            decoder.offer(wrappedBuffer(msgText.getBytes()));
            decoder.poll();
        } catch(CodecEmbedderException e) {
            assertThat(e.getCause(), is(CorruptedFrameException.class));
        }
    }

    @Test
    public void testJunkBodyLength() {
        String badMessage = "8=FIX.4.2\0019=1a2b3\00135=X\001108=30\00110=036\001";
        try {
            decoder.offer(wrappedBuffer(badMessage.getBytes()));
            decoder.poll();
        } catch (CodecEmbedderException e) {
            assertThat(e.getCause(), is(CorruptedFrameException.class));
        }
    }

    @Test
    public void testIncorrectBodyLength() {
        String msgText = "8=FIX.4.2\0019=123\00135=X\001108=30\00110=036\001";
        try {
            decoder.offer(wrappedBuffer(msgText.getBytes()));
            assertThat(decoder.poll(), is(wrappedBuffer(msgText.getBytes())));
        } catch (CodecEmbedderException e) {
            assertThat(e.getCause(), is(CorruptedFrameException.class));
        }
    }

    @Test
    public void testEmptyArray() {
        try {
            decoder.offer(wrappedBuffer(new byte[1024]));
        } catch(CodecEmbedderException e) {
            assertThat(e.getCause(), is(CorruptedFrameException.class));
        }
    }

    @Test
    public void testInvalidCheckSum() {
        try {
            String msgText = "8=FIX.4.4\u00019=64\u000135=A\u000149=CLIENT\u000156=BROKER\u000134=-1\u000152=20120501-15:37:32\u000198=0\u0001108=30\u000110=227";
            System.out.println("decoding: " + msgText);
            DecoderEmbedder decoder = new DecoderEmbedder<ChannelBuffer>(new FIXFrameDecoder());
            int checksum = FIXFrameDecoder.calcChecksum(msgText.getBytes());
            System.out.println("checksum = " + checksum);

            decoder.offer(wrappedBuffer(new byte[1024]));
        } catch(CodecEmbedderException e) {
            assertThat(e.getCause(), is(CorruptedFrameException.class));
        }
    }

    @Test
    public void testParseIntPerformance() {
        ChannelBuffer buffer = wrappedBuffer("1234".getBytes());
        Stopwatch stopwatch = new Stopwatch().start();
        for (int i=0; i<=TEST_LOOPS; i++) {
            final String lengthStr = buffer.slice(0, 4).toString(Charset.forName("US-ASCII"));
            int bodyLength = Integer.parseInt(lengthStr);
            if (bodyLength != 1234) {
                throw new RuntimeException("Incorrect BodyLength(" + bodyLength + ")");
            }
        }
        TestHelper.printResults("parseInt", TEST_LOOPS, stopwatch.elapsedMillis());
    }

    @Test
    public void testIntegerUtilPerformance() {
        ChannelBuffer buffer = wrappedBuffer("1234".getBytes());
        Stopwatch stopwatch = new Stopwatch().start();
        for (int i=0; i<=TEST_LOOPS; i++) {
            int bodyLength = ByteArrayUtil.toInteger(buffer.array(), buffer.readerIndex(), buffer.readableBytes());
            if (bodyLength != 1234) {
                throw new RuntimeException("Incorrect BodyLength(" + bodyLength + ")");
            }
        }
        TestHelper.printResults("ByteArrayUtil.toInteger", TEST_LOOPS, stopwatch.elapsedMillis());
    }

}
