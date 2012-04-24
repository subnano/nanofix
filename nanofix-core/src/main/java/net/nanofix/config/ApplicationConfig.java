package net.nanofix.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**

 */
public interface ApplicationConfig {
    String getId();
    void addSessionConfig(SessionConfig sessionConfig);
    void removeSessionConfig(SessionConfig sessionConfig);
    SessionConfig[] getSessionConfigs();
    ConnectionConfig[] getConnectors();
}
