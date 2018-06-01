package net.nanofix.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import net.nanofix.message.FIXConstants;
import net.nanofix.message.FIXMessage;
import net.nanofix.message.NanoFIXMessage;
import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.ByteScanner;
import net.nanofix.util.FIXBytes;
import net.nanofix.util.ProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * The first three fields are mandatory and must appear in this order:
 *
 * BeginString (8) - Identifies the beginning of a FIX message. E.g. 8=FIX.4.4.
 *
 * BodyLength (9) - The number of bytes in the message following the BodyLength (9) field up to, and including,
 * the delimiter immediately preceding the CheckSum (10) field.
 *
 * MsgType (35) - Defines message type. E.g. 35=A.
 *
 * CheckSum (10) - Always the last field and the value always contains 3 bytes. E.g. 10=093.
 * Calculated as modulo 256 of the sum of every byte in the message up to but not including the checksum field itself.
 *
 * User: Mark Wardell
 * Date: 10/10/11
 * Time: 14:49
 */
public class FIXFrameDecoder extends ByteToMessageDecoder {
    // TODO avoid creating a new objects anywhere in this class

    private final static Logger LOGGER = LoggerFactory.getLogger(FIXFrameDecoder.class);
    private static final byte[] beginStringByteArray = new byte[FIXConstants.BEGIN_STRING_PREFIX_FIXT.length()];
    private static final byte[] CHECKSUM_PREFIX_BYTES = "10=".getBytes();

    private static final int DEFAULT_MAX_FRAME_LENGTH = 9999;
    private static final int MIN_LENGTH_TAG_OFFSET = 12;
    private static final int MAX_LENGTH_BYTES = 5;
    private static final int LENGTH_OF_TRAILER_BYTES = 7;

    private static final Charset ASCII_CHARSET = StandardCharsets.US_ASCII;
    private static final int NOT_FOUND = -1;
    private final int maxFrameLength;

