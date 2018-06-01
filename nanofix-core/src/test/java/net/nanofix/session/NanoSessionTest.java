package net.nanofix.session;

import net.nanofix.message.FIXMessage;
import net.nanofix.message.FIXVersion;
import net.nanofix.message.MsgTypes;
import net.nanofix.settings.SessionSettings;
import net.nanofix.settings.SessionSettingsBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class NanoSessionTest implements SessionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NanoSessionTest.class);

    @Test
    void sessionConnect() {
        // INFO: Listening for connections at 0.0.0.0/0.0.0.0:9880 for session(s) [FIX.4.4:EXEC->BANZAI]
        // specify the session settings to be used
        SessionSettings settings = new SessionSettingsBuilder()
                .host("localhost")
                .port(9880)
                .fixVersion(FIXVersion.FIX44)
                .targetCompId("EXEC")
                .senderCompId("BANZAI")
                .heartbeatInterval(30)
                .build();

        // TODO get message factory from session?

        //FIXMessage msg = new StandardFIXMessage(MsgTypes.Logon);
        Session session = new NanoSession(settings, this);
        session.connect();
        // on
        //session.send(msg);
        session.disconnect();
    }

    @Override
    public void stateUpdated(Session session, SessionState state) {
        LOGGER.info("{} - {}", session, state);
    }

    @Override
    public void messageReceived(Session session, FIXMessage message) {
        LOGGER.info("{} - {}", session, message);
    }
}