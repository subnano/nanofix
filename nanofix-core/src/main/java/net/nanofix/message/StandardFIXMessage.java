package net.nanofix.message;

import net.nanofix.util.FieldValueConverter;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
    private Map<Integer,Object> fields = new LinkedHashMap<Integer,Object>();
    private FIXMessageFormatter stringFormatter = new SummeryMessageToStringFormatter();
    private long timestamp = -1L;
    private long msgSeqNum = -1L;

    public StandardFIXMessage() {
    }

    public StandardFIXMessage(String msgType) {
        this.msgType = msgType;
    }

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
    public boolean hasField(int tag) {
        return fields.containsKey(tag);
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
        Object value = fields.get(tag);
        if (value == null) {
            throw new MissingFieldException(tag, "Field " + tag + " missing");
        }
        if (value instanceof String) {
            return (String) value;
        }
        return FieldValueConverter.convertToString(value);
    }

    @Override
    public int getIntegerFieldValue(int tag) throws MissingFieldException {
        Object value = fields.get(tag);
        if (value == null) {
            throw new MissingFieldException(tag, "Field " + tag + " missing");
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return FieldValueConverter.convertToInt(value);
    }

    @Override
    public long getLongFieldValue(int tag) throws MissingFieldException {
        Object value = fields.get(tag);
        if (value == null) {
            throw new MissingFieldException(tag, "Field " + tag + " missing");
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        return FieldValueConverter.convertToLong(value);
    }

    @Override
    public boolean getBooleanFieldValue(int tag) throws MissingFieldException {
        Object value = fields.get(tag);
        if (value == null) {
            throw new MissingFieldException(tag, "Field " + tag + " missing");
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return FieldValueConverter.convertToBoolean(value);
    }

    @Override
    public Object getFieldValue(int tag) {
        return fields.get(tag);
    }

    @Override
    public byte[] setFieldValue(int tag, byte[] value) {
        fields.put(tag, value);
        return value;
    }

    @Override
    public boolean setFieldValue(int tag, boolean value) {
        fields.put(tag, value);
        return value;
    }

    @Override
    public int setFieldValue(int tag, int value) {
        fields.put(tag, value);
        return value;
    }

    @Override
    public long setFieldValue(int tag, long value) {
        fields.put(tag, value);
        return value;
    }

    @Override
    public String setFieldValue(int tag, String value) {
        fields.put(tag, value);
        return value;
    }

    public Set<Integer> getTags() {
        return fields.keySet();
    }

    @Override
    public String toString() {
        return stringFormatter.toString(this);
    }
}
