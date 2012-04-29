package net.nanofix.netty;

import net.nanofix.message.DefaultFIXMessageFactory;
import net.nanofix.session.Session;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:26
 */
public class FIXClientPipelineFactory implements ChannelPipelineFactory {

    private final DefaultFIXMessageFactory fixMessageFactory;
    private final Session session;

    public FIXClientPipelineFactory(Session session) {
        this.session = session;
        fixMessageFactory = new DefaultFIXMessageFactory();
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
//        final ChannelHandler[] handlers = {
//                new IdleStateHandler(HASHEDWHEELTIMER, 1, 1, 1),
//                new FIXFrameDecoder(),
//                STRINGDECODER,//Incoming
//                STRINGENCODER,//Outgoing
//                isDebugOn ? LOGHANDLER : NOOPHANDLER,
//                new FIXSessionProcessor(true,headerFields, trailerFields, logonManager,
//                        sessions, queueFactory, outgoingCallback)
//        };

        ChannelPipeline pipeline = Channels.pipeline(
                new FIXFrameDecoder(),
                new StandardFIXMessageDecoder(fixMessageFactory),
                new FIXMessageEncoder(session.getVersion()),
                new NanoFixChannelHandler(null, session)
        );
        return pipeline;
    }
}
