package net.nanofix.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * User: Mark
 * Date: 05/04/12
 * Time: 05:53
 */
@XStreamAlias("ClientSocket")
public class ClientSocketConfig extends AbstractSocketConfig implements ConnectionConfig {

    @XStreamAsAttribute
    private String hostname;

    @XStreamAsAttribute
    private boolean resetSeqNum;

    private String[] allowedAddresses;
    private String ignoredAddresses;

    public String getHostname() {
        return hostname;
    }

    public boolean isResetSeqNum() {
        return resetSeqNum;
    }
}
