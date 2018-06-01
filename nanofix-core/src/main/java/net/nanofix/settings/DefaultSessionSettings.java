package net.nanofix.settings;

public class DefaultSessionSettings implements SessionSettings {

    private final String host;
    private final int port;
    private final int heartbeatInterval;
    private final boolean resetSeqNumOnLogon;

    DefaultSessionSettings(SessionSettingsBuilder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.heartbeatInterval = builder.heartbeatInterval;
        this.resetSeqNumOnLogon = builder.resetSeqNumOnLogon;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    @Override
    public boolean isResetSeqNumOnLogon() {
        return resetSeqNumOnLogon;
    }
}
