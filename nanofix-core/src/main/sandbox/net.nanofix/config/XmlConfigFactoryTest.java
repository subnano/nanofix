package net.nanofix.config;

import net.nanofix.message.FIXVersion;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * User: Mark
 * Date: 04/04/12
 * Time: 18:49
 */
public class XmlConfigFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFilename() throws Exception {
        new XmlConfigFactory().load("");
    }

    @Test
    public void testLoad() throws Exception {
        XmlConfigFactory configFactory = new XmlConfigFactory();
        ApplicationConfig config = configFactory.load("test-config.xml");
        assertNotNull("config", config);
        assertThat(config.getId(), is("NanoServer"));
        assertThat(config.getSessionConfigs(), notNullValue());
        assertThat(config.getSessionConfigs().length, is(2));

        SessionConfig sessionConfig = config.getSessionConfigs()[0];
        assertNotNull("sessionConfig", config);
        assertThat("version", sessionConfig.getVersion(), is(FIXVersion.FIX44));
        assertThat("senderCompID", sessionConfig.getSenderCompID(), is("BROKER"));
        assertThat("heartbeatInterval", sessionConfig.getHeartbeatInterval(), is(60));

        assertThat(sessionConfig.getConnectors(), notNullValue());
        assertThat(sessionConfig.getConnectors().size(), is(1));
        ConnectionConfig connector = sessionConfig.getConnectors().get(0);
        assertNotNull("connection", connector);
        assertEquals("port", 9001, connector.getPort());
        assertTrue("tcpNoDelay", connector.isTcpNoDelay());
    }
}
