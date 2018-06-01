package net.nanofix.message;

import java.nio.ByteBuffer;

import static net.nanofix.util.FIXBytes.SOH;

public final class ByteBufferUtil {

    private static byte[] trailerBytePrefix = new byte[] {SOH, '0', '1', '0', '='} ;

    private ByteBufferUtil() {
        // can't touch this
    }

    /**
     * Takes the bytes from the array of given ByteBuffers and combines them
     * into a single byte array.
     * @param buffers the collection of ByteBuffers to combine
     * @return a new byte array
     */
    public static byte[] combine(ByteBuffer[] buffers) {
        int bufferLen = 0;
        for (ByteBuffer buffer : buffers) {
            buffer.flip();  // flip to reading mode
            bufferLen += buffer.remaining();
        }
        byte[] bytes = new byte[bufferLen];
        int offset = 0;
        for (ByteBuffer buffer : buffers) {
            int srcLen = buffer.remaining();
            buffer.get(bytes ,offset, srcLen);
            offset += srcLen;
            buffer.clear();
        }
        return bytes;
    }

    /**
     * ByteBuffer contents for a FIX message should end with |010=nnn| (9 bytes)
     */
    public static boolean hasChecksum(ByteBuffer buffer) {
        int offset = buffer.remaining() - 9;
        return  ByteBufferUtil.equals(buffer, offset, trailerBytePrefix);
    }

    /**
     * Checks whether the bytes in the buffer at position represented by offset
     * are equal to the bytes in the given byte array.
     */
    public static boolean equals(ByteBuffer buffer, int offset, byte[] bytes) {
        for (int pos=0; pos<bytes.length; pos++) {
            if (buffer.get(offset + pos) != bytes[pos])
                return false;
        }
        return true;
    }

    public static int readableBytes(ByteBuffer buffer1, ByteBuffer buffer2) {
        return 0;
    }
}
