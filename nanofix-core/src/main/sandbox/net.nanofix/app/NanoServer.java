package net.nanofix.app;

import net.nanofix.netty.ClientSocketConnector;
import net.nanofix.netty.ServerSocketConnector;
import net.nanofix.session.Session;
import net.nanofix.config.*;
import net.nanofix.netty.SocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 06:02
 */
public class NanoServer extends AbstractComponent implements  Application {

    private final Logger LOG;
    private ApplicationConfig config;

    private static String DEFAULT_CONFIG_FILE_NAME = "nanofix.config";

    private transient List<Session> sessions;
    private transient List<SocketConnector> socketConnectors;

    public NanoServer() {
        LOG = LoggerFactory.getLogger(this.getClass());
        sessions = new ArrayList<Session>();
        socketConnectors = new ArrayList<SocketConnector>();
    }

    public NanoServer(String configName) throws FileNotFoundException {
        this();
        this.config = loadConfig(configName);
    }

    public NanoServer(InputStream inputStream) {
        this();
        this.config = loadConfig(inputStream);
    }

    private ApplicationConfig loadConfig(String configName) throws FileNotFoundException {
        InputStream inputStream = null;
        LOG.debug("trying to find [{}] on classpath", configName);
        inputStream = NanoServer.class.getResourceAsStream(configName);
        if (inputStream == null) {
            LOG.debug("not found, trying to load from file [{}]", configName);
            inputStream = new FileInputStream(configName);
        }
        return loadConfig(inputStream);
    }

    private ApplicationConfig loadConfig(InputStream inputStream) {
        XmlConfigFactory configFactory = new XmlConfigFactory();
        config = configFactory.load(inputStream);
        initAfterPropertyInitialization();
        return config;
    }

    private void initAfterPropertyInitialization() {

        // register with central manager
        ApplicationManager.addApplication(config.getId(), this);

        createSessions();
        createConnectors();
    }

    public ApplicationConfig getConfig() {
        return config;
    }

    @Override
    public void open() {
        // open services?
        // open other components?
        super.open();
        openSessions();
        openServerConnectors();
    }

    @Override
    public void start() {
        super.start();
        startSessions();
        startServerConnectors();
    }

    @Override
    public void refresh() {
        // TODO iterate through all components and refresh()
    }

    @Override
    public void stop() {
        // TODO iterate through all components and stop()
        super.stop();
        stopSessions();
        stopConnectors();
    }

    @Override
    public void close() {
        super.close();
        closeSessions();
    }

    private void createSessions() {
        if (config.getSessionConfigs() == null || config.getSessionConfigs().length == 0) {
            throw new IllegalArgumentException("SessionConfigs is null or empty");
        }
        for (SessionConfig sessionConfig : config.getSessionConfigs()) {
            sessions.add(config.getSessionFactory().createSession(sessionConfig));
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
                    socketConnector = new ServerSocketConnector(this, (ServerSocketConfig) connectorConfig);
                }
                else if (connectorConfig instanceof ClientSocketConfig) {
                    LOG.info("creating client connector from config - {}", connectorConfig.toString());
                    socketConnector = new ClientSocketConnector(this,(ClientSocketConfig) connectorConfig, session);
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

    private void openSessions() {
        for (Session session : getSessions()) {
            session.open();
        }
    }

    private void openServerConnectors() {
        for (SocketConnector connector : getSocketConnectors()) {
            if (connector instanceof ServerSocketConnector) {
                connector.open();
            }
        }
    }

    private void startSessions() {
        for (Session session : getSessions()) {
            session.start();
        }
    }

    private void startServerConnectors() {
        for (SocketConnector connector : getSocketConnectors()) {
            if (connector instanceof ServerSocketConnector) {
                connector.start();
            }
        }
    }

    private void stopConnectors() {
        for (SocketConnector connector : getSocketConnectors()) {
            connector.stop();
        }
    }

    private void stopSessions() {
        for (Session session : getSessions()) {
            session.stop();
        }
    }

    private void closeSessions() {
        for (Session session : getSessions()) {
            session.close();
        }
    }

    @Override
    public void shutdown() {
        LOG.info("shutdown invoked ..");
        stop();
        close();
    }

    public static void main(String[] args) throws Exception {
        String configName = null;
        if (args.length == 0) {
            configName = DEFAULT_CONFIG_FILE_NAME;
        } else if (args.length == 1) {
            configName = args[0];
        }
        if (configName == null) {
            System.out.println("Usage: " + NanoServer.class.getName() + " [configFile].");
            return;
        }

        Application server = new NanoServer(configName);
        server.open();
        server.start();
    }
}
