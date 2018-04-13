package net.nanofix.session;

import io.netty.channel.Channel;
import net.nanofix.core.connector.MessageListener;
import net.nanofix.core.connector.MockConnector;
import net.nanofix.message.FIXMessage;
import net.nanofix.store.MemoryMessageStore;
import net.nanofix.store.MessageStore;
import net.nanofix.util.NanoTestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.nanofix.session.Session.SessionState.*;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class ServerSessionTest extends AbstractSessionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockConnector.class);

    private ServerSession serverSession;

    private TestMessageFactory messageFactory;

    private MessageListener messageListener;

    @Mock
    private Channel channel;

    private SessionListener sessionListener;

    private MessageStore messageStore;

    @Before
    public void setUp() throws Exception {
        sessionConfig = NanoTestData.DEFAULT_CLIENT_SESSION_INFO;
        connector = new MockConnector(channel);
        messageStore = new MemoryMessageStore();
        serverSession = newServerSession(connector, sessionConfig, messageStore);
        messageFactory = new TestMessageFactory(sessionConfig);
        sessionListener = spy(new LoggingSessionListener(LOGGER));
        serverSession.addListener(messageListener);
        serverSession.addListener(sessionListener);
    }

    @Test
    public void invalidLogonBeforeConnected() throws Exception {
        FIXMessage msg = messageFactory.createLogonMessage(true);
        mockInboundMessage(serverSession, msg);
        serverSession.checkState(Disconnected);
    }

    @Test
    public void invalidMessageBeforeLogon() throws Exception {
        setConnected(serverSession, true);
        FIXMessage msg = messageFactory.createHeartbeatMessage();
        mockInboundMessage(serverSession, msg);
        serverSession.checkState(Disconnected);
    }

    private void verifyNormalLogon(boolean reset) throws SessionException {
        setConnected(serverSession, true);
        FIXMessage msg = messageFactory.createLogonMessage(reset);
        mockInboundMessage(serverSession, msg);
        verifyNoExceptionsCaught(sessionListener);
        verifyMessageSent("35=A 34=1 141=Y");
        serverSession.checkState(LogonComplete);
    }

    @Test
    public void logonSuccessfully() throws Exception {
        verifyNormalLogon(true);
    }

    @Test
    public void logonLogoffSuccessfully() throws Exception {
        verifyNormalLogon(true);
        FIXMessage msg = messageFactory.createLogoutMessage("Normal operation");
        mockInboundMessage(serverSession, msg);
        verifyNoExceptionsCaught(sessionListener);
        verifyMessageSent("35=5 34=2");
        serverSession.checkState(LogoutComplete);
    }

    @Test
    public void logonSeqNumTooLow() throws Exception {
        verifyNormalLogon(true);
        mockInboundMessage(serverSession, messageFactory.createHeartbeatMessage());
        FIXMessage msg = messageFactory.createLogoutMessage("Normal operation");
        mockInboundMessage(serverSession, msg);
        verifyMessageSent("35=5 34=2");
        serverSession.checkState(LogoutComplete);
    }
}
