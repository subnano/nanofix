package net.nanofix.app;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * User: Mark
 * Date: 01/05/12
 * Time: 21:41
 */
public class ApplicationManager {

    private static ConcurrentMap<String,Application> map = new ConcurrentHashMap<String, Application>();

    public static Application addApplication(String id, Application application) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id is null or empty");
        }
        if (application == null) {
            throw new IllegalArgumentException("Application is null");
        }
        if (map.containsKey(id)) {
            throw new IllegalArgumentException("Application id [" + id + "] already exists");
        }
        map.put(id, application);

        return application;
    }

    public static Application getApplication(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id is null or empty");
        }
        if (!map.containsKey(id)) {
            throw new IllegalArgumentException("Application id [" + id + "] does not exist");
        }
        return map.get(id);
    }

    public static Application removeApplication(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id is null or empty");
        }
        if (!map.containsKey(id)) {
            throw new IllegalArgumentException("Application id [" + id + "] does not exist");
        }
        return map.remove(id);
    }
}
