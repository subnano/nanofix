package net.nanofix.netty;

import io.netty.channel.ChannelHandlerContext;
import net.nanofix.core.connector.ConnectionListener;
import net.nanofix.core.connector.Connector;
import net.nanofix.core.connector.MessageListener;
import net.nanofix.core.netty.FIXChannelHandler;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.MsgTypes;
import net.nanofix.session.*;
import net.nanofix.util.ChannelHandlerNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mark
 * Date: 02/04/12
 * Time: 16:40
 */
public class AnonymousSessionHandler extends FIXChannelHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnonymousSessionHandler.class);
    private final Connector connector;
    private final SessionManager sessionManager;
    private final FIXLogonManager logonManager;

    public AnonymousSessionHandler(Connector connector,
                                   ConnectionListener connectionListener,
                                   MessageListener<FIXMessage> messageListener,
                                   SessionManager sessionManager,
                                   FIXLogonManager fixLogonManager) {
        super(connectionListener, messageListener);
        this.connector = connector;
        this.sessionManager = sessionManager;
        this.logonManager = fixLogonManager;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FIXMessage msg) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("messageReceived: {}", msg);
        }

        // first we make sure that we have a Logon message
        if (MsgTypes.Logon.equals(msg.getMsgType())) {
            processLogonMessage(msg);
        } else {
            // todo ensure we disconnect
            throw new FatalProtocolException("First message must be Logon, initiating disconnect");
        }
    }

    private void processLogonMessage(FIXMessage msg) {

        // extract SessionId from message
        SessionId sessionId;
        try {
            sessionId = SessionId.parse(msg);
        } catch (IllegalArgumentException e) {
            throw new FatalProtocolException(e);
        }

        // log the attempt
        LOGGER.info("logon attempt by {} from {}", sessionId, channel().remoteAddress());

        // see if the session details are valid
        Session session = sessionManager.getSession(connector, sessionId.reverse());
        if (session == null) {
            throw new FatalProtocolException("Session [" + sessionId + "] is not configured");
        }

/*
        // TODO make sure session has started (i.e. in schedule)
        if (!session.isActive()) {
            throw new FatalProtocolException("Session [" + session.getSessionId() + "] is not active");
        }
*/

        // see if already logged on - log a suitable message
        Session.SessionState sessionState = session.getSessionState();
        if (sessionState == Session.SessionState.LogonComplete) {
            throw new FatalProtocolException("Session [" + session.getSessionId()
                    + "] is already logged on");
        }

        // see if state permits
        if (!(sessionState == Session.SessionState.Disconnected)) {
            throw new FatalProtocolException("Session [" + session.getSessionId()
                    + "] is already in the process of logging in");
        }

        // see if logon message is permitted
        boolean logonValid = logonManager.logonValid(msg, session);
        if (!logonValid) {
            throw new FatalProtocolException("Logon denied");
        }

        // now we need to mark active session
        acceptLogon(session);

        session.addListener(messageListener());

        // forward message onto session
        session.messageReceived(msg);
    }

    private void acceptLogon(Session session) {
        LOGGER.info("Logon accepted for {}", session.getSessionId());

        ChannelHandlerContext context = context();
        if (context == null) {
            throw new FatalProtocolException("Cannot determine channel context processing logon");
        }
        context.pipeline().replace(this, ChannelHandlerNames.CHANNEL_HANDLER, new FIXChannelHandler(session, session));
    }

}