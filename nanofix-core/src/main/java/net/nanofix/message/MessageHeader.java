package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.time.UtcDateTimeEncoder;
import net.nanofix.util.ByteArrayUtil;
import net.nanofix.util.ByteString;
import net.nanofix.util.DefaultTimeGenerator;
import net.nanofix.util.FIXBytes;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Header
 * 8 BeginString
 * 9 BodyLength
 * 35 MsgType
 * 49 SenderCompID
 * 56 TargetCompID
 * 34 MsgSeqNum
 * 52 SendingTime
 */
public class MessageHeader extends FixMessageAssembler {

    private static final int MAX_FIXED_BUFFER_CAPACITY = 19;

    private final UtcDateTimeEncoder timeEncoder = new UtcDateTimeEncoder();
    private final DefaultTimeGenerator timeGenerator = new DefaultTimeGenerator();
    private final ByteBuffer beginBuffer = ByteBuffer.allocate(MAX_FIXED_BUFFER_CAPACITY);
    private final ByteBuffer buffer;

    private boolean dirty = false;
    private ByteString beginString;
    private int bodyLength;
    private MsgType msgType;
    private ByteString senderCompId;
    private ByteString targetCompId;
    private int msgSeqNum;
    private long sendingTime;

    public MessageHeader(ByteBuffer buffer) {
        super(buffer);
        this.buffer = buffer;
    }

//    public ByteString beginString() {
//        return beginString;
//    }

    public void beginString(ByteString beginString) {
        this.dirty = true;
        this.beginString = beginString;
    }

    public MsgType msgType() {
        return msgType;
    }

    public void msgType(MsgType msgType) {
        this.dirty = true;
        this.msgType = msgType;
    }

    public ByteString senderCompId() {
        return senderCompId;
    }

    public void senderCompId(ByteString senderCompId) {
        this.dirty = true;
        this.senderCompId = senderCompId;
    }

    public ByteString targetCompId() {
        return targetCompId;
    }


    public void targetCompId(ByteString targetCompId) {
        this.dirty = true;
        this.targetCompId = targetCompId;
    }

    public int msgSeqNum() {
        return msgSeqNum;
    }

    public void msgSeqNum(int msgSeqNum) {
        this.dirty = true;
        this.msgSeqNum = msgSeqNum;
    }

    public long sendingTime() {
        return sendingTime;
    }

    public void sendingTime(long sendingTime) {
        this.dirty = true;
        this.sendingTime = sendingTime;
    }

    public void bodyLength(int bodyLength) {
        this.dirty = true;
        this.bodyLength = bodyLength;
    }

    ByteBuffer beginBuffer() {
        return beginBuffer;
    }

    ByteBuffer headerBuffer() {
        return buffer;
    }

    void populateBuffer() {

        // TODO inject a time formatter / generator that understands millis, micros etc
        // TODO transfer long directly into buffer
        if (dirty) {
            addStringField(Tags.MsgType, msgType);
            addStringField(Tags.SenderCompID, senderCompId);
            addStringField(Tags.TargetCompID, targetCompId);
            addIntField(Tags.MsgSeqNum, msgSeqNum);
            addTimestamp(Tags.SendingTime, sendingTime);
        }
        int fixBodyLength = bodyLength + ByteBufferUtil.readableBytes(buffer);

        // now that we have the length we can populated the begin buffer
        addBytesWithDelimiters(
                beginBuffer,
                ByteArrayUtil.asByteArray(Tags.BeginString),
                beginString.bytes()
        );
        addBytesWithDelimiters(
                beginBuffer,
                ByteArrayUtil.asByteArray(Tags.BodyLength),
                ByteArrayUtil.asByteArray(fixBodyLength)
        );
    }

    private void addTimestamp(int tag, long epochMillis) {
        buffer.put(ByteArrayUtil.asByteArray(tag));
        buffer.put(FIXBytes.EQUALS);
        timeEncoder.encode(epochMillis, buffer);
        buffer.put(FIXBytes.SOH);
    }

}
