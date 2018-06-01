package net.nanofix.session;

import org.jboss.netty.channel.ChannelHandler;

/**
 * User: Mark
 * Date: 26/04/12
 * Time: 22:04
 */
public interface ConnectionListener {

    void connectionStatus(ChannelHandler channelHandler, boolean state);

}
