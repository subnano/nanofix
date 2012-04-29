package net.nanofix.session;

import net.nanofix.app.Component;
import net.nanofix.config.SessionConfig;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXMessageFactory;
import net.nanofix.netty.SocketConnector;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:36
 */
public interface Session extends Component, ConnectorListener  {

    SessionConfig getConfig();

    FIXMessageFactory getFIXMessageFactory();

    SocketConnector getConnector();

    void setConnector(SocketConnector connector);

    String getVersion();

    int getLastSeqNumIn();

    int getLastSeqNumOut();

    void send(FIXMessage msg);

}
