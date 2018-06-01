package net.nanofix.session;

import net.nanofix.message.FIXMessage;
import net.nanofix.message.Tags;
import net.nanofix.util.FIXMessageUtil;

/**
 * User: Mark
 * Date: 26/03/12
 * Time: 21:52
 */
public class SessionID {

    public static final String UNDEFINED = "";

    private final String beginString;
    private final String senderCompID;
    private final String senderSubID;
    private final String senderLocationID;
    private final String targetCompID;
    private final String targetSubID;
    private final String targetLocationID;
    private final String toString;

    public SessionID() {
        throw new UnsupportedOperationException("Unsupported constructor: use constructor with arguments");
    }

    public SessionID(String beginString, String senderCompID, String targetCompID) {
        this(beginString, senderCompID, null, null, targetCompID, null, null);
    }

    public SessionID(String beginString,
                     String senderCompID, String senderSubID,
                     String targetCompID, String targetSubID) {
        this(beginString, senderCompID, senderSubID, null, targetCompID, targetSubID, null);
    }

    public SessionID(String beginString,
                     String senderCompID, String senderSubID, String senderLocationID,
                     String targetCompID, String targetSubID, String targetLocationID) {
        if (senderCompID == null || senderCompID.isEmpty()) {
            throw new IllegalArgumentException("Value for senderCompID cannot be null or empty");
        }
        if (targetCompID == null || targetCompID.isEmpty()) {
            throw new IllegalArgumentException("Value for targetCompID cannot be null or empty");
        }
        this.beginString = beginString;
        this.senderCompID = senderCompID;
        this.senderSubID = senderSubID;
        this.senderLocationID = senderLocationID;
        this.targetCompID = targetCompID;
        this.targetSubID = targetSubID;
        this.targetLocationID = targetLocationID;
        toString = createToString();
    }

    public String getBeginString() {
        return beginString;
    }

    public String getSenderCompID() {
        return senderCompID;
    }

    public String getTargetCompID() {
        return targetCompID;
    }

    public String getSenderSubID() {
        return senderSubID;
    }

    public String getSenderLocationID() {
        return senderLocationID;
    }

    public String getTargetSubID() {
        return targetSubID;
    }

    public String getTargetLocationID() {
        return targetLocationID;
    }

    private String createToString() {
        return beginString
                + ":"
                + senderCompID
                + (senderSubID == null ? "" : "." + senderSubID)
                + (senderLocationID == null ? "" : "." + senderLocationID)
                + "~"
                + targetCompID
                + (targetSubID == null ? "" : "." + targetSubID)
                + (targetLocationID == null ? "" : "." + targetLocationID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionID sessionID = (SessionID) o;

        if (beginString != null ? !beginString.equals(sessionID.beginString) : sessionID.beginString != null)
            return false;
        if (senderCompID != null ? !senderCompID.equals(sessionID.senderCompID) : sessionID.senderCompID != null)
            return false;
        if (senderLocationID != null ? !senderLocationID.equals(sessionID.senderLocationID) : sessionID.senderLocationID != null)
            return false;
        if (senderSubID != null ? !senderSubID.equals(sessionID.senderSubID) : sessionID.senderSubID != null)
            return false;
        if (targetCompID != null ? !targetCompID.equals(sessionID.targetCompID) : sessionID.targetCompID != null)
            return false;
        if (targetLocationID != null ? !targetLocationID.equals(sessionID.targetLocationID) : sessionID.targetLocationID != null)
            return false;
        if (targetSubID != null ? !targetSubID.equals(sessionID.targetSubID) : sessionID.targetSubID != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = beginString != null ? beginString.hashCode() : 0;
        result = 31 * result + (senderCompID != null ? senderCompID.hashCode() : 0);
        result = 31 * result + (senderSubID != null ? senderSubID.hashCode() : 0);
        result = 31 * result + (senderLocationID != null ? senderLocationID.hashCode() : 0);
        result = 31 * result + (targetCompID != null ? targetCompID.hashCode() : 0);
        result = 31 * result + (targetSubID != null ? targetSubID.hashCode() : 0);
        result = 31 * result + (targetLocationID != null ? targetLocationID.hashCode() : 0);
        return result;
    }

    public String toString() {
        return toString;
    }

    public static SessionID parse(FIXMessage msg) {
        return new SessionID(
                FIXMessageUtil.getStringFieldValue(msg, Tags.BeginString),
                FIXMessageUtil.getStringFieldValue(msg, Tags.SenderCompID),
                FIXMessageUtil.getStringFieldValue(msg, Tags.SenderSubID),
                FIXMessageUtil.getStringFieldValue(msg, Tags.SenderLocationID),
                FIXMessageUtil.getStringFieldValue(msg, Tags.TargetCompID),
                FIXMessageUtil.getStringFieldValue(msg, Tags.TargetSubID),
                FIXMessageUtil.getStringFieldValue(msg, Tags.TargetLocationID)
        );
    }

    public SessionID reverse() {
        return new SessionID(
            this.beginString,
            this.targetCompID,
            this.targetSubID,
            this.targetLocationID,
            this.senderCompID,
            this.senderSubID,
            this.senderLocationID
        );
    }
}
