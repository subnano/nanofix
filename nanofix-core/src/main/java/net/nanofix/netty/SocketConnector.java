package net.nanofix.netty;

import net.nanofix.app.Component;
import net.nanofix.message.FIXMessage;
import net.nanofix.session.ClientSession;
import net.nanofix.session.ConnectorListener;
import net.nanofix.session.Session;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 18:36
 */
public interface SocketConnector extends Component {

    String getBindAddress();

    void setSession(Session session);

    Session getSession();

    public void send(FIXMessage msg);

    void addListener(ConnectorListener listener);

    void removeListener(ConnectorListener listener);
}
