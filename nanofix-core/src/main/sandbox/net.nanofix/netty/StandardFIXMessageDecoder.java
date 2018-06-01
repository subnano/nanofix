package net.nanofix.netty;

import net.nanofix.field.RawField;
import net.nanofix.message.*;
import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.FIXMessageUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * User: Mark
 * Date: 11/10/11
 * Time: 07:52
 */
public class StandardFIXMessageDecoder extends FrameDecoder implements FIXMessageDecoder {

    protected Logger LOG = LoggerFactory.getLogger(StandardFIXMessageDecoder.class);

    private byte[] checksumBytes = new byte[FIXConstants.CHECKSUM_SIZE];
    private final Charset charset;
    private final FIXMessageFactory fixMessageFactory;

    public StandardFIXMessageDecoder() {
        this(new DefaultFIXMessageFactory());
    }

    public StandardFIXMessageDecoder(FIXMessageFactory messageFactory) {
        this.charset = Charset.defaultCharset();
        this.fixMessageFactory = messageFactory;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        return decode(buffer.readBytes(buffer.readableBytes()).array());
    }

    @Override
    public FIXMessage decode(byte[] bytes) throws MessageException, MissingFieldException {
        logIncomingMessage(bytes);
        FIXMessage target = fixMessageFactory.createMessage();
        target.setRawBytes(bytes);
        decodeMessageHeader(bytes, target);
        LOG.debug("decoded message: {}", target);
        return target;
    }

    private void logIncomingMessage(byte[] bytes) {
        LOG.debug("< {}", new String(bytes));
    }

    private void decodeMessageHeader(byte[] bytes, FIXMessage msg) throws MissingFieldException {
        decodeBytes(bytes, msg, true);

        // get the CompIDs
    }

    private void setSessionID() {
    }

    /**
     * Read the fields from the byte array adding each field to the collection
     * @param bytes the byte array containing the raw tag/value pairs
     * @param target the target FIXMessage to populate
     * @param headerOnly whether to process the header fields only
     * @return the target FIXMessage
     */
    public FIXMessage decodeBytes(byte[] bytes, FIXMessage target, boolean headerOnly) {
        target.clear();
        int offset = 0;
        while (offset < bytes.length) {
            int equalPos = getEqualPos(bytes, offset);

            // extract the tag number
            int tag = ByteArrayUtil.toInteger(bytes, offset, equalPos - offset);

            // return now if this isn't a header field and that's all we want
            if (headerOnly && !FIXMessageUtil.isHeaderField(tag)) {
                break;
            }
            int delimiterPos = getDelimiterPos(bytes, equalPos + 1);

            // extract the tag value
            byte[] tagValue = target.setFieldValue(tag, Arrays.copyOfRange(bytes, equalPos + 1, delimiterPos));

            // set these additional values
            if (tag == Tags.MsgType) {
                target.setMsgType(new String(tagValue));
            }
            else if (tag == Tags.MsgSeqNum) {
                target.setMsgSeqNum(ByteArrayUtil.toLong(tagValue));
            }

            offset = delimiterPos + 1;
        }
        return target;
    }

    /**
     * Find the position of the next '=' from the given offset
     * @param bytes The source bytes to search
     * @param offset The offset to start searching in the given byte array
     * @return The position of the '=' after the given offset
     */
    private int getEqualPos(byte[] bytes, int offset) {
        for (int i=offset; i < bytes.length; i++) {
            if (bytes[i] == FIXConstants.SOH)
                throw new IllegalArgumentException("Tag delimiter '=' expected before SOH");
            if (bytes[i] == '=')
                return i;
        }
        throw new IllegalArgumentException("Tag delimiter '=' expected before end of array");
    }

    /**
     * Find the position of the next SOH from the given offset
     * @param bytes The source bytes to search
     * @param offset The offset to start searching in the given byte array
     * @return The position of the SOH after the given offset
     */
    private int getDelimiterPos(byte[] bytes, int offset) {
        for (int i=offset; i < bytes.length; i++) {
            if (bytes[i] == FIXConstants.SOH)
                return i;
        }
        throw new IllegalArgumentException("Tag/value delimiter SOH expected before end of array");
    }

}
