package net.nanofix.field;

/**
 * User: Mark
 * Date: 31/03/12
 * Time: 20:44
 */
public class BooleanField implements Field {

    private final int tag;
    private final boolean value;

    public BooleanField(int tag, boolean value) {
        this.tag = tag;
        this.value = value;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public Boolean getValue() {
        return value;
    }
}
