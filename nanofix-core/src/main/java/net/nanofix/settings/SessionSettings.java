package net.nanofix.settings;

public interface SessionSettings {

    String getHost();

    int getPort();

    int getHeartbeatInterval();

    boolean isResetSeqNumOnLogon();
}
