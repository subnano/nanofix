package net.nanofix.app.session;

import net.nanofix.app.AbstractComponent;
import net.nanofix.config.SessionConfig;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 16:38
 */
public abstract class AbstractSession extends AbstractComponent implements Session {

    private final SessionConfig config;

    public AbstractSession(SessionConfig config) {
        this.config = config;
    }
}
