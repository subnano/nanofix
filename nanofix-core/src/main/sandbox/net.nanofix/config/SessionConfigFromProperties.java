package net.nanofix.config;

import java.util.Properties;

/**
 * User: Mark
 * Date: 02/04/12
 * Time: 18:30
 */
public class SessionConfigFromProperties {

    private static final String VERSION_PROP = "version";
    private static final String SENDER_COMP_ID_PROP = "sender-comp-id";
    private static final String TARGET_COMP_ID_PROP = "target-comp-id";
    private static final String HEARTBEAT_INTERVAL_PROP = "heartbeat-interval";

    public static SessionConfig createFromProperties(Properties properties) {
        if (properties == null) {
            throw new NullPointerException("properties is null");
        }
        SessionConfig config = new SessionConfigImpl();
        config.setVersion(properties.getProperty(VERSION_PROP));
        config.setSenderCompID(properties.getProperty(SENDER_COMP_ID_PROP));
        config.setTargetCompID(properties.getProperty(TARGET_COMP_ID_PROP));
        config.setHeartbeatInterval(Integer.parseInt(properties.getProperty(HEARTBEAT_INTERVAL_PROP)));
        return config;
    }

}
