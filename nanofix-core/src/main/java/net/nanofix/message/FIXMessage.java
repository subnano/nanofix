package net.nanofix.message;

import net.nanofix.field.Field;

import java.util.Set;

/**
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 07:41
 */
public interface FIXMessage {
    public void clear();
    public void setMsgType(String msgType);
    public String getMsgType();
    public void setRawBytes(byte[] bytes);
    public byte[] getRawBytes();
    void setField(Field rawField);
    boolean hasField(int tag);
    void setTimestamp(long timestamp);
    long getTimestamp();
    void setMsgSeqNum(long seqNum);
    long getMsgSeqNum();

    String getStringFieldValue(int tag) throws MissingFieldException;
    int getIntegerFieldValue(int tag) throws MissingFieldException;
    long getLongFieldValue(int tag) throws MissingFieldException;

    void setFieldValue(int tag, boolean value);
    void setFieldValue(int tag, int value);
    void setFieldValue(int tag, String value);

    boolean getBooleanFieldValue(int tag) throws MissingFieldException;

    Object getFieldValue(int tag);

    Set<Integer> getTags();
}
