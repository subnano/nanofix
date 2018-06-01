package net.nanofix.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * User: Mark
 * Date: 02/04/12
 * Time: 21:37
 */
public class SessionConfigFactory {

    public static SessionConfig createSessionConfig(File propertyFile) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(propertyFile));
        return SessionConfigFromProperties.createFromProperties(properties);
    }


}
