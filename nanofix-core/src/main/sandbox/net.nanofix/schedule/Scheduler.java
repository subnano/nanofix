package net.nanofix.schedule;

import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * User: Mark
 * Date: 09/05/12
 * Time: 17:39
 */
public interface Scheduler {
    Timeout schedule(TimerTask task, long delay, TimeUnit unit);
}
