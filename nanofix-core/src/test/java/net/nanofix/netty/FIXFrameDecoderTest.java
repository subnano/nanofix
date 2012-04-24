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

    private static final String M1 = "8=FIX.4.4\u00019=74\u000135=A\u000149=FIXLDNMD1\u000156=COBAFXMD\u000134=1\u000152=20111012-16:30:59\u0001108=30\u000198=0\u0001141=Y\u000110=043\u0001";
    private static final String M2 = "8=FIX.4.4\u00019=80\u000135=x\u000134=2\u000156=COBAFXMD\u000149=FIXLDNMD1\u000152=20111012-16:31:00\u0001320=ID12163100896\u0001559=4\u000110=183\u0001";
    private static final String M3_1 = "8=FIX.4.4\u00019=98\u000135=c\u000134=3\u000156=COBAFXMD\u000149=FIX";
    private static final String M3_2 = "LDNMD1\u000152=20111012-16:31:01\u0001320=ID12163101443\u0001321=0\u000155=AUD/CAD\u0001541=TN\u000110=132\u0001";

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
        embedder.offer(wrappedBuffer("8=FIXT.1.1\0019=12\00135=X\001108=30\00110=049\001".getBytes()));
        embedder.poll();
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
            assertThat(e.getCause().getClass(), is(CorruptedFrameException.class));
        }

    }

}
