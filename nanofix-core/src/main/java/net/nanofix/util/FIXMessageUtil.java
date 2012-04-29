package net.nanofix.util;

import net.nanofix.message.FIXConstants.*;
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

    public static boolean isHeaderField(int field) {
        switch (field) {
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

    public static boolean isTrailerField(int field) {
        switch (field) {
            case Tags.SignatureLength:
            case Tags.Signature:
            case Tags.CheckSum:
                return true;
            default:
                return false;
        }
    }
}
