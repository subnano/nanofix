package net.nanofix.message;

import java.nio.ByteBuffer;

public interface ByteWriter {
    void apply(ByteBuffer buffer);
}
