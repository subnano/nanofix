package net.nanofix.util;

import net.nanofix.message.FIXConstants.*;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.MissingFieldException;
import net.nanofix.message.Tags;

/**
 * Created by IntelliJ IDEA.
 * User: Mark
 * Date: 22/03/12
 * Time: 16:24
 */
public class FIXMessageUtil {

    private static final String ADMIN_MSG_TYPES = "0A12345";

    public static boolean isAdminMessage(String msgType) {
        return msgType.length() == 1 && ADMIN_MSG_TYPES.contains(msgType);
    }

    public static boolean isHeaderField(int tag) {
        switch (tag) {
            case Tags.BeginString:
            case Tags.BodyLength:
            case Tags.MsgType:
            case Tags.SenderCompID:
            case Tags.TargetCompID:
            case Tags.OnBehalfOfCompID:
            case Tags.DeliverToCompID:
            case Tags.SecureDataLen:
            case Tags.MsgSeqNum:
            case Tags.SenderSubID:
            case Tags.SenderLocationID:
            case Tags.TargetSubID:
            case Tags.TargetLocationID:
            case Tags.OnBehalfOfSubID:
            case Tags.OnBehalfOfLocationID:
            case Tags.DeliverToSubID:
            case Tags.DeliverToLocationID:
            case Tags.PossDupFlag:
            case Tags.PossResend:
            case Tags.SendingTime:
            case Tags.OrigSendingTime:
            case Tags.XmlDataLen:
            case Tags.XmlData:
            case Tags.MessageEncoding:
            case Tags.LastMsgSeqNumProcessed:
//            case Tags.OnBehalfOfSendingTime:
            case Tags.ApplVerID:
            case Tags.CstmApplVerID:
            case Tags.NoHops:
                return true;
            default:
                return false;
        }
    }

    public static boolean isTrailerField(int tag) {
        switch (tag) {
            case Tags.SignatureLength:
            case Tags.Signature:
            case Tags.CheckSum:
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns the field value from the message for the given field if the field exists.
     * Or null if it doesn't exist.
     *
     * @param msg The given FIX message
     * @param tag The field number to extract the value for
     * @return The field value or null
     */
    public static String getStringFieldValue(FIXMessage msg, int tag) {
        return getStringFieldValue(msg, tag, null);
    }

    /**
     * Returns the field value as a String from the message for the given field if the field exists.
     * Or the default value if it doesn't exist.
     *
     * @param msg The given FIX message
     * @param tag The field number to extract the value for
     * @param defaultValue The default value to return if the field does not exist
     * @return The field value or default value
     */
    public static String getStringFieldValue(FIXMessage msg, int tag, String defaultValue) {
        throw new UnsupportedOperationException();
//        String value;
//        try {
//            value = msg.hasField(tag) ? msg.getStringFieldValue(tag) : defaultValue;
//        } catch (MissingFieldException e) {
//            value = defaultValue;
//        }
//        return value;
    }

    /**
     * Returns the field value as a boolean from the message for the given field if the field exists.
     * Or the default value if it doesn't exist.
     *
     * @param msg The given FIX message
     * @param tag The field number to extract the value for
     * @param defaultValue The default value to return if the field does not exist
     * @return The field value or default value
     */
    public static boolean getBooleanFieldValue(FIXMessage msg, int tag, boolean defaultValue) {
        throw new UnsupportedOperationException();
//        boolean value;
//        try {
//            value = msg.hasField(tag) ? msg.getBooleanFieldValue(tag) : defaultValue;
//        } catch (MissingFieldException e) {
//            value = defaultValue;
//        }
//        return value;
    }
}
