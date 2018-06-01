package net.nanofix.app;

import net.nanofix.session.Session;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * User: Mark
 * Date: 20/04/12
 * Time: 16:18
 */
public class NanoFixServerTest {

    @Test
    public void testNanofixServer() throws Exception {
        NanoServer server = new NanoServer("/test-config.xml");
        assertThat("server", server.getConfig(), notNullValue());

        List<Session> sessions = server.getSessions();
        assertThat("sessions", sessions.size(), is(2));

        // open
        server.open();
        assertThat("opened", server.isOpen(), is(true));

        // start
        server.start();
        assertThat("started", server.isStarted(), is(true));

        // stop
        server.stop();
        assertThat("stopped", server.isStopped(), is(true));
    }
}
