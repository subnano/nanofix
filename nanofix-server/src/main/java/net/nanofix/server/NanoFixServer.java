package net.nanofix.server;

import net.nanofix.core.connector.*;
import net.nanofix.message.FIXMessage;
import net.nanofix.netty.ServerConnector;
import net.nanofix.session.SessionFactoryListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 06:02
 */
public class NanoFixServer implements MessageSender<FIXMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NanoFixServer.class);

    private final ServerConnector connector;
    private final SessionFactoryListener sessionFactoryListener;

    public NanoFixServer(int port) {
        this(new InetSocketAddress(port), null);
    }

    public NanoFixServer(int port, SessionFactoryListener sessionFactoryListener) {
        this(new InetSocketAddress(port), sessionFactoryListener);
    }

    public NanoFixServer(InetSocketAddress localAddress, SessionFactoryListener sessionFactoryListener) {
        this.sessionFactoryListener = sessionFactoryListener;
        this.connector = new ServerConnector(localAddress, sessionFactoryListener);
    }


    public void start() {
        connector.connect(null, null);
    }

    public void stop() {
        connector.disconnect();
    }

    @Override
    public void send(FIXMessage msg) {
        connector.send(msg);
    }

    public static void main(String[] args) {
        NanoFixServer server = new NanoFixServer(9880);
//        server.start(new LoggingConnectionListener(LOGGER), new LoggingMessageListener(LOGGER));
        server.start();
    }
}
