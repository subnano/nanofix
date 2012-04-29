package net.nanofix.session;

import net.nanofix.netty.AbstractSocketConnector;
import net.nanofix.netty.SocketConnector;

/**
 * User: Mark
 * Date: 26/04/12
 * Time: 22:04
 */
public interface ConnectorListener {

    void onConnectorStatus(SocketConnector connector, boolean success);

}
