package net.nanofix.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.nanofix.session.SessionFactory;
import net.nanofix.session.StaticSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Mark
 * Date: 04/04/12
 * Time: 16:26
 */
@XStreamAlias("Application")
public class ApplicationConfigImpl implements ApplicationConfig {

    private transient Logger LOG;

    @XStreamAsAttribute
    private String id;

    @XStreamAlias("SessionFactory")
    private SessionFactory sessionFactory;

    @XStreamAlias("Sessions")
    private final List<SessionConfig> sessionConfigs;

    public ApplicationConfigImpl() {
        sessionConfigs = new ArrayList<SessionConfig>();
    }

    private void initAfterPropertiesSet() {
        LOG = LoggerFactory.getLogger(ApplicationConfigImpl.class);

        // set any defaults that haven't created created
        if (sessionFactory == null) {
            sessionFactory = new StaticSessionFactory();
        }
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

    /**
     * Returns the SessionFactory used to create Session objects
     * @return the SessionFactory
     */
    @Override
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * called when the object is deserialized
     * @return the newly created object
     */
    public ApplicationConfigImpl readResolve() {

        // set defaults for this instance
        initAfterPropertiesSet();

        return this;
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
