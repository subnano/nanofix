package net.nanofix.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.embedder.CodecEmbedderException;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import static org.hamcrest.core.Is.*;

import org.jboss.netty.handler.codec.frame.CorruptedFrameException;
import org.junit.Before;
import org.junit.Test;

import static org.jboss.netty.buffer.ChannelBuffers.wrappedBuffer;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * User: Mark Wardell
 * Date: 13/10/11
 * Time: 04:37
 */
public class FIXFrameDecoderTest {

    private DecoderEmbedder<ChannelBuffer> embedder;

    private static final String M1 = "8=FIX.4.4\u00019=132\u000135=D\u000134=4\u000149=BANZAI\u000152=20120331-10:26:33.264\u000156=EXEC\u000111=1333189593005\u000121=1\u000138=100\u000140=1\u000154=1\u000155=GOOG.N\u000159=0\u000160=20120331-10:26:33.257\u000110=219\u0001";
    private static final String M2 = "8=FIX.4.4\u00019=123\u000135=8\u000134=4\u000149=EXEC\u000152=20120331-10:26:33.507\u000156=BANZAI\u00016=0\u000111=1333189593005\u000114=0\u000117=1\u000137=1\u000139=0\u000154=1\u000155=GOOG.N\u0001150=2\u0001151=100\u000110=181\u0001";
    private static final String M3_1 = "8=FIX.4.4\u00019=132\u000135=D\u000134=4\u000149=BANZAI\u000152=20120331-";
    private static final String M3_2 = "10:26:33.264\u000156=EXEC\u000111=1333189593005\u000121=1\u000138=100\u000140=1\u000154=1\u000155=GOOG.N\u000159=0\u000160=20120331-10:26:33.257\u000110=219\u0001";

    @Before
    public void setUp() {
        embedder = new DecoderEmbedder<ChannelBuffer>(new FIXFrameDecoder());
    }

    @Test
    public void testValidMessage() {
        System.out.println("=== testValidMessage ===");
        embedder.offer(wrappedBuffer(M1.getBytes()));
        assertThat(embedder.poll(), is(wrappedBuffer(M1.getBytes())));
    }

    @Test
    public void testJoinedMessage() {
        System.out.println("=== testJoinedMessage ===");
        embedder.offer(wrappedBuffer((M1 + M2).getBytes()));
        assertThat(embedder.poll(), is(wrappedBuffer(M1.getBytes())));
        assertThat(embedder.poll(), is(wrappedBuffer(M2.getBytes())));
    }

    @Test
    public void testSplitMessage() {
        System.out.println("=== testSplitMessage ===");
        embedder.offer(wrappedBuffer((M3_1).getBytes()));
        assertThat(embedder.poll(), is(isNull()));
        embedder.offer(wrappedBuffer((M3_2).getBytes()));
        assertThat(embedder.poll(), is(wrappedBuffer((M3_1+M3_2).getBytes())));
    }

    @Test(expected = CodecEmbedderException.class)
    public void testInvalidBeginString() {
        System.out.println("=== testInvalidBeginString ===");
        embedder.offer(wrappedBuffer("8=ABC.4.2\u00019=24\u000135=x".getBytes()));
        fail("expected CorruptedFrameException");
        embedder.poll();
    }

    @Test
    public void testFIXTBeginString() {
        System.out.println("=== testFIXTBeginString ===");
//        embedder.offer(wrappedBuffer("8=FIXT.1.1\0019=12\00135=X\001108=30\00110=049\001".getBytes()));
//        embedder.poll();
    }

    @Test
    public void testMissingLength() {
    }

    @Test
    public void testInvalidLength() {
    }

    @Test
    public void testEmptyArray() {
        try {
            embedder.offer(wrappedBuffer(new byte[1024]));
        } catch(CodecEmbedderException e) {
            assertThat(e.getCause(), is(CorruptedFrameException.class));
        }

    }

}
