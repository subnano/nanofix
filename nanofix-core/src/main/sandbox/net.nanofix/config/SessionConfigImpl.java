package net.nanofix.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.nanofix.message.FIXMessageFactory;
import net.nanofix.processor.MessageProcessor;
import net.nanofix.schedule.ScheduledTask;

import java.util.List;

/**
 * User: Mark
 * Date: 02/04/12
 * Time: 20:44
 */
@XStreamAlias("Session")
public class SessionConfigImpl implements SessionConfig {

    @XStreamAsAttribute
    private String version;

    @XStreamAsAttribute
    private String senderCompID;

    @XStreamAsAttribute
    private String targetCompID;

    @XStreamAsAttribute
    private String senderSubID;

    @XStreamAsAttribute
    private String senderLocationID;

    @XStreamAsAttribute
    private String targetSubID;

    @XStreamAsAttribute
    private String targetLocationID;

    @XStreamAsAttribute
    private int heartbeatInterval;

    @XStreamAsAttribute
    private boolean resetSeqNum;

    @XStreamAsAttribute
    private boolean useMillisInTimeStamp;

    @XStreamAlias("Connectors")
    private List<ConnectionConfig> connectionConfig;

    @XStreamAlias("AllowedCounterParties")
    private String[] allowedCounterParties;

    @XStreamAlias("IgnoredCounterParties")
    private String[] ignoredCounterParties;

    @XStreamAlias("MessageFactory")
    private FIXMessageFactory fixMessageFactory;

    @XStreamAlias("MessageProcessor")
    private MessageProcessor messageProcessor;

    @XStreamAlias("Schedule")
    private List<ScheduledTask> scheduledTasks;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        if (version == null) {
            throw new NullPointerException("version is null");
        }
        this.version = version;
    }

    public String getSenderCompID() {
        return senderCompID;
    }

    public void setSenderCompID(String senderCompID) {
        this.senderCompID = senderCompID;
    }

    public String getTargetCompID() {
        return targetCompID;
    }

    public void setTargetCompID(String targetCompID) {
        this.targetCompID = targetCompID;
    }

    public String getSenderSubID() {
        return senderSubID;
    }

    public void setSenderSubID(String senderSubID) {
        this.senderSubID = senderSubID;
    }

    public String getSenderLocationID() {
        return senderLocationID;
    }

    public void setSenderLocationID(String senderLocationID) {
        this.senderLocationID = senderLocationID;
    }

    public String getTargetSubID() {
        return targetSubID;
    }

    public void setTargetSubID(String targetSubID) {
        this.targetSubID = targetSubID;
    }

    public String getTargetLocationID() {
        return targetLocationID;
    }

    public void setTargetLocationID(String targetLocationID) {
        this.targetLocationID = targetLocationID;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public void setHeartbeatInterval(int heartbeatInterval) {
        this.heartbeatInterval = heartbeatInterval;
    }

    @Override
    public List<ConnectionConfig> getConnectors() {
        return connectionConfig;
    }

    @Override
    public void setResetSeqNum(boolean resetSeqNum) {
        this.resetSeqNum = resetSeqNum;
    }

    public boolean isResetSeqNum() {
        return resetSeqNum;
    }

    @Override
    public void setUseMillisInTimeStamp(boolean useMillisInTimeStamp) {
        this.useMillisInTimeStamp = useMillisInTimeStamp;
    }

    public boolean isUseMillisInTimeStamp() {
        return useMillisInTimeStamp;
    }

    @Override
    public String toString() {
        return "SessionConfigImpl{" +
                "senderCompID='" + senderCompID + '\'' +
                ", senderSubID='" + senderSubID + '\'' +
                ", senderLocationID='" + senderLocationID + '\'' +
                ", targetCompID='" + targetCompID + '\'' +
                ", targetSubID='" + targetSubID + '\'' +
                ", targetLocationID='" + targetLocationID + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
