package net.nanofix.message;

import net.nanofix.field.BooleanField;
import net.nanofix.field.Field;
import net.nanofix.field.IntegerField;
import net.nanofix.field.StringField;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TODO
 * - List is just a version 0.1 implementation
 * - abstract the field storage as a list incurs a O(n) cost in getField()
 * - separate parsing of header and body for performance
 *
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 08:47
 */
public class StandardFIXMessage implements FIXMessage {

    private String msgType;
    private byte[] rawBytes;
    //private List<Field> fields = new ArrayList<Field>();
    private Map<Integer,Field> fields = new LinkedHashMap<Integer,Field>();
    private FIXMessageFormatter stringFormatter = new MessageToStringFormatter();
    private long timestamp = -1L;
    private long msgSeqNum = -1L;

    @Override
    public void clear() {
        setMsgType(null);
        fields.clear();
    }

    @Override
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String getMsgType() {
        return msgType;
    }

    @Override
    public void setRawBytes(byte[] bytes) {
        this.rawBytes = bytes;
    }

    @Override
    public byte[] getRawBytes() {
        return rawBytes;
    }

    @Override
    public void addField(Field field) {
        fields.put(field.getTag(), field);
    }

    @Override
    public boolean hasField(int tag) {
        return fields.containsKey(tag);
    }

    @Override
    public Field getField(int tag) {
        return fields.get(tag);
    }

    @Override
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void setMsgSeqNum(long seqNum) {
        this.msgSeqNum = seqNum;
    }

    @Override
    public long getMsgSeqNum() {
        return msgSeqNum;
    }

    @Override
    public String getStringFieldValue(int tag) throws MissingFieldException {
        Field field = getField(tag);
        if (field == null)
            throw new MissingFieldException(tag, "Field " + tag + " missing");
        return "";
//        return field.getStringValue();
    }

    @Override
    public int getIntegerFieldValue(int tag) throws MissingFieldException {
        Field field = getField(tag);
        if (field == null)
            throw new MissingFieldException(tag, "Field " + tag + " missing");
        return -1;
//        return field.getIntValue();
    }

    @Override
    public long getLongFieldValue(int tag) throws MissingFieldException {
        Field field = getField(tag);
        if (field == null)
            throw new MissingFieldException(tag, "Field " + tag + " missing");
        return -1L;
//        return field.getLongValue();
    }

    @Override
    public void setFieldValue(int tag, boolean value) {
        addField(new BooleanField(tag, value));
    }

    @Override
    public void setFieldValue(int tag, int value) {
        addField(new IntegerField(tag, value));
    }

    @Override
    public void setFieldValue(int tag, String value) {
        addField(new StringField(tag, value));
    }

    @Override
    public String toString() {
        return stringFormatter.toString(this);
    }
}
