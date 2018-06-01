package net.nanofix.config;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:18
 */
public abstract class AbstractSocketConfig implements ConnectionConfig {

    @XStreamAsAttribute
    private int port;

    @XStreamAsAttribute
    private String bindAddress;

    @XStreamAsAttribute
    private boolean tcpNoDelay;

    @XStreamAsAttribute
    private int receiveBufferSize;

    @XStreamAsAttribute
    private int sendBufferSize;

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getBindAddress() {
        return bindAddress;
    }

    @Override
    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    @Override
    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    @Override
    public int getSendBufferSize() {
        return sendBufferSize;
    }

}
