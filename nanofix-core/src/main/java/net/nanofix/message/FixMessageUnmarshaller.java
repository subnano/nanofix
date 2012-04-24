package net.nanofix.message;

import net.nanofix.field.RawField;
import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.FIXMessageUtil;

import java.util.Arrays;

/**
 * User: Mark Wardell
 * Date: 18/10/11
 * Time: 08:23
 */
public class FixMessageUnmarshaller {

    public FIXMessage toFIXMessage(byte[] bytes, FIXMessage target) {
        return toFIXMessage(bytes, target, false);
    }

    /**
     * Read the fields from the byte array adding each field to the collection
     * @param bytes the byte array containing the raw tag/value pairs
     * @param target the target FIXMessage to populate
     * @param headerOnly whether to process the header fields only
     * @return the target FIXMessage
     */
    public FIXMessage toFIXMessage(byte[] bytes, FIXMessage target, boolean headerOnly) {
        target.clear();
        int offset = 0;
        while (offset < bytes.length) {
            int equalPos = getEqualPos(bytes, offset);
            int tag = ByteArrayUtil.toInteger(bytes, offset, equalPos - offset);

            // return now if this isn't a header field and that's all we want
            if (headerOnly && !FIXMessageUtil.isHeaderField(tag)) {
                return target;
            }
            int delimiterPos = getDelimiterPos(bytes, equalPos + 1);
            target.addField(new RawField(tag, Arrays.copyOfRange(bytes, equalPos + 1, delimiterPos)));

            offset = delimiterPos + 1;
        }
        return target;
    }

    /**
     * Find the position of the next '=' from the given offset
     * @param bytes The source bytes to search
     * @param offset The offset to start searching in the given byte array
     * @return The position of the '=' after the given offset
     */
    private int getEqualPos(byte[] bytes, int offset) {
        for (int i=offset; i < bytes.length; i++) {
            if (bytes[i] == FIXConstants.SOH)
                throw new IllegalArgumentException("Tag delimiter '=' expected before SOH");
            if (bytes[i] == '=')
                return i;
        }
        throw new IllegalArgumentException("Tag delimiter '=' expected before end of array");
    }

    /**
     * Find the position of the next SOH from the given offset
     * @param bytes The source bytes to search
     * @param offset The offset to start searching in the given byte array
     * @return The position of the SOH after the given offset
     */
    private int getDelimiterPos(byte[] bytes, int offset) {
        for (int i=offset; i < bytes.length; i++) {
            if (bytes[i] == FIXConstants.SOH)
                return i;
        }
        throw new IllegalArgumentException("Tag/value delimiter SOH expected before end of array");
    }
}
