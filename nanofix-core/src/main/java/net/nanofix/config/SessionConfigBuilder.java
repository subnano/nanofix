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

    public SessionConfigBuilder setVersion(String version) {
        config.setVersion(version);
        return this;
    }

    public SessionConfigBuilder setSenderCompID(String compID) {
        config.setSenderCompID(compID);
        return this;
    }

    public SessionConfigBuilder setSenderSubID(String subID) {
        config.setSenderSubID(subID);
        return this;
    }

    public SessionConfigBuilder setSenderLocationID(String locationID) {
        config.setSenderLocationID(locationID);
        return this;
    }

    public SessionConfigBuilder setTargetCompID(String compID) {
        config.setTargetCompID(compID);
        return this;
    }

    public SessionConfigBuilder setTargetSubID(String subID) {
        config.setTargetSubID(subID);
        return this;
    }

    public SessionConfigBuilder setTargetLocationID(String locationID) {
        config.setTargetLocationID(locationID);
        return this;
    }

    public SessionConfigBuilder setHeartbeatInterval(int interval) {
        config.setHeartbeatInterval(interval);
        return this;
    }

    public SessionConfig build() {
        return config;

    }
}
