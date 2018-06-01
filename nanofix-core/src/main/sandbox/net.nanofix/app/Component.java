package net.nanofix.app;

/**
 * User: Mark
 * Date: 03/04/12
 * Time: 06:16
 */
public interface Component {
    void open();
    boolean isOpen();
    void start();
    boolean isStarted();
    void refresh();
    void stop();
    boolean isStopped();
    void close();
    boolean isClosed();
    ComponentState getState();
}
