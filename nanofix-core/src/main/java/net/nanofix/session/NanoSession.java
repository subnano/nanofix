package net.nanofix.session;

import net.nanofix.message.DefaultFIXMessageFactory;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXMessageFactory;
import net.nanofix.message.NanoFIXMessage;
import net.nanofix.settings.SessionSettings;
import net.nanofix.socket.SocketConnector;
import net.nanofix.socket.SocketListener;
import net.nanofix.socket.SocketState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public class NanoSession implements Session, SocketListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NanoSession.class);

    private final AtomicReference<SessionState> stateHolder = new AtomicReference<SessionState>();

    private final SessionSettings settings;
    private final SessionListener listener;
    private final SocketConnector socketConnector;
    private final FIXMessageFactory fixMessageFactory;

    private final FIXMessage logonMessage;
    private final FIXMessage logoutMessage;

    public NanoSession(SessionSettings settings, SessionListener listener) {
        this.settings = settings;
        this.listener = listener;
        // TODO need to create socket connector in a factory from the settings
        this.socketConnector = null; //new NettySocketConnector(this, settings, this);
        this.fixMessageFactory = new DefaultFIXMessageFactory(settings);

        // create messages
        this.logonMessage = fixMessageFactory.createLogonMessage();
        this.logoutMessage = fixMessageFactory.createLogoutMessage();
        updateSessionState(SessionState.Disconnected);
    }

    @Override
    public void connect() {
        socketConnector.connect();
    }

    @Override
    public void send(FIXMessage msg) {
        socketConnector.send(msg);
    }

    @Override
    public void disconnect() {
        socketConnector.disconnect();
    }

    @Override
    public void stateUpdated(SocketState state) {
        switch (state) {
            case Connecting:
                updateSessionState(SessionState.Connecting);
                break;
            case Connected:
                sendLogon();
                updateSessionState(SessionState.LoggingOn);
                break;
            case Disconnected:
                updateSessionState(SessionState.Disconnected);
                break;
            default:
                LOGGER.warn("Unexpected state encountered: {}", state);

        }
    }

    private void updateSessionState(SessionState newState) {
        SessionState oldState = stateHolder.getAndSet(newState);
        LOGGER.debug("Session state updated: {} => {}", oldState, newState);
        listener.stateUpdated(this, newState);
    }

    private void sendLogon() {
        FIXMessage msg = new NanoFIXMessage(null, null);
        send(msg);
    }
}
