package net.nanofix.message;

import io.netty.buffer.ByteBuf;
import net.nanofix.message.util.ChecksumCalculator;
import net.nanofix.netty.BufferUtil;
import net.nanofix.util.ByteScanner;

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
        this.buffers = new ByteBuffer[]{header.beginBuffer(), header.headerBuffer(), buffer};
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

        // calculate just the length of the body - no  headers
        int bodyLength = ByteBufferUtil.readableBytes(buffer);
        header.bodyLength(bodyLength);
        header.populateBuffer();

        // update the checksum
        byte[] checksumBytes = ChecksumCalculator.calculateChecksum(buffers);
        addBytesField(Tags.CheckSum, checksumBytes);
        return buffers;
    }

}
