package net.nanofix.session;

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
    private final String id;

    public SessionID() {
        throw new UnsupportedOperationException("Unsupported constructor: use constructor with arguments");
    }

    public SessionID(String beginString, String senderCompID, String targetCompID) {
        this(beginString, senderCompID, UNDEFINED, UNDEFINED, targetCompID, UNDEFINED, UNDEFINED);
    }

    public SessionID(String beginString,
                     String senderCompID, String senderSubID,
                     String targetCompID, String targetSubID) {
        this(beginString, senderCompID, senderSubID, UNDEFINED, targetCompID, targetSubID, UNDEFINED);
    }

    public SessionID(String beginString,
                     String senderCompID, String senderSubID, String senderLocationID,
                     String targetCompID, String targetSubID, String targetLocationID) {
        this.beginString = beginString;
        this.senderCompID = senderCompID;
        this.senderSubID = senderSubID;
        this.senderLocationID = senderLocationID;
        this.targetCompID = targetCompID;
        this.targetSubID = targetSubID;
        this.targetLocationID = targetLocationID;
        id = createID();
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

    private String createID() {
        return beginString
                + ":"
                + senderCompID
                + (isSet(senderSubID) ? "." + senderSubID : "")
                + (isSet(senderLocationID) ? "." + senderLocationID : "")
                + "~"
                + targetCompID
                + (isSet(targetSubID) ? "." + targetSubID : "")
                + (isSet(targetLocationID) ? "." + targetLocationID : "")
                ;
    }

    private boolean isSet(String value) {
        return !value.equals(UNDEFINED);
    }

    public String toString() {
        return id;
    }

}
