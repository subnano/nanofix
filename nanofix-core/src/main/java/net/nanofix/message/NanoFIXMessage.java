package net.nanofix.message;

import io.netty.buffer.ByteBuf;
import net.nanofix.netty.BufferUtil;
import net.nanofix.util.ByteScanner;
import net.nanofix.util.FIXBytes;

import java.nio.ByteBuffer;

/**
 * Two cases:
 * - socket notify then read into a single buffer
 * - compose message then build as write
 */
public class NanoFIXMessage extends FixMessageAssembler implements FIXMessage {

    private final MessageHeader header;
    private final ByteBuffer[] buffers;

    public NanoFIXMessage(MessageHeader header, ByteBuffer buffer) {
        super(buffer);
        this.header = header;
        this.buffers = new ByteBuffer[]{null, null, buffer};
    }

    @Override
    public MessageHeader header() {
        return header;
    }

    public static FIXMessage decodeToMsgType(ByteBuf buffer, int start, int length) {
        int tagIndex = ByteScanner.nextTagIndex(buffer, start);
        int tagLen = ByteScanner.tagLen(buffer, start);
        int valueIndex = ByteScanner.nextValueIndex(buffer, start);
        int valueLen = ByteScanner.valueLen(buffer, valueIndex);
        int tag = BufferUtil.toInteger(buffer, tagIndex, tagLen);
        boolean foundMsgType = false;
        do {
            // only want as far as MsgType
            if (tag == Tags.BeginString) {
                // do stuff with BeginString
            } else if (tag == Tags.BodyLength) {
                // do stuff with BodyLength
            } else if (tag == Tags.MsgType) {
                // do stuff with MsgType
                foundMsgType = true;
            } else {
                break;
            }
        } while (!foundMsgType);
        return null;
    }

    @Override
    public MsgType msgType() {
        return header.msgType();
    }

    @Override
    public ByteBuffer[] buffers() {
        appendTrailerIfNecessary();

        // set the main header buffer
        buffers[0] = header.beginBuffer();
        buffers[1] = header.headerBuffer();

        // now calculate the combined length
        int bodyLength = ByteBufferUtil.readableBytes(buffers[1], buffer);
        header.bodyLength(bodyLength);


        // update the checksum
        // TODO add trailer / checksum
        return buffers;
    }

    /**
     * Append the checksum field if not already present.
     * Does not calculate the checksum at this point as body length will not have been set.
     */
    private void appendTrailerIfNecessary() {
        if (!ByteBufferUtil.hasChecksum(buffer)) {
            addBytesField(Tags.CheckSum, FIXBytes.CHECKSUM_PLACEHOLDER);
        }
    }

}
