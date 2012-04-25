package net.nanofix.session;

import net.nanofix.app.AbstractComponent;
import net.nanofix.config.SessionConfig;
import net.nanofix.message.FIXMessageFactory;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:38
 */
public abstract class AbstractSession extends AbstractComponent implements Session {

    private final SessionConfig config;
    private FIXMessageFactory fixMessageFactory;

    public AbstractSession(SessionConfig config) {
        this.config = config;
    }

    public SessionConfig getConfig() {
        return config;
    }

    public void setFixMessageFactory(FIXMessageFactory fixMessageFactory) {
        this.fixMessageFactory = fixMessageFactory;
    }

    public FIXMessageFactory getFIXMessageFactory() {
        return fixMessageFactory;
    }
}
