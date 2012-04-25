package net.nanofix.session;

import net.nanofix.app.Component;
import net.nanofix.config.SessionConfig;
import net.nanofix.message.FIXMessageFactory;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:36
 */
public interface Session extends Component {
    SessionConfig getConfig();
    FIXMessageFactory getFIXMessageFactory();

    String getVersion();
}
