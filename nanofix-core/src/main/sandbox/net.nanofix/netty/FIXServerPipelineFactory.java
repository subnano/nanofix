package net.nanofix.netty;

import net.nanofix.app.Application;
import net.nanofix.session.ConnectionListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * User: Mark
 * Date: 24/04/12
 * Time: 06:26
 */
public class FIXServerPipelineFactory implements ChannelPipelineFactory {

    private final Application application;

    public FIXServerPipelineFactory(Application application) {
        this.application = application;
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

        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("frame-decoder", new FIXFrameDecoder());
        pipeline.addLast("message-decoder", new StandardFIXMessageDecoder());
        pipeline.addLast("channel-handler", new FIXServerChannelHandler(application, null));
        return pipeline;
    }
}
