package net.nanofix.schedule;

import net.nanofix.session.HeartbeatManager;
import net.nanofix.session.Session;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * User: Mark
 * Date: 09/05/12
 * Time: 17:57
 */
public class HeartbeatTask implements TimerTask {

    private static final Logger LOG = LoggerFactory.getLogger(HeartbeatTask.class);
    private final Session session;
    private final int interval;

    public HeartbeatTask(Session session, int interval) {
        this.session = session;
        this.interval = interval;
    }

    @Override
    public void run(Timeout timeout) throws Exception {

        long deadline = System.currentTimeMillis();

        if (LOG.isDebugEnabled()) {
            LOG.debug("sending heartbeat for Session {}", session.getSessionID());
        }
        session.send(session.getFIXMessageFactory().createHeartbeatMessage());

        // TODO reschedule ...??
        timeout.getTimer().newTimeout(this, interval, TimeUnit.SECONDS);
    }
}
