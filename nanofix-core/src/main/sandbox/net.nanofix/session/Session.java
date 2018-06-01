package net.nanofix.session;

import net.nanofix.app.Component;
import net.nanofix.config.SessionConfig;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXMessageFactory;
import net.nanofix.netty.SocketConnector;
import net.nanofix.util.Listener;
import org.jboss.netty.channel.Channel;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:36
 */
public interface Session extends Component, ConnectionListener {


    public enum LogonState {
        None, Disconnected,
        LogonSent, LogonReceived, LogonComplete,
        LogoutSent, LogoutReceived, LogoutComplete }

    LogonState getLogonState();

    SessionID getSessionID();

    SessionConfig getConfig();

    FIXMessageFactory getFIXMessageFactory();

    String getVersion();

    int lastSeqNumIn();

    int getSeqNumOut();

    void messageReceived(FIXMessage msg);

    void send(FIXMessage msg);

    void setConnector(SocketConnector socketConnector);

    void setChannel(Channel channel);

    Channel getChannel();

    void addListener(Listener listener);

    void removeListener(Listener listener);

}
