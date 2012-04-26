package net.nanofix.session;

import net.nanofix.app.AbstractComponent;
import net.nanofix.config.SessionConfig;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXMessageFactory;
import net.nanofix.message.Tags;
import net.nanofix.netty.SocketConnector;
import net.nanofix.util.DateTimeGenerator;
import net.nanofix.util.DefaultTimeGenerator;

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
    private DateTimeGenerator timeGenerator = new DefaultTimeGenerator();
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

    protected DateTimeGenerator getTimeGenerator() {
        return timeGenerator;
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

    protected void addHeader(FIXMessage msg) {
        addCounterParties(msg);
        addSeqNum(msg, getNextSeqNumOut());
        addSessionIdentifiers(msg);
        addSendingTime(msg);
    }

    private void addSeqNum(FIXMessage msg, int seqNum) {
        msg.setFieldValue(Tags.MsgSeqNum, seqNum);
    }

    protected void addSendingTime(FIXMessage msg) {
        msg.setFieldValue(Tags.SendingTime, getTimeGenerator().getUtcTime(isUseMillisInTimeStamp()));
    }

    protected boolean isUseMillisInTimeStamp() {
        // TODO check for FIX version 4.2+ here
        return getConfig().isUseMillisInTimeStamp();
    }

    private void addCounterParties(FIXMessage msg) {
        msg.setFieldValue(Tags.SenderCompID, getConfig().getSenderCompID());
        msg.setFieldValue(Tags.TargetCompID, getConfig().getTargetCompID());
    }

    private void addSessionIdentifiers(FIXMessage msg) {
    }

}
