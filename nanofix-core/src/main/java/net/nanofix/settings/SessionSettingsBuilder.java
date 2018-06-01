package net.nanofix.settings;

import net.nanofix.util.Builder;

public class SessionSettingsBuilder implements Builder<SessionSettings> {

    String host;
    int port;
    String targetCompId;
    String senderCompId;
    String fixVersion;
    int heartbeatInterval;
    boolean resetSeqNumOnLogon;

    public SessionSettingsBuilder host(String host) {
        this.host = host;
        return this;
    }

    public SessionSettingsBuilder port(int port) {
        this.port = port;
        return this;
    }

    public SessionSettingsBuilder heartbeatInterval(int interval) {
        this.heartbeatInterval = interval;
        return this;
    }

    public SessionSettingsBuilder targetCompId(String compId) {
        this.targetCompId = compId;
        return this;
    }

    public SessionSettingsBuilder senderCompId(String compId) {
        this.senderCompId = compId;
        return this;
    }

    public SessionSettingsBuilder fixVersion(String version) {
        this.fixVersion = version;
        return this;
    }

    public SessionSettingsBuilder resetSeqNumOnLogon(boolean flag) {
        this.resetSeqNumOnLogon = flag;
        return this;
    }

    @Override
    public SessionSettings build() {
        return new DefaultSessionSettings(this);
    }
}
