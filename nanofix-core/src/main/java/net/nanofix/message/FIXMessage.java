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
    boolean hasField(int tag);
    void setTimestamp(long timestamp);
    long getTimestamp();
    void setMsgSeqNum(long seqNum);
    long getMsgSeqNum();

    String getStringFieldValue(int tag) throws MissingFieldException;
    int getIntegerFieldValue(int tag) throws MissingFieldException;
    long getLongFieldValue(int tag) throws MissingFieldException;

    byte[] setFieldValue(int tag, byte[] value);
    boolean setFieldValue(int tag, boolean value);
    int setFieldValue(int tag, int value);
    long setFieldValue(int tag, long value);
    String setFieldValue(int tag, String value);

    boolean getBooleanFieldValue(int tag) throws MissingFieldException;

    Object getFieldValue(int tag);

    Set<Integer> getTags();

}
