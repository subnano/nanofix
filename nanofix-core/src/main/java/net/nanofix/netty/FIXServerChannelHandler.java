package net.nanofix.netty;

import net.nanofix.app.Application;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.MsgTypes;
import net.nanofix.session.*;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

/**
 * User: Mark
 * Date: 02/04/12
 * Time: 16:40
 */
public class FIXServerChannelHandler extends FIXChannelHandler {
    private final FIXLogonManager logonManager;
    private final SessionResolver sessionResolver;

    private boolean logonReceived = false;
    private Session session;
    private ChannelHandlerContext channelContext;

    public FIXServerChannelHandler(Application application, SocketConnector connector) {
        super(application, connector);
        sessionResolver = new SessionResolver(application);
        logonManager = new AnonymousFIXLogonManager();
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.channelContext = ctx;
        super.channelOpen(ctx, e);
    }

    public void messageReceived(FIXMessage msg) {

        // first we make sure that we have a Logon message
        if (logonReceived) {
            session.messageReceived(msg);
        }
        else {
            if (MsgTypes.Logon.equals(msg.getMsgType())) {
                processLogonMessage(msg);
            }
            else {
                throw new FatalProtocolException("First message must be Logon, initiating disconnect");
            }
        }
    }

    private void processLogonMessage(FIXMessage msg) {

        // extract SessionID from message
        SessionID sessionID;
        try {
            sessionID = SessionID.parse(msg);
        } catch (IllegalArgumentException e) {
            throw new FatalProtocolException(e);
        }

        // log the attempt
        LOG.info("logon attempt by {} from {}", sessionID, channel.getRemoteAddress());

        // see if the session details are valid
        Session session = sessionResolver.resolve(sessionID);
        if (session == null) {
            throw new FatalProtocolException("Session [" + sessionID + "] is not configured");
        }

        // make sure it is a server session
        if (!(session instanceof ServerSession)) {
            throw new FatalProtocolException("Session [" + session.getSessionID()
                    + "] does not support incoming connections");
        }

        // make sure session has started (i.e. in schedule)
        if (!session.isStarted()) {
            throw new FatalProtocolException("Session [" + session.getSessionID()
                    + "] is not active");
        }

        // see if already logged on - log a suitable message
        Session.LogonState logonState = session.getLogonState();
        if (logonState == Session.LogonState.LogonComplete) {
            throw new FatalProtocolException("Session [" + session.getSessionID()
                    + "] is already logged on");
        }

        // see if state permits
        if (!(logonState == Session.LogonState.None || logonState == Session.LogonState.Disconnected)) {
            throw new FatalProtocolException("Session [" + session.getSessionID()
                    + "] is already in the process of logging in");
        }

        // see if logon message is permitted
        boolean logonValid = logonManager.logonValid(msg, session);
        if (!logonValid) {
            throw new FatalProtocolException("Logon denied");
        }

        // now we need to mark active session
        acceptLogon(session);

        // forward message onto server
        session.messageReceived(msg);
    }

    private void acceptLogon(Session session) {
        LOG.info("logon accepted for {}" + session);

        this.session = session;
        this.setListener(session);
        logonReceived = true;

        ServerSession serverSession = (ServerSession) session;
        serverSession.setChannel(channel);

        channelContext.getPipeline().addAfter("message-decoder", "message-encoder",
                new FIXMessageEncoder(session.getVersion()));
    }

}