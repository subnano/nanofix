package net.nanofix.message;

import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.ByteString;
import net.nanofix.util.FIXBytes;

import java.nio.ByteBuffer;

/**
 **/
public class FixMessageAssembler implements MessageAssembler {

    // possibly use an interface for the buffers !!??
    protected final ByteBuffer buffer;

    public FixMessageAssembler(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void addBooleanField(int tag, boolean value) {
        buffer.put(ByteArrayUtil.asByteArray(tag));
        buffer.put(FIXBytes.EQUALS);
        buffer.put(value ? FIXBytes.FIX_TRUE : FIXBytes.FIX_FALSE);
        buffer.put(FIXBytes.SOH);
    }

    @Override
    public void addIntField(int tag, int value) {
        addBytesField(tag, ByteArrayUtil.asByteArray(value));
    }

    @Override
    public void addLongField(int tag, long value) {
        addBytesField(tag, ByteArrayUtil.asByteArray(value));
    }

    @Override
    public void addStringField(int tag, ByteString value) {
        addBytesField(tag, value.bytes());
    }

    @Override
    public void addBytesField(int tag, byte[] bytes) {
        addBytesWithDelimiters(buffer, ByteArrayUtil.asByteArray(tag), bytes);
    }

    protected void addBytesWithDelimiters(ByteBuffer buffer, byte[] tagAsBytes, byte[] bytes) {
        buffer.put(tagAsBytes);
        buffer.put(FIXBytes.EQUALS);
        buffer.put(bytes);
        buffer.put(FIXBytes.SOH);
    }
}
