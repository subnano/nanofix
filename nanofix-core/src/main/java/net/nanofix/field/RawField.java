package net.nanofix.field;

import net.nanofix.util.ByteArrayUtil;

/**
 * User: Mark Wardell
 * Date: 18/10/11
 * Time: 08:57
 */
public class RawField implements Field {

    private int tag;
    private byte[] bytes;

    public RawField(int tag, byte[] bytes) {
        this.tag = tag;
        this.bytes = bytes;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }


    @Override
    public byte[] getValue() {
        return bytes;
    }

    public String getStringValue() {
        return new String(bytes);
    }

    public int getIntValue() {
        return ByteArrayUtil.toInteger(bytes) ;
    }

    public long getLongValue() {
        return ByteArrayUtil.toLong(bytes);
    }
}
