package net.nanofix.socket;

import net.nanofix.message.FIXMessage;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 18:36
 */
public interface SocketConnector {

    String getBindAddress();

    void connect();

    public void send(FIXMessage msg);

    void disconnect();
}
