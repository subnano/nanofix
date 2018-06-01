package net.nanofix.session;

import net.nanofix.message.FIXMessage;

public interface Session {
    void connect();
    void send(FIXMessage msg);
    void disconnect();
}