    public FIXFrameDecoder() {
        this.maxFrameLength = DEFAULT_MAX_FRAME_LENGTH;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {

        // absolute critical fields are 8=, 9= and 35=
        // the first three fields and always in that order
        try {
            int startOfMessageIndex = buffer.readerIndex();

            // Make sure if the bodyLength field was received.
            if (buffer.readableBytes() <= MIN_LENGTH_TAG_OFFSET + 1) {
                // The bodyLength field was not received yet - return null.
                // This method will be invoked again when more packets are
                // received and appended to the buffer.
                return;
            }

            // validate the buffer starts with 8=FIX.
            int startOfValue = ByteScanner.nextValueIndex(buffer, startOfMessageIndex);
            int valueLen = ByteScanner.valueLen(buffer, startOfValue);
            validateBeginString(buffer, startOfValue, valueLen);

            // TODO need to ensure that no extra bytes in between 8 and 9

            // find the second field (i.e. after the first delimiter)
            int bodyLengthTagIndex = ByteScanner.nextTagIndex(buffer, startOfMessageIndex);
            if (bodyLengthTagIndex == NOT_FOUND) {
                throw new ProtocolException("Field delimiter SOH not found in message");
            }

            // make sure '9=' comes next
            if (buffer.getByte(buffer.readerIndex() + bodyLengthTagIndex) != (byte) '9'
                    || buffer.getByte(buffer.readerIndex() + bodyLengthTagIndex + 1) != (byte) '=') {
                throw new ProtocolException("BodyLength(9) should be the second field in the message");
            }

            // make sure there's enough data in the buffer
            final int lengthOfLength = buffer.bytesBefore(buffer.readerIndex() + bodyLengthTagIndex + 2,
                    MAX_LENGTH_BYTES + 1, FIXBytes.SOH);
            if (lengthOfLength == NOT_FOUND) {
                if (buffer.readableBytes() > MAX_LENGTH_BYTES) {
                    // too many characters read, but no BodyLength field found
                    throw new ProtocolException("End of BodyLength field not found within first "
                            + (MAX_LENGTH_BYTES + MIN_LENGTH_TAG_OFFSET) + " bytes");
                }

                // end of BodyLength field not found
                return;
            }

            // extract BodyLength from the buffer next
            // BodyLength is the number of bytes
            int bodyLength;
            try {
                bodyLength = ByteArrayUtil.toInteger(buffer.array(),
                        buffer.readerIndex() + bodyLengthTagIndex + 2, lengthOfLength);
            } catch (NumberFormatException e) {
                bodyLength = NOT_FOUND;
            }
            if (bodyLength <= 0) {
                final String lengthStr = toString(buffer.slice(buffer.readerIndex() + bodyLengthTagIndex + 2, lengthOfLength));
                throw new ProtocolException("Invalid BodyLength(" + lengthStr + ")");
            }
            final int messageLength = (bodyLengthTagIndex + 2 + lengthOfLength + 1) + bodyLength + LENGTH_OF_TRAILER_BYTES;

            if (messageLength > maxFrameLength) {
                throw new ProtocolException("BodyLength may not be greater than " + maxFrameLength + " bytes");
            }

            // There are enough bytes in the buffer. Read it.
            if (buffer.readableBytes() >= messageLength) {

                //ByteBuf msgBuf = buffer.slice(startOfMessageIndex, messageLength);
                FIXMessage msg = NanoFIXMessage.decodeToMsgType(buffer, startOfMessageIndex, messageLength);

                // make sure it ends well
                validateChecksum(buffer);

                // pass this up the pipeline
                out.add(msg);
            }

            // if not make sure BodyLength isn't wrong
            else if (isChecksumPresent(buffer)) {
                throw new ProtocolException("Invalid BodyLength(" + bodyLength + ")");
            }
        } catch (Exception e) {
            LOGGER.warn("Error in decoder: " + e.toString());
            LOGGER.info("Above error caused by message: " + buffer.toString(Charset.defaultCharset()));
            throw e;
        }
    }

    /**
     * validates message to make sure that it starts with begin string (i.e. 8=FIX. or 8=FIXT.)
     *
     * @param buffer the given ChannelBuffer containing a possible FIX message that requires validation
     * @param startOfValue
     * @param valueLen
     * @throws CorruptedFrameException if the buffer does not start with the required FIX identifier
     */
    private void validateBeginString(ByteBuf buffer, int startOfValue, int valueLen) throws CorruptedFrameException {

        // clear the array first
        Arrays.fill(beginStringByteArray, (byte) 0);

        // test for normal FIX first
        buffer.getBytes(buffer.readerIndex(), beginStringByteArray, 0, FIXConstants.BEGIN_STRING_PREFIX.length());
        if (Arrays.equals((FIXConstants.BEGIN_STRING_PREFIX + "\u0000").getBytes(), beginStringByteArray))
            return;

        // now test for FIXT
        buffer.getBytes(buffer.readerIndex(), beginStringByteArray, 0, FIXConstants.BEGIN_STRING_PREFIX_FIXT.length());
        if (Arrays.equals(FIXConstants.BEGIN_STRING_PREFIX_FIXT.getBytes(), beginStringByteArray))
            return;
        throw new CorruptedFrameException("FIX message should begin with \"8=FIX.\" or \"8=FIXT.\"");
    }

    private String toString(ByteBuf buf) {
        return buf.toString(ASCII_CHARSET);
    }

    /**
     * Confirm that end of message has the checksum - i.e. 10=xxx<SOH>
     * and that the calculated checksum is correct
     *
     * @param buffer
     * @throws net.nanofix.message.MessageException
     */
    protected static void validateChecksum(ByteBuf buffer) throws ProtocolException {

        // make sure checksum is there
        int bufferLength = buffer.readableBytes();

        if (!BufferUtil.hasBytes(buffer, CHECKSUM_PREFIX_BYTES, bufferLength - 7)) {
            throw new ProtocolException("Missing checksum field [10=] at end of message");
        }

        // confirm that end of message has the checksum - i.e. 10=xxx<SOH>
        int computedChecksum = ByteBufChecksumCalculator.calculate(buffer);
        int receivedChecksum = 0;
        try {
            receivedChecksum = Integer.parseInt(buffer.toString(bufferLength - 4, 3, ASCII_CHARSET));
        } catch (NumberFormatException e) {
            throw new ProtocolException("Checksum failure (" + receivedChecksum + ")");
        }

        if (receivedChecksum != computedChecksum)
            throw new ProtocolException("Checksum failure (" + receivedChecksum + ")");
    }

    private static boolean isChecksumPresent(ByteBuf buffer)  {
        for (int i = buffer.readerIndex(); i < buffer.capacity(); i++) {
            if (buffer.getByte(i) == FIXBytes.SOH
                    && buffer.getByte(i + 1) == (byte) '1'
                    && buffer.getByte(i + 2) == (byte) '0'
                    && buffer.getByte(i + 3) == (byte) '=')
                return true;
        }
        return false;
    }

    /**
     * This method is a crude attempt to extract 34=NNN from an invalid FIX messages that cannot be parsed
     * using the normal parsing technique.
     *
     * @param bytes the invalid FIX message
     * @return the SeqNum extracted from the string
     */
    private long extractSeqNum(byte[] bytes) {
        long seqNum = -1L;
        String msg = new String(bytes);
        int index = msg.indexOf(FIXBytes.SOH + "34=");
        if (index == -1) {
            // not found
            return seqNum;
        }
        int endIndex = msg.indexOf(FIXBytes.SOH, index + 3);
        if (endIndex == -1) {
            // not found
            return seqNum;
        }
        try {
            seqNum = Long.parseLong(msg.substring(index + 3, endIndex));
        } catch (NumberFormatException e) {
            // ignore, best efforts here
        }
        return seqNum;
    }


}
