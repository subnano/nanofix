package net.nanofix.app;

import net.nanofix.config.ApplicationConfig;
import net.nanofix.netty.SocketConnector;
import net.nanofix.session.Session;

import java.util.List;

/**
 * The Application interface extends the Component interface by adding
 * an additional shutdown() method. All NanoFix applications should implement this interface
 */
public interface Application extends Component {

    ApplicationConfig getConfig();

    /**
     * Called by an application when it wants to shutdown
     */
    void shutdown();

    List<Session> getSessions();

    List<SocketConnector> getSocketConnectors();
}
