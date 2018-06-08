package net.nanofix.message;

import net.nanofix.util.ByteString;

import java.util.concurrent.TimeUnit;

public interface MessageAssembler {

    void addBooleanField(int tag, boolean value);

    void addIntField(int tag, int value);

    void addLongField(int tag, long value);

    void addStringField(int tag, ByteString value);

    void addTimestamp(int tag, long timestamp, TimeUnit timeUnit);

    void addBytesField(int tag, byte[] bytes);

}
