package net.nanofix.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Mark
 * Date: 04/04/12
 * Time: 16:26
 */
@XStreamAlias("Application")
public class ApplicationConfigImpl implements ApplicationConfig {

    @XStreamAsAttribute
    private String id;

    @XStreamAlias("Sessions")
    private final List<SessionConfig> sessionConfigs;

    public ApplicationConfigImpl() {
        sessionConfigs = new ArrayList<SessionConfig>();
    }

    public String getId() {
        return id;
    }

    @Override
    public void addSessionConfig(SessionConfig sessionConfig) {
        if (sessionConfigs.contains(sessionConfig)) {
            throw new IllegalArgumentException("session config already added");
        }
        sessionConfigs.add(sessionConfig);
    }

    @Override
    public void removeSessionConfig(SessionConfig sessionConfig) {
        if (!sessionConfigs.contains(sessionConfig)) {
            throw new IllegalArgumentException("session config not found");
        }
        sessionConfigs.remove(sessionConfig);
    }

    @Override
    public SessionConfig[] getSessionConfigs() {
        return sessionConfigs.toArray(new SessionConfig[sessionConfigs.size()]);
    }

    @Override
    public ConnectionConfig[] getConnectors() {
        List<ConnectionConfig> connectors = new ArrayList<ConnectionConfig>();
        for (SessionConfig sessionConfig : sessionConfigs) {
            for (ConnectionConfig connector : sessionConfig.getConnectors()) {
                if (!connectors.contains(connector)) {
                    connectors.add(connector);
                }
            }
        }
        return connectors.toArray(new ConnectionConfig[connectors.size()]);
    }
}
