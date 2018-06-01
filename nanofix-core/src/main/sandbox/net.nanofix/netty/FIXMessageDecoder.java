package net.nanofix.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.nanofix.message.*;
import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.FIXBytes;
import net.nanofix.util.FIXMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Header
 * 8 BeginString
 * 9 BodyLength
 * 35 MsgType
 * 49 SenderCompID
 * 56 TargetCompID
 * 34 MsgSeqNum
 * 52 SendingTime
 *
 * User: Mark
 * Date: 11/10/11
 * Time: 07:52
 */
public class FIXMessageDecoder extends ByteToMessageDecoder {

    protected Logger LOG = LoggerFactory.getLogger(FIXMessageDecoder.class);

    private final Charset charset;
    private final FIXMessageFactory fixMessageFactory;

    public FIXMessageDecoder() {
        this(new DefaultFIXMessageFactory(settings));
    }

    public FIXMessageDecoder(FIXMessageFactory messageFactory) {
        this.charset = Charset.defaultCharset();
        this.fixMessageFactory = messageFactory;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        logIncomingMessage(in);
        FIXMessage target = fixMessageFactory.createMessage();
        //target.setRawBytes(bytes);
        decodeMessageHeader(in, target);
        LOG.debug("decoded message: {}", target);
        out.add(target);
    }

    private void logIncomingMessage(ByteBuf byteBuf) {
        LOG.debug("< {}", new String(byteBuf.array()));
    }

    private void decodeMessageHeader(ByteBuf byteBuf, FIXMessage msg) throws MissingFieldException {
        decodeBytes(byteBuf, msg, true);
        // TODO get the CompIDs
    }

    /**
     * Read the fields from the byte array adding each field to the collection
     * @param byteBuf the byte array containing the raw tag/value pairs
     * @param target the target FIXMessage to populate
     * @param headerOnly whether to process the header fields only
     * @return the target FIXMessage
     */
    public FIXMessage decodeBytes(ByteBuf byteBuf, FIXMessage target, boolean headerOnly) {
        int offset = 0;
        byte[] bytes = byteBuf.array();
        while (offset < bytes.length) {
            int equalPos = nextEqualPos(bytes, offset);

            // extract the tag number
            int tag = ByteArrayUtil.toInteger(bytes, offset, equalPos - offset);

            // return now if this isn't a header field and that's all we want
            if (headerOnly && !FIXMessageUtil.isHeaderField(tag)) {
                break;
            }
            int delimiterPos = nextDelimiterPos(bytes, equalPos + 1);

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
    private int nextEqualPos(byte[] bytes, int offset) {
        for (int i=offset; i < bytes.length; i++) {
            if (bytes[i] == FIXBytes.SOH)
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
    private int nextDelimiterPos(byte[] bytes, int offset) {
        for (int i=offset; i < bytes.length; i++) {
            if (bytes[i] == FIXBytes.SOH)
                return i;
        }
        throw new IllegalArgumentException("Tag/value delimiter SOH expected before end of array");
    }

}
