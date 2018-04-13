package net.nanofix.session;

import net.nanofix.config.SessionConfig;
import net.nanofix.core.connector.ConnectionState;
import net.nanofix.core.connector.MockConnector;
import net.nanofix.message.DetailedMessageFormatter;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXMessageFormatter;
import net.nanofix.message.FIXMessageMatcher;
import net.nanofix.store.MessageStore;
import net.nanofix.util.FIXMessageParser;
import net.nanofix.util.MessageAssertions;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AbstractSessionTest {

    private static final FIXMessageFormatter messageFormatter = new DetailedMessageFormatter();
    private static final FIXMessageParser MESSAGE_PARSER_WITH_SPACE = FIXMessageParser.createDefault().space();

    protected MockConnector connector;

    protected SessionConfig sessionConfig;

    protected ServerSession newServerSession(MockConnector connector, SessionConfig sessionConfig, MessageStore messageStore) {
        ServerSession session = new ServerSession(connector, sessionConfig, messageStore);
        connector.setSession(session);
        return session;
    }

    protected FIXMessageMatcher matcher(String expected) {
        StringBuilder sb = new StringBuilder(500);
        sb.append("8=").append(sessionConfig.getVersion()).append(" ");
        sb.append("49=").append(sessionConfig.getSenderCompID()).append(" ");
        sb.append("56=").append(sessionConfig.getTargetCompID()).append(" ");
        //sb.append("98=0").append(" ");
//        sb.append("108=").append(sessionConfig.getHeartbeatInterval()).append(" ");
        sb.append(expected);
        return new FIXMessageMatcher(sb.toString());
    }

    protected void setConnected(Session session, boolean connected) {
        connector.setConnected(connected);
        ConnectionState<AbstractSessionTest> state = connected ? new ConnectionState<>(this, ConnectionState.State.Connected)
                : new ConnectionState<>(this, ConnectionState.State.Connected);
        session.connectionStateChanged(state);
    }

    protected void mockInboundMessage(Session session, FIXMessage msg) {
        msg.setRawBytes(messageFormatter.toString(msg).getBytes());
        session.messageReceived(msg);
    }

    protected void verifyNoExceptionsCaught(SessionListener sessionListener) {
        verify(sessionListener, times(0)).exceptionCaught(any(Session.class), any(Throwable.class));
    }

    protected FIXMessage createExpectedMessage(String expectedMessage) {
        StringBuilder sb = new StringBuilder(500);
        sb.append("8=").append(sessionConfig.getVersion()).append(" ");
        sb.append("49=").append(sessionConfig.getSenderCompID()).append(" ");
        sb.append("56=").append(sessionConfig.getTargetCompID()).append(" ");
        //sb.append("98=0").append(" ");
//        sb.append("108=").append(sessionConfig.getHeartbeatInterval()).append(" ");
        sb.append(expectedMessage);
        return MESSAGE_PARSER_WITH_SPACE.parse(sb.toString());
    }

    protected void verifyMessageSent(String expectedMessageString) {
        //verify(spyConnector).send(argThat(matcher(expectedMessage)));
        FIXMessage sentMessage = connector.getSentMessage();
        // todo need to handle accessing sent messages better
        // todo assert message equals
        FIXMessage expectedMessage = createExpectedMessage(expectedMessageString);
        MessageAssertions.assertEquals(expectedMessage, sentMessage);
    }
}
