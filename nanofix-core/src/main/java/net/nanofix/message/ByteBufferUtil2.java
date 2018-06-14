package net.nanofix.message;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public final class ByteBufferUtil2 {

    private static final byte[] CHECKSUM_BYTE_SUFFIX = new byte[]{'0', '1', '0', '='};
    public static final int NOT_FOUND_INDEX = -1;

    private ByteBufferUtil2() {
        // can't touch this
    }

    public static int indexOf(ByteBuffer buffer, int startIndex, byte value) {
        int toIndex = buffer.position();
        for (int index = startIndex; index < toIndex; index++) {
            if (buffer.get(index) == value) return index;
        }
        return NOT_FOUND_INDEX;
    }

    /**
     * Takes the bytes from the array of given ByteBuffers and combines them
     * into a single byte array.
     *
     * @param buffers the collection of ByteBuffers to combine
     * @return a new byte array
     */
    public static byte[] asByteArray(ByteBuffer[] buffers) {
        int bufferLen = 0;
        for (ByteBuffer buffer : buffers) {
            buffer.flip();  // flip to reading mode
            bufferLen += buffer.remaining();
        }
        byte[] bytes = new byte[bufferLen];
        int offset = 0;
        for (ByteBuffer buffer : buffers) {
            int srcLen = buffer.remaining();
            buffer.get(bytes, offset, srcLen);
            offset += srcLen;
            buffer.clear();
        }
        return bytes;
    }

    public static byte[] asByteArray(ByteBuffer buffer, int index, int len) {
        byte[] bytes = new byte[len];
        //buffer.get(bytes);
        if (len > buffer.remaining())
            throw new BufferUnderflowException();
        for (int i = 0; i < len; i++)
            bytes[i] = buffer.get(index + i);
        return bytes;
    }

    /**
     * ByteBuffer contents for a FIX message should end with 010=nnn| (8 bytes)
     */
    public static boolean hasChecksum(ByteBuffer buffer) {
        int offset = buffer.remaining() - 8;
        return ByteBufferUtil2.hasBytes(buffer, offset, CHECKSUM_BYTE_SUFFIX);
    }

    /**
     * Checks if the byte in the buffer at position represented by offset
     * matches the given byte.
     */
    public static boolean hasByte(ByteBuffer buffer, int offset, byte wantedByte) {
        return buffer != null && buffer.get(offset) == wantedByte;
    }

    /**
     * Checks whether the bytes in the buffer at position represented by offset
     * are equal to the bytes in the given byte array.
     */
    public static boolean hasBytes(ByteBuffer buffer, int offset, byte[] bytes) {
        for (int pos = 0; pos < bytes.length; pos++) {
            if (buffer.get(offset + pos) != bytes[pos])
                return false;
        }
        return true;
    }

    public static int readableBytes(ByteBuffer buffer) {
        return buffer.position();
    }

    public static int readableBytes(ByteBuffer[] buffers) {
        int readableBytes = 0;
        if (buffers != null) {
            for (ByteBuffer buffer : buffers) {
                if (buffer != null) {
                    readableBytes += buffer.remaining();
                }
            }
        }
        return readableBytes;
    }

    public static int toInt(ByteBuffer buffer, int offset, int len) {
        int number = 0;
        int index = 0;
        for (int i = 0; i < len; i++) {
            index = offset + len - i - 1;
            number += ((buffer.get(index) - '0') * Math.pow(10, i));
        }
        return number;
    }

    public static void putBytes(ByteBuffer buffer, byte[] bytes) {
        buffer.put(bytes, 0, bytes.length);
    }
}
