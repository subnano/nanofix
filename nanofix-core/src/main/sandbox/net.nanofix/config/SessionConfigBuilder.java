package net.nanofix.config;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:41
 */
public class SessionConfigBuilder {

    private final SessionConfigImpl config;

    public SessionConfigBuilder() {
        config = new SessionConfigImpl();
    }

    public SessionConfigBuilder version(String version) {
        config.setVersion(version);
        return this;
    }

    public SessionConfigBuilder senderCompID(String compID) {
        config.setSenderCompID(compID);
        return this;
    }

    public SessionConfigBuilder senderSubID(String subID) {
        config.setSenderSubID(subID);
        return this;
    }

    public SessionConfigBuilder senderLocationID(String locationID) {
        config.setSenderLocationID(locationID);
        return this;
    }

    public SessionConfigBuilder targetCompID(String compID) {
        config.setTargetCompID(compID);
        return this;
    }

    public SessionConfigBuilder targetSubID(String subID) {
        config.setTargetSubID(subID);
        return this;
    }

    public SessionConfigBuilder targetLocationID(String locationID) {
        config.setTargetLocationID(locationID);
        return this;
    }

    public SessionConfigBuilder heartbeatInterval(int interval) {
        config.setHeartbeatInterval(interval);
        return this;
    }

    public SessionConfigBuilder resetSeqNum(boolean resetSeqNum) {
        config.setResetSeqNum(resetSeqNum);
        return this;
    }

    public SessionConfigBuilder useMillisInTimeStamp(boolean useMillisInTimeStamp) {
        config.setUseMillisInTimeStamp(useMillisInTimeStamp);
        return this;
    }

    public SessionConfig build() {
        return config;

    }
}
