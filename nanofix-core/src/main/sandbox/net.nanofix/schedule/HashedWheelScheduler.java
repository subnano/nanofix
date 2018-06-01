package net.nanofix.schedule;

import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * User: Mark
 * Date: 09/05/12
 * Time: 17:44
 */
public class HashedWheelScheduler implements Scheduler {
    private static final Logger LOG = LoggerFactory.getLogger(HashedWheelScheduler.class);

    private static HashedWheelScheduler singleton = new HashedWheelScheduler();

    private final HashedWheelTimer hashedWheelTimer;

    private HashedWheelScheduler() {
        hashedWheelTimer = new HashedWheelTimer();
    }

    public static HashedWheelScheduler getInstance() {
        return singleton;
    }

    public void start() {
        hashedWheelTimer.start();
    }

    @Override
    public Timeout schedule(TimerTask task, long delay, TimeUnit unit) {
        return hashedWheelTimer.newTimeout(task, delay, unit);
    }

    public Set<Timeout> stop() {
        return hashedWheelTimer.stop();
    }
}
