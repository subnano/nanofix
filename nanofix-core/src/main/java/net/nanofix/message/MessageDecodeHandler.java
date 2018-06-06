package net.nanofix.message;

import java.nio.ByteBuffer;

public interface MessageDecodeHandler {

    /**
     * Callback as each tag value pair is iterated over in buffer.
     *
     * It can be assumed that valueIndex will be tagIndex + tagLen + 1
     */
    void onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen);

    void onError(int index, String message);
}
