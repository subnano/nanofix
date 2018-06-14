package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;

import java.nio.ByteBuffer;

class LoggingDecodeHandler implements MessageDecodeHandler {

    @Override
    public void onTag(ByteBuffer buffer, int tagIndex, int tagLen, int valueLen) {
        int valueIndex = tagIndex + tagLen + 1;
        System.out.println(String.format(
                "onTag: tagIndex=%d tagLen=%d (%s) valueIndex=%d valueLen=%d (%s)",
                tagIndex,
                tagLen,
                new String(ByteBufferUtil.asByteArray(buffer, tagIndex, tagLen)),
                valueIndex,
                valueLen,
                new String(ByteBufferUtil.asByteArray(buffer, valueIndex, valueLen))
        ));
    }

    @Override
    public void onError(int index, String message) {
        System.out.println(String.format(
                "onError: index=%d message=%s",
                index,
                message
        ));
    }
}
