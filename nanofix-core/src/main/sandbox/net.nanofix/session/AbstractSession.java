package net.nanofix.session;

import net.nanofix.app.AbstractComponent;
import net.nanofix.config.SessionConfig;
import net.nanofix.message.*;
import net.nanofix.netty.SocketConnector;
import net.nanofix.util.Listener;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:38
 */
public abstract class AbstractSession extends AbstractComponent implements Session {

    protected SessionConfig config;
    protected FIXMessageFactory fixMessageFactory;
    private final AtomicInteger lastSeqNumIn = new AtomicInteger(0);
    private final AtomicInteger lastSeqNumOut = new AtomicInteger(0);
    private final SessionID sessionID;

    protected LogonState logonState;
    protected Channel channel;
    protected SocketConnector connector;

    private final CopyOnWriteArrayList<MessageListener> messageListeners
            = new CopyOnWriteArrayList<MessageListener>();
    private final CopyOnWriteArrayList<ConnectionListener> connectionListeners
            = new CopyOnWriteArrayList<ConnectionListener>();
    private final CopyOnWriteArrayList<LogonStateListener> sessionStateListeners
            = new CopyOnWriteArrayList<LogonStateListener>();
    protected final HeartbeatManager heartbeatManager;

    public AbstractSession(SessionConfig config) {
        this.config = config;
        this.sessionID = createSessionID(config);
        fixMessageFactory = new DefaultFIXMessageFactory(this);
        heartbeatManager = HeartbeatManager.getInstance();
        setLogonState(LogonState.None);
    }

    @Override
    public void addListener(Listener listener) {
        if (listener instanceof MessageListener) {
            messageListeners.addIfAbsent((MessageListener) listener);
        }
        if (listener instanceof ConnectionListener) {
            connectionListeners.addIfAbsent((ConnectionListener) listener);
        }
        if (listener instanceof LogonStateListener) {
            sessionStateListeners.addIfAbsent((LogonStateListener) listener);
        }
    }

    @Override
    public void removeListener(Listener listener) {
        if (listener instanceof MessageListener) {
            messageListeners.remove(listener);
        }
        if (listener instanceof ConnectionListener) {
            connectionListeners.remove(listener);
        }
        if (listener instanceof LogonStateListener) {
            sessionStateListeners.remove(listener);
        }
    }

    public LogonState getLogonState() {
        return logonState;
    }

    private SessionID createSessionID(SessionConfig config) {
        return new SessionID(
                config.getVersion(),
                config.getSenderCompID(),
                config.getSenderSubID(),
                config.getSenderLocationID(),
                config.getTargetCompID(),
                config.getTargetSubID(),
                config.getTargetLocationID());
    }

    @Override
    public SessionID getSessionID() {
        return sessionID;
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
    public String getVersion() {
        return config.getVersion();
    }

    public int lastSeqNumIn() {
        return lastSeqNumIn.get();
    }

    protected int expectedSeqNumIn() {
        return lastSeqNumIn.get() + 1;
    }

    public void setSeqNumIn(int value) {
        lastSeqNumIn.set(value);
    }

    public int getSeqNumOut() {
        return lastSeqNumOut.get();
    }

    protected void setSeqNumOut(int value) {
        lastSeqNumOut.set(value);
    }

    protected int nextSeqNumOut() {
        return lastSeqNumOut.incrementAndGet();
    }

    @Override
    public void send(FIXMessage msg) {

        if (msg == null) {
            throw new IllegalArgumentException("nothing to send, msg is null");
        }

        // update SeqNum
        msg.setFieldValue(Tags.MsgSeqNum, nextSeqNumOut());

        // TODO what about PossDup & PossResend??

        // TODO persist message
        // TODO write message to log file

        if (LOG.isDebugEnabled()) {
            LOG.debug("sending {}", msg.toString());
        }

        ChannelFuture channelFuture = channel.write(msg);
        channelFuture.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                LOG.info("write future:" + future);
            }
        });
    }

    @Override
    public void setConnector(SocketConnector socketConnector) {
        this.connector = socketConnector;
    }

    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public Channel getChannel() {
        return channel;
    }

    @Override
    public void messageReceived(FIXMessage msg) {
        for (MessageListener listener : messageListeners) {
            listener.messageReceived(msg);
        }
    }

    @Override
    public void connectionStatus(ChannelHandler handler, boolean state) {
        for (ConnectionListener listener : connectionListeners) {
            listener.connectionStatus(handler, state);
        }
    }

    protected void setLogonState(LogonState state) {
        logonState = state;

        // notify listeners
        notifySessionStateListeners(this, state);
    }

    private void notifySessionStateListeners(AbstractSession session, LogonState state) {
        for (LogonStateListener listener : sessionStateListeners) {
            listener.logonStateChanged(session, state);
        }
    }


}
