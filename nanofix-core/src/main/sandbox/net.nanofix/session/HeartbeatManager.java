package net.nanofix.session;

import net.nanofix.schedule.HashedWheelScheduler;
import net.nanofix.schedule.HeartbeatTask;
import net.nanofix.schedule.Scheduler;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * User: Mark
 * Date: 09/05/12
 * Time: 18:26
 */
public class HeartbeatManager {
    private static final Logger LOG = LoggerFactory.getLogger(HeartbeatManager.class);

    private static final HeartbeatManager singleton = new HeartbeatManager();

    private final Scheduler scheduler;
    private Map<Session, Timeout> sessions2Timeout;

    private HeartbeatManager() {
        scheduler = HashedWheelScheduler.getInstance();
        sessions2Timeout = new ConcurrentHashMap<Session, Timeout>();
    }

    public static HeartbeatManager getInstance() {
        return singleton;
    }

    public void start(Session session) {

        int interval = session.getConfig().getHeartbeatInterval();
        TimerTask task = new HeartbeatTask(session, interval);

        if (LOG.isDebugEnabled()) {
            LOG.debug("starting heartbeat timer for {} every {} seconds", session.getSessionID(), interval);
        }

        Timeout timeout = scheduler.schedule(task, interval, TimeUnit.SECONDS);
        sessions2Timeout.put(session, timeout);
    }

    public void stop(Session session) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("stopping heartbeat timer for {}", session.getSessionID());
        }

        Timeout timeout = sessions2Timeout.remove(session);
        if (!timeout.isCancelled()) {
            timeout.cancel();
        }
    }
}
