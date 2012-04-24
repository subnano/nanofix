package net.nanofix.app;

import net.nanofix.app.session.DefaultSessionFactory;
import net.nanofix.app.session.Session;
import net.nanofix.app.session.SessionFactory;
import net.nanofix.config.*;
import net.nanofix.netty.ClientSocketConnector;
import net.nanofix.netty.ServerSocketConnector;
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
public class NanoFixServer extends AbstractComponent implements  Application {

    private final Logger LOG;
    private final SessionFactory sessionFactory;
    private final List<Session> sessions = new ArrayList<Session>();
    private final List<SocketConnector> socketConnectors = new ArrayList<SocketConnector>();
    private ApplicationConfig config;

    private static String DEFAULT_CONFIG_FILE_NAME = "nanofix.config";

    public NanoFixServer() {
        LOG = LoggerFactory.getLogger(this.getClass());
        sessionFactory = new DefaultSessionFactory();
    }

    public NanoFixServer(String configName) throws FileNotFoundException {
        this();
        this.config = loadConfig(configName);
    }

    public NanoFixServer(InputStream inputStream) {
        this();
        this.config = loadConfig(inputStream);
    }

    private ApplicationConfig loadConfig(String configName) throws FileNotFoundException {
        InputStream inputStream = null;
        LOG.debug("trying to find [{}] on classpath", configName);
        inputStream = NanoFixServer.class.getResourceAsStream(configName);
        if (inputStream == null) {
            LOG.debug("not found, trying to load from file [{}]", configName);
            inputStream = new FileInputStream(configName);
        }
        return loadConfig(inputStream);
    }

    private ApplicationConfig loadConfig(InputStream inputStream) {
        XmlConfigFactory configFactory = new XmlConfigFactory();
        ApplicationConfig config = configFactory.load(inputStream);
        createSessions(config.getSessionConfigs());
        createServerConnectors(config.getConnectors());
        return config;
    }

    public ApplicationConfig getConfig() {
        return config;
    }

    private void createSessions(SessionConfig[] sessionConfigs) {
        if (sessionConfigs == null || sessionConfigs.length == 0) {
            throw new IllegalArgumentException("SessionConfigs array is null or empty");
        }
        for (SessionConfig sessionConfig : sessionConfigs) {
            LOG.info("creating session from config - {}", sessionConfig.toString());
            sessions.add(sessionFactory.createSession(sessionConfig));
        }
    }

    private void createServerConnectors(ConnectionConfig[] connectorConfigs) {
        if (connectorConfigs == null || connectorConfigs.length == 0) {
            throw new IllegalArgumentException("ConnectorConfigs array is null or empty");
        }
        for (ConnectionConfig connectorConfig : connectorConfigs) {
            if (connectorConfig instanceof ServerSocketConfig) {
                LOG.info("creating server connector from config - {}", connectorConfig.toString());
                socketConnectors.add(new ServerSocketConnector((ServerSocketConfig)connectorConfig));
            }
            else if (connectorConfig instanceof ClientSocketConfig) {
                LOG.info("creating server connector from config - {}", connectorConfig.toString());
                socketConnectors.add(new ClientSocketConnector((ClientSocketConfig)connectorConfig));
            }
        }
    }

    public List<Session> getSessions() {
        return sessions;
    }

    @Override
    public void open() {
        // open services?
        // open other components?
        super.open();
        openSessions();
        openConnectors();
    }

    @Override
    public void start() {
        super.start();
        startSessions();
        startConnectors();
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
    }

    @Override
    public void close() {
        super.close();
        closeSessions();
    }

    private void openSessions() {
        for (Session session : getSessions()) {
            session.open();
        }
    }

    private void openConnectors() {
        for (SocketConnector connector : socketConnectors) {
            connector.open();
        }
    }

    private void startSessions() {
        for (Session session : getSessions()) {
            session.start();
        }
    }

    private void startConnectors() {
        for (SocketConnector connector : socketConnectors) {
            connector.start();
        }
    }

    private void stopConnectors() {
        for (SocketConnector connector : socketConnectors) {
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
            System.out.println("Usage: " + NanoFixServer.class.getName() + " [configFile].");
            return;
        }

        Application server = new NanoFixServer(configName);
        server.open();
        server.start();
    }
}
