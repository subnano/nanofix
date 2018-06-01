package net.nanofix.message;

import net.nanofix.util.ByteString;

public interface MessageAssembler {

    void addBooleanField(int tag, boolean value);

    void addIntField(int tag, int value);

    void addLongField(int tag, long value);

    void addStringField(int tag, ByteString value);

    void addBytesField(int tag, byte[] bytes);

}
