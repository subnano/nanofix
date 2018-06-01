package net.nanofix.message;

import java.nio.ByteBuffer;

/**
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 07:41
 */
public interface FIXMessage extends MessageAssembler {

    MessageHeader header();

    MsgType msgType();

    ByteBuffer[] buffers();
}
