package net.nanofix.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.nanofix.session.DefaultSessionFactory;
import net.nanofix.session.Session;
import net.nanofix.session.SessionFactory;
import net.nanofix.netty.ClientSocketConnector;
import net.nanofix.netty.ServerSocketConnector;
import net.nanofix.netty.SocketConnector;
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

    private final Logger LOG = LoggerFactory.getLogger(ApplicationConfigImpl.class);

    @XStreamAsAttribute
    private String id;

    @XStreamAlias("SessionFactory")
    private SessionFactory sessionFactory = new DefaultSessionFactory();

    @XStreamAlias("Sessions")
    private final List<SessionConfig> sessionConfigs;

    private final transient List<Session> sessions = new ArrayList<Session>();
    private final transient List<SocketConnector> socketConnectors = new ArrayList<SocketConnector>();

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

    /**
     * Returns the SessionFactory used to create Session objects
     * @return the SessionFactory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * called when the object is deserialized
     * @return the newly created object
     */
    public ApplicationConfigImpl readResolve() {
        createSessions();
        createConnectors();
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

    private void createSessions() {
        if (sessionConfigs == null || sessionConfigs.isEmpty()) {
            throw new IllegalArgumentException("SessionConfigs is null or empty");
        }
        for (SessionConfig sessionConfig : sessionConfigs) {
            sessions.add(sessionFactory.createSession(sessionConfig));
        }
    }

    private void createConnectors() {
        if (sessions == null || sessions.isEmpty()) {
            throw new IllegalArgumentException("Cannot create Connectors from sessions that is null or empty");
        }
        for (Session session : sessions) {
            for (ConnectionConfig connectorConfig : session.getConfig().getConnectors()) {
                SocketConnector socketConnector = null;
                if (connectorConfig instanceof ServerSocketConfig) {
                    LOG.info("creating server connector from config - {}", connectorConfig.toString());
                    socketConnector = new ServerSocketConnector((ServerSocketConfig) connectorConfig);
                }
                else if (connectorConfig instanceof ClientSocketConfig) {
                    LOG.info("creating client connector from config - {}", connectorConfig.toString());
                    socketConnector = new ClientSocketConnector((ClientSocketConfig) connectorConfig, session);
                }

                socketConnectors.add(socketConnector);
            }
        }
    }

    @Override
    public List<Session> getSessions() {
        return sessions;
    }

    @Override
    public List<SocketConnector> getSocketConnectors() {
        return socketConnectors;
    }
}
