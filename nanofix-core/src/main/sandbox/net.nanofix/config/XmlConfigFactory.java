package net.nanofix.config;

import com.thoughtworks.xstream.XStream;
import net.nanofix.processor.MessageProcessor;
import net.nanofix.schedule.ResetTask;
import net.nanofix.schedule.StartTask;
import net.nanofix.schedule.StopTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * User: Mark
 * Date: 04/04/12
 * Time: 18:01
 */
public class XmlConfigFactory implements ApplicationConfigFactory {

    private static final Logger LOG = LoggerFactory.getLogger(XmlConfigFactory.class);

    private XStream xstream;

    public XmlConfigFactory() {
        this.xstream = new XStream();
        xstream.processAnnotations(ApplicationConfigImpl.class);
        xstream.processAnnotations(SessionConfigImpl.class);
        xstream.processAnnotations(ServerSocketConfig.class);
        xstream.processAnnotations(ClientSocketConfig.class);
        xstream.processAnnotations(MessageProcessor.class);
        xstream.processAnnotations(StartTask.class);
        xstream.processAnnotations(StopTask.class);
        xstream.processAnnotations(ResetTask.class);

        xstream.addDefaultImplementation(ApplicationConfigImpl.class, ApplicationConfig.class);
        xstream.addDefaultImplementation(SessionConfigImpl.class, SessionConfig.class);
        xstream.addDefaultImplementation(ServerSocketConfig.class, ConnectionConfig.class);
        xstream.addDefaultImplementation(ClientSocketConfig.class, ConnectionConfig.class);
    }

    @Override
    public ApplicationConfig load(String filename) {
        LOG.info("loading config from file [{}]", filename);

        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("filename is null or empty(" + filename + ")");
        }

        // assumes that your config file is in the classpath.
        InputStream stream = this.getClass().getResourceAsStream(filename);
        if (stream == null) {
            stream = this.getClass().getClassLoader().getResourceAsStream(filename);
        }
        if (stream == null) {
            throw new IllegalArgumentException("cannot find config file [" + filename + "] on classpath");
        }
        return load(stream);
    }

    public ApplicationConfig load(InputStream stream) {
        ApplicationConfig config = (ApplicationConfig) xstream.fromXML(stream);
        validate(config);
        return config;
    }

    private void validate(ApplicationConfig config) {
        validateConnectors(config);
    }

    private void validateConnectors(ApplicationConfig config) {
        for (SessionConfig sessionConfig : config.getSessionConfigs()) {
            for (ConnectionConfig connector : sessionConfig.getConnectors()) {
            }
        }
    }
}
