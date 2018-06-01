package net.nanofix.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * User: Mark
 * Date: 02/04/12
 * Time: 18:32
 */
public interface SessionConfig {

    String getVersion();

    void setVersion(String version);

    String getSenderCompID();

    void setSenderCompID(String senderCompID);

    String getTargetCompID();

    void setTargetCompID(String targetCompID);

    String getSenderSubID();

    void setSenderSubID(String senderSubID);

    String getSenderLocationID();

    void setSenderLocationID(String senderLocationID);

    String getTargetSubID();

    void setTargetSubID(String targetSubID);

    String getTargetLocationID();

    void setTargetLocationID(String targetLocationID);

    int getHeartbeatInterval();

    void setHeartbeatInterval(int heartbeat);

    List<ConnectionConfig> getConnectors();

    void setResetSeqNum(boolean resetSeqNum);

    boolean isResetSeqNum();

    void setUseMillisInTimeStamp(boolean useMillisInTimeStamp);

    boolean isUseMillisInTimeStamp();

}
