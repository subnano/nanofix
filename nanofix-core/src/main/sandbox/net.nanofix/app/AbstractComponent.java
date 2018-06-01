package net.nanofix.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 06:32
 */
public abstract class AbstractComponent implements Component {

    protected Logger LOG;
    private ComponentState state;

    public AbstractComponent() {
        this.state = ComponentState.New;
        LOG = LoggerFactory.getLogger(this.getClass());
    }


    @Override
    public void open() {
        if (state != ComponentState.New) {
            throw new ComponentStateException(this.getClass().getSimpleName() + " is already open");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("open()");
        }
        state = ComponentState.Open;
    }

    @Override
    public boolean isOpen() {
        return ComponentState.Open == state;
    }

    @Override
    public void start() {
        if (state != ComponentState.Open) {
            throw new ComponentStateException("component is not open");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("start()");
        }
        state = ComponentState.Started;
    }

    @Override
    public boolean isStarted() {
        return ComponentState.Started == state;
    }

    @Override
    public void refresh() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("refresh()");
        }
    }

    @Override
    public void stop() {
        if (state != ComponentState.Started) {
            throw new ComponentStateException("component is not started");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("stop()");
        }
        state = ComponentState.Stopped;
    }

    @Override
    public boolean isStopped() {
        return ComponentState.Stopped == state;
    }

    @Override
    public void close() {
        if (state != ComponentState.Stopped) {
            throw new ComponentStateException("component is not stopped");
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("close()");
        }
        state = ComponentState.Closed;
    }

    @Override
    public boolean isClosed() {
        return ComponentState.Closed == state;
    }

    @Override
    public ComponentState getState() {
        return state;
    }

}
