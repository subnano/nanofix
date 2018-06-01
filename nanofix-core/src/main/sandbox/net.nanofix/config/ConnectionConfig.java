package net.nanofix.config;

/**
 * User: Mark
 * Date: 05/04/12
 * Time: 05:53
 */
public interface ConnectionConfig {

    int getPort();

    String getBindAddress();

    boolean isTcpNoDelay();

    int getReceiveBufferSize();

    int getSendBufferSize();

}
