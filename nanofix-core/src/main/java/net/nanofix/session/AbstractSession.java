package net.nanofix.session;

import net.nanofix.app.AbstractComponent;
import net.nanofix.config.SessionConfig;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXMessageFactory;
import net.nanofix.netty.SocketConnector;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:38
 */
public abstract class AbstractSession extends AbstractComponent implements Session {

    private final SessionConfig config;
    private FIXMessageFactory fixMessageFactory;
    private SocketConnector connector;
    private final AtomicInteger lastSeqNumIn = new AtomicInteger(-1);
    private final AtomicInteger lastSeqNumOut = new AtomicInteger(-1);

    public AbstractSession(SessionConfig config) {
        this.config = config;
    }

    public SessionConfig getConfig() {
        return config;
    }

    public void setFixMessageFactory(FIXMessageFactory fixMessageFactory) {
        this.fixMessageFactory = fixMessageFactory;
    }

    public FIXMessageFactory getFIXMessageFactory() {
        return fixMessageFactory;
    }

    @Override
    public SocketConnector getConnector() {
        return connector;
    }

    @Override
    public void setConnector(SocketConnector connector) {
        this.connector = connector;
    }

    protected int getNextSeqNumOut() {
        return lastSeqNumOut.getAndIncrement();
    }

    public int getLastSeqNumIn() {
        return lastSeqNumIn.get();
    }

    public int getLastSeqNumOut() {
        return lastSeqNumIn.get();
    }

    @Override
    public void send(FIXMessage msg) {
        // TODO persist message
        // TODO write message to log file
        getConnector().send(msg);
    }
}
