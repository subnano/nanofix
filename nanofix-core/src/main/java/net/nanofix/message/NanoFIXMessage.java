package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.message.util.ChecksumCalculator;

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
