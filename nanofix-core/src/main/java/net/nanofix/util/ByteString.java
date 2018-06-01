package net.nanofix.util;

public class ByteString {

    private final byte[] bytes;

    public ByteString(byte[] bytes) {
        this.bytes = bytes;
    }

    public static ByteString of(String string) {
        return new ByteString(ByteArrayUtil.asByteArray(string));
    }

    public byte[] bytes() {
        return bytes;
    }

}
