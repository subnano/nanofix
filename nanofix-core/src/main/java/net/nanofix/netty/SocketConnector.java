package net.nanofix.netty;

import net.nanofix.app.Component;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 18:36
 */
public interface SocketConnector extends Component {
    int getPort();
    String getBindAddress();
}
