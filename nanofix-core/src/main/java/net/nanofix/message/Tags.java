package net.nanofix.message;

/**
 * User: Mark
 * Date: 13/04/12
 * Time: 11:26
 */
public interface Tags {

    // header
    public static final int BeginString = 8;
    public static final int BodyLength = 9;
    public static final int MsgType = 35;
    public static final int SenderCompID = 49;
    public static final int TargetCompID = 56;
    public static final int OnBehalfOfCompID = 115;
    public static final int DeliverToCompID = 128;
    public static final int SecureDataLen = 90;
    public static final int MsgSeqNum = 34;
    public static final int SenderSubID = 50;
    public static final int SenderLocationID = 142;
    public static final int TargetSubID = 57;
    public static final int TargetLocationID = 143;
    public static final int OnBehalfOfSubID = 116;
    public static final int OnBehalfOfLocationID = 144;
    public static final int DeliverToSubID = 129;
    public static final int DeliverToLocationID = 145;
    public static final int PossDupFlag = 43;
    public static final int PossResend = 97;
    public static final int SendingTime = 52;
    public static final int OrigSendingTime = 122;
    public static final int XmlDataLen = 212;
    public static final int XmlData = 213;
    public static final int MessageEncoding = 347;
    public static final int LastMsgSeqNumProcessed = 369;
    public static final int NoHops = 627;
    public static final int ApplVerID = 1128;
    public static final int CstmApplVerID = 1129;


    // trailer
    public static final int SignatureLength = 93;
    public static final int Signature = 89;
    public static final int CheckSum = 10;
}
