package net.nanofix.field;

/**
 * User: Mark
 * Date: 31/03/12
 * Time: 20:44
 */
public class StringField implements Field {

    private final int tag;
    private final String value;

    public StringField(int tag, String value) {
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
    public String getValue() {
        return value;
    }
}
