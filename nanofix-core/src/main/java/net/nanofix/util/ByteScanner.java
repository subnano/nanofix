package net.nanofix.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import static net.nanofix.util.FIXBytes.EQUALS;
import static net.nanofix.util.FIXBytes.SOH;

public final class ByteScanner {

    static final int NOT_FOUND = -1;

    private ByteScanner() {
        // can't touch this
    }

    public static int nextTagIndex(ByteBuf buffer, int startIndex) {
        return indexOf(buffer, startIndex, SOH);
    }

    public static int tagLen(ByteBuf buffer, int startIndex) {
        int index = indexOf(buffer, startIndex, EQUALS);
        return index == NOT_FOUND ? NOT_FOUND : index - startIndex;
    }

    public static int nextValueIndex(ByteBuf buffer, int startIndex) {
        return indexOf(buffer, startIndex, EQUALS);
    }

    public static int indexOf(ByteBuf buffer, int startIndex, byte value) {
        int toIndex = buffer.readableBytes() - startIndex;
        int index = ByteBufUtil.indexOf(buffer, startIndex, toIndex, value);
        // if we found the index then return the next byte after the '='
        return index == NOT_FOUND || index == toIndex ? NOT_FOUND : index + 1;
    }

    public static int valueLen(ByteBuf buffer, int startIndex) {
        int toIndex = buffer.readableBytes() - startIndex;
        int index = ByteBufUtil.indexOf(buffer, startIndex, toIndex, SOH);
        return index == NOT_FOUND ? NOT_FOUND : index - startIndex;
    }
}
