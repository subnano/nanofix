package net.nanofix.util;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: Mark
 * Date: 02/05/12
 * Time: 07:25
 */
public class ListenerSupport<T extends Listener> {

    private final CopyOnWriteArrayList<T> listeners = new CopyOnWriteArrayList<T>();

    public void addListener(T listener) {
        listeners.addIfAbsent(listener);
    }

    public void removeListener(T listener) {
        listeners.remove(listener);
    }

    public List<T> getListeners() {
        return listeners;
    }
}
