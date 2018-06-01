package net.nanofix.session;

import net.nanofix.app.NanofixRuntimeException;
import net.nanofix.config.SessionConfig;
import net.nanofix.config.SessionConfigBuilder;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.MsgTypes;
import net.nanofix.message.Tags;
import net.nanofix.netty.SocketConnector;
import net.nanofix.util.TestHelper;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * User: Mark
 * Date: 07/05/12
 * Time: 21:47
 */
public class ClientSessionTest extends TestHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ClientSessionTest.class);
    private Session session;

    @Before
    public void setup() {
        session = new ClientSession(createSessionConfig(false));
        session.addListener(new LoggingListener());
    }

    private SessionConfig createSessionConfig(boolean resetSeqNum) {
        return new SessionConfigBuilder()
                .version(BEGIN_STRING)
                .senderCompID(SENDER_COMP_ID)
                .targetCompID(TARGET_COMP_ID)
                .heartbeatInterval(30)
                .resetSeqNum(true)
                .build();
    }

    @Test(expected = NanofixRuntimeException.class)
    public void testOpenNullConnector() throws Exception {
        // open session before connector is set
        session.open();
        fail("Expected NanofixRuntimeException");
    }

    @Test
    public void testConnectorState() throws Exception {
        SocketConnector connector = mock(SocketConnector.class);
        session.setConnector(connector);
        session.open();
        assertTrue(session.isOpen());
        verify(connector).open();

        session.start();
        assertTrue(session.isStarted());
        verify(connector).start();

        session.stop();
        assertTrue(session.isStopped());
        verify(connector).stop();

        session.close();
        assertTrue(session.isClosed());
        verify(connector).close();
    }

    @Test
    public void testConnectionStatus() throws Exception {
        LogonStateListener listener = mock(LogonStateListener.class);
        Channel channel = mock(Channel.class);
        when(channel.write(any(FIXMessage.class))).thenReturn(mock(ChannelFuture.class));
        session.setChannel(channel);
        session.addListener(listener);

        // this triggers sending a  Logon
        session.connectionStatus(mock(ChannelHandler.class), true);
        verify(listener).logonStateChanged(session, Session.LogonState.LogonSent);

        session.connectionStatus(mock(ChannelHandler.class), false);
        verify(listener).logonStateChanged(session, Session.LogonState.Disconnected);
    }

    @Test(expected = FatalProtocolException.class)
    public void testFirstMessageReceivedNotLogon() throws Exception {
        session.messageReceived(createHeartbeatMessage());
    }

    @Test(expected = FatalProtocolException.class)
    public void testLogonReceivedBeforeSent() throws Exception {
        session.messageReceived(createLogonMessage(-1));
    }

    @Test
    public void testNormalLogonReceived() throws Exception {
        LogonStateListener listener = mock(LogonStateListener.class);
        Channel channel = mock(Channel.class);
        when(channel.write(any(FIXMessage.class))).thenReturn(mock(ChannelFuture.class));
        session.setChannel(channel);
        session.addListener(listener);

        // this triggers sending a  Logon
        session.connectionStatus(mock(ChannelHandler.class), true);
        verify(listener).logonStateChanged(session, Session.LogonState.LogonSent);

        // now process the response
        session.messageReceived(createLogonMessage(1));
        verify(listener).logonStateChanged(session, Session.LogonState.LogonComplete);
    }

    @Test
    public void testInvalidLogonReceived() throws Exception {
        LogonStateListener listener = mock(LogonStateListener.class);
        Channel channel = mock(Channel.class);
        when(channel.write(any(FIXMessage.class))).thenReturn(mock(ChannelFuture.class));
        session.setChannel(channel);
        session.addListener(listener);

        // this triggers sending a  Logon
        session.connectionStatus(mock(ChannelHandler.class), true);
        verify(listener).logonStateChanged(session, Session.LogonState.LogonSent);

        // now process the response with an invalid SeqNum of -1
        session.messageReceived(createLogonMessage(-1));
        verify(listener).logonStateChanged(session, Session.LogonState.LogoutSent);
    }

    @Test
    public void testLogonReceivedSeqNumTooHigh() throws Exception {
        LogonStateListener listener = mock(LogonStateListener.class);
        Channel channel = mock(Channel.class);
        ArgumentCaptor<FIXMessage> msgCaptor = ArgumentCaptor.forClass(FIXMessage.class);
        when(channel.write(any(FIXMessage.class))).thenReturn(mock(ChannelFuture.class));

        session.setChannel(channel);
        session.addListener(listener);

        // this triggers sending a  Logon
        session.connectionStatus(mock(ChannelHandler.class), true);
        verify(listener).logonStateChanged(session, Session.LogonState.LogonSent);
        verify(channel).write(msgCaptor.capture());
        assertEquals(MsgTypes.Logon, msgCaptor.getValue().getMsgType());

        // now process the response
        session.messageReceived(createLogonMessage(3));
        verify(listener).logonStateChanged(session, Session.LogonState.LogonComplete);
        verify(channel, times(2)).write(msgCaptor.capture());

        // make sure resend was sent
        FIXMessage msg = msgCaptor.getValue();
        assertEquals(MsgTypes.ResendRequest, msg.getMsgType());
    }

    private FIXMessage createHeartbeatMessage() {
        FIXMessage msg = new StandardFIXMessage(MsgTypes.Heartbeat);
        msg.setFieldValue(Tags.SenderCompID, SENDER_COMP_ID);
        msg.setFieldValue(Tags.TargetCompID, TARGET_COMP_ID);
        return msg;
    }

    private FIXMessage createLogonMessage(long seqNum) {
        FIXMessage msg = new StandardFIXMessage(MsgTypes.Logon);
        msg.setFieldValue(Tags.SenderCompID, SENDER_COMP_ID);
        msg.setFieldValue(Tags.TargetCompID, TARGET_COMP_ID);
        msg.setMsgSeqNum(seqNum);
        return msg;
    }

}