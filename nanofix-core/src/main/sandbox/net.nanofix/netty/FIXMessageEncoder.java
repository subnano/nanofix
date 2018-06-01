package net.nanofix.netty;

import net.nanofix.message.FIXConstants;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.Tags;
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

//    private final AtomicReference<ChannelBuffer> channelBuffer = new AtomicReference<ChannelBuffer>();
    private final AtomicReference<ChannelBuffer> headerChannelBuffer = new AtomicReference<ChannelBuffer>();
    private final AtomicReference<ChannelBuffer> bodyChannelBuffer = new AtomicReference<ChannelBuffer>();
    private final String beginString;
    private final byte[] beginStringBytes;

    public FIXMessageEncoder(String beginString) {
        this.beginString = beginString;
        beginStringBytes = beginString.getBytes();
    }

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof FIXMessage) {
            return encodeMessage((FIXMessage) msg, channel);
        }
        return null;
    }

    protected Object encodeMessage(FIXMessage msg, Channel channel) {

        // get the header buffer
        ChannelBuffer headerBuffer = getHeaderChannelBuffer(channel);
        headerBuffer.clear();

        // BeginString
        writeField(headerBuffer, Tags.BeginString, beginStringBytes);

        // get the body buffer
        ChannelBuffer bodyBuffer = getBodyChannelBuffer(channel);
        bodyBuffer.clear();

        // MsgType
        writeField(bodyBuffer, Tags.MsgType, msg.getMsgType());

        // fields
        encodeMessageFields(msg, bodyBuffer);

        // go back and update BodyLength
        writeField(headerBuffer, Tags.BodyLength, calcBodyLength(bodyBuffer));

        // CheckSum
        int checkSum = calcChecksum(headerBuffer, bodyBuffer);
        bodyBuffer.writeBytes(ByteArrayConverter.asByteArray(Tags.CheckSum));
        bodyBuffer.writeByte(FIXConstants.EQUALS);
        bodyBuffer.writeBytes(ByteArrayConverter.as3ByteArray(checkSum));
        bodyBuffer.writeByte(FIXConstants.SOH);

        // log the message being written
        logBufferContents(headerBuffer, bodyBuffer);

        // return the composite of the two buffers
        return ChannelBuffers.wrappedBuffer(headerBuffer, bodyBuffer);
    }

    private void logBufferContents(ChannelBuffer headerBuffer, ChannelBuffer bodyBuffer) {
        byte[] bytes = new byte[headerBuffer.readableBytes() + bodyBuffer.readableBytes()];
        headerBuffer.getBytes(headerBuffer.readerIndex(), bytes);
        bodyBuffer.getBytes(bodyBuffer.readerIndex(), bytes, headerBuffer.readableBytes(), bodyBuffer.readableBytes());

        LOG.debug("> {}", new String(bytes));
    }

    private int calcBodyLength(ChannelBuffer buffer) {
        return buffer.readableBytes();
    }

    protected static int calcChecksum(ChannelBuffer headerBuffer, ChannelBuffer bodyBuffer) {
        int checksum = 0;
        int count = 0;
        for (int i = headerBuffer.readerIndex(); i < headerBuffer.readerIndex() + headerBuffer.readableBytes(); i++) {
            checksum += headerBuffer.getByte(i);
            count++;
        }
        for (int i = bodyBuffer.readerIndex(); i < bodyBuffer.readerIndex() + bodyBuffer.readableBytes(); i++) {
            checksum += bodyBuffer.getByte(i);
            count++;
        }
        return checksum % 256;
    }

    private void encodeMessageFields(FIXMessage msg, ChannelBuffer buffer) {
        for (int tag : msg.getTags()) {
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
        buffer.writeBytes(ByteArrayConverter.asByteArray(tag));
        buffer.writeByte(FIXConstants.EQUALS);
        writeFieldValue(buffer, value);
        buffer.writeByte(FIXConstants.SOH);
    }

    private void writeFieldValue(ChannelBuffer buffer, String value) {
        buffer.writeBytes(value.getBytes());
    }

    private void writeFieldValue(ChannelBuffer buffer, Integer value) {
        buffer.writeBytes(ByteArrayConverter.asByteArray(value));
    }

    private void writeFieldValue(ChannelBuffer buffer, Long value) {
        buffer.writeBytes(ByteArrayConverter.asByteArray(value));
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

    private ChannelBuffer getHeaderChannelBuffer(Channel channel) {
        ChannelBuffer buffer = headerChannelBuffer.get();
        if (buffer == null) {
            buffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
            if (!headerChannelBuffer.compareAndSet(null, buffer)) {
                buffer = headerChannelBuffer.get();
            }
        }
        return buffer;
    }

    private ChannelBuffer getBodyChannelBuffer(Channel channel) {
        ChannelBuffer buffer = bodyChannelBuffer.get();
        if (buffer == null) {
            buffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
            if (!bodyChannelBuffer.compareAndSet(null, buffer)) {
                buffer = bodyChannelBuffer.get();
            }
        }
        return buffer;
    }

}
