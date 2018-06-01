package net.nanofix.netty;

import net.nanofix.message.*;
import org.hamcrest.text.StringContains;
import org.hamcrest.text.StringStartsWith;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.CompositeChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.embedder.DecoderEmbedder;
import org.jboss.netty.handler.codec.embedder.EncoderEmbedder;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.jboss.netty.buffer.ChannelBuffers.buffer;
import static org.jboss.netty.buffer.ChannelBuffers.wrappedBuffer;
import static org.junit.Assert.assertThat;

/**
 * User: Mark
 * Date: 29/04/12
 * Time: 19:17
 */
public class FIXMessageEncoderTest {

    //private static final String M1 = "8=FIX.4.4\u00019=132\u000135=D\u000134=4\u000149=BANZAI\u000152=20120331-10:26:33.264\u000156=EXEC\u000111=1333189593005\u000121=1\u000138=100\u000140=1\u000154=1\u000155=GOOG.N\u000159=0\u000160=20120331-10:26:33.257\u000110=219\u0001";

    private static final FIXMessageFactory messageFactory = new DefaultFIXMessageFactory();

    @Test
    public void encodeMessageTest() {
        FIXMessage msg = messageFactory.createMessage(MsgTypes.Logon);
        msg.setMsgSeqNum(123);
        msg.setFieldValue(Tags.SenderCompID, "SENDER");
        msg.setFieldValue(Tags.TargetCompID, "TARGET");
        msg.setFieldValue(Tags.HeartBtInt, 30);
        EncoderEmbedder<ChannelBuffer> embedder = new EncoderEmbedder<ChannelBuffer>(new FIXMessageEncoder("FIX.4.2"));
        embedder.offer(msg);
        ChannelBuffer buffer = embedder.poll();
        assertThat(buffer, is(CompositeChannelBuffer.class));

        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes);
        String msgText = new String(bytes);

        // we know that we get this out
        String expectedText = "8=FIX.4.2\u00019=32\u000135=A\u000149=SENDER\u000156=TARGET\u0001108=30\u000110=248\u0001";
        assertThat(msgText, allOf(new StringStartsWith("8=FIX.4.2")));
        assertThat(msgText, allOf(new StringContains("9=32")));
        assertThat(msgText, allOf(new StringContains("35=A")));
        assertThat(msgText, allOf(new StringContains("49=SENDER")));
        assertThat(msgText, allOf(new StringContains("56=TARGET")));
        assertThat(msgText, allOf(new StringContains("108=30")));

        // assert checksum
        String checksumText = expectedText.substring(expectedText.length() - 4, expectedText.length() - 1);
        int actualChecksum = Integer.parseInt(checksumText);
        byte[] newBytes = expectedText.substring(0, expectedText.length() - 7).getBytes();
        int sum = 0;
        for (int i=0; i<newBytes.length; i++) {
            sum += (int) newBytes[i];
        }
        int checksum = sum % 256;
        assertThat("checksum incorrect", actualChecksum, is(checksum));
    }

}
