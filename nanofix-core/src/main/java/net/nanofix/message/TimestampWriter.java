package net.nanofix.message;

import java.nio.ByteBuffer;
import java.util.function.ObjLongConsumer;

public interface TimestampWriter extends ObjLongConsumer<ByteBuffer> {

    @Override
    void accept(ByteBuffer byteBuffer, long value);

}
