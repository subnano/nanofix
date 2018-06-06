package net.nanofix.message;

import java.nio.ByteBuffer;

public interface FIXMessageVisitor {

    /**
     * Callback as each tag value pair is iterated over in buffer.
     *
     * It can be assumed that startOfValueIndex will be startOfTagIndex + len + 1
     */
    void onTag(ByteBuffer buffer, int startOfTagIndex, int tagLen, int valueLen);

    void onError(int index, String message);
}
