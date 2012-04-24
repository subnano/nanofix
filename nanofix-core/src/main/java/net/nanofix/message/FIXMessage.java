package net.nanofix.message;

import net.nanofix.field.Field;

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
    void addField(Field rawField);
    boolean hasField(int tag);
    Field getField(int tag);
    void setTimestamp(long timestamp);
    long getTimestamp();
    void setMsgSeqNum(long seqNum);
    long getMsgSeqNum();

    String getStringFieldValue(int tag) throws MissingFieldException;
    int getIntegerFieldValue(int tag) throws MissingFieldException;
    long getLongFieldValue(int tag) throws MissingFieldException;

}
