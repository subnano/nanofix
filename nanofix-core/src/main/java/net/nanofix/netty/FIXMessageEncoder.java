package net.nanofix.netty;

import com.google.common.base.Strings;
import net.nanofix.message.FIXConstants;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.MessageException;
import net.nanofix.message.Tags;
import net.nanofix.util.IntegerUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

/**
 * User: Mark
 * Date: 27/04/12
 * Time: 17:28
 */
public class FIXMessageEncoder extends OneToOneEncoder {

    protected Logger LOG = LoggerFactory.getLogger(FIXMessageEncoder.class);

    private final AtomicReference<ChannelBuffer> channelBuffer = new AtomicReference<ChannelBuffer>();
    private final String beginString;
    private final byte[] beginStringBytes;

    public FIXMessageEncoder(String beginString) {
        this.beginString = beginString;
        beginStringBytes = beginString.getBytes();
    }

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof FIXMessage) {
            return encodeMessage((FIXMessage) msg, getChannelBuffer(channel));
        }
        return null;
    }

    private Object encodeMessage(FIXMessage msg, ChannelBuffer buffer) {

        // BeginString
        writeField(buffer, Tags.BeginString, beginStringBytes);

        // BodyLength
        buffer.writeBytes(IntegerUtil.asByteArray(Tags.BodyLength));
        buffer.writeByte(FIXConstants.EQUALS);
        int bodyLengthIndex = buffer.writerIndex();
        writeFieldValue(buffer, 0);  // will update BodyLength later
        buffer.writeByte(FIXConstants.SOH);
        int bodyIndex = buffer.writerIndex();

        // MsgType
        writeField(buffer, Tags.MsgType, msg.getMsgType());

        // fields
        encodeMessageFields(msg, buffer);

        // CheckSum
        buffer.writeBytes(IntegerUtil.asByteArray(Tags.CheckSum));
        buffer.writeByte(FIXConstants.EQUALS);
        buffer.writeBytes(IntegerUtil.as3ByteArray(
                calcChecksumAsInt(buffer, bodyIndex, buffer.writerIndex())));
        buffer.writeByte(FIXConstants.SOH);

        // go back and update BodyLength
        //buffer.setInt(bodyLengthIndex, calcBodyLength(buffer, bodyIndex));

        LOG.debug("encode({})", new String(buffer.array()));
        return buffer;
    }

    private int calcBodyLength(ChannelBuffer buffer, int bodyIndex) {
        return 123;
    }

    protected static int calcChecksumAsInt(ChannelBuffer buffer, int startIndex, int endIndex) {
        int checksum = 0;
        for (int i = startIndex; i < endIndex; i++) {
            checksum += buffer.getChar(i);
        }
        return (checksum + 1) % 256;
    }

    private void encodeMessageFields(FIXMessage msg, ChannelBuffer buffer) {
        for (int tag : msg.getTags()) {
            LOG.debug("> tag={}, value={}", tag, msg.getFieldValue(tag));
            writeField(buffer, tag, msg.getFieldValue(tag));
        }
    }

    private void writeFieldValue(ChannelBuffer buffer, Object value) {
        if (value instanceof byte[]) {
            buffer.writeBytes((byte[]) value);
        }
        else if (value instanceof String) {
            writeFieldValue(buffer, (String) value);
        }
        else if (value instanceof Integer) {
            writeFieldValue(buffer, (Integer) value);
        }
        else if (value instanceof Long) {
            writeFieldValue(buffer, (Long) value);
        }
        else if (value instanceof Boolean) {
            writeFieldValue(buffer, (Boolean) value);
        }
        else {
            buffer.writeBytes(value.toString().getBytes());
        }
    }

    private void writeField(ChannelBuffer buffer, int tag, Object value) {
        buffer.writeBytes(IntegerUtil.asByteArray(tag));
        buffer.writeByte(FIXConstants.EQUALS);
        writeFieldValue(buffer, value);
        buffer.writeByte(FIXConstants.SOH);
    }

    private void writeFieldValue(ChannelBuffer buffer, String value) {
        buffer.writeBytes(value.getBytes());
    }

    private void writeFieldValue(ChannelBuffer buffer, Integer value) {
        buffer.writeBytes(IntegerUtil.asByteArray(value));
    }

    private void writeFieldValue(ChannelBuffer buffer, Long value) {
        buffer.writeLong(value);
    }

    private void writeFieldValue(ChannelBuffer buffer, Boolean value) {
        buffer.writeChar(value ? FIXConstants.FIX_TRUE : FIXConstants.FIX_FALSE);
    }

    private String encodeAsString(FIXMessage msg) {
        StringBuilder sb = new StringBuilder(100);
        // msg type
        sb.append(Tags.MsgType)
                .append("=")
                .append(msg.getMsgType())
                .append(getSOHAsString());

        for (int tag : msg.getTags()) {
            LOG.debug("tag={}, value={}", tag, msg.getFieldValue(tag));
            sb.append(tag)
                    .append("=")
                    .append(msg.getFieldValue(tag))
                    .append(getSOHAsString());
        }
        return sb.toString();
    }

    private String getSOHAsString() {
        return new String(new byte[]{ FIXConstants.SOH });
    }

    private ChannelBuffer getChannelBuffer(Channel channel) throws Exception {
        ChannelBuffer buffer = channelBuffer.get();
        if (buffer == null) {
            buffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
            if (!channelBuffer.compareAndSet(null, buffer)) {
                buffer = channelBuffer.get();
            }
        }
        return buffer;
    }
}
