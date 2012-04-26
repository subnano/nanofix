package net.nanofix.field;

/**
 * User: Mark
 * Date: 25/04/12
 * Time: 21:03
 */
public class RepeatingGroup implements Field {

    private final int tag;

    public RepeatingGroup(int tag) {
        this.tag = tag;
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
    public Object getValue() {
        return null;
    }
}
