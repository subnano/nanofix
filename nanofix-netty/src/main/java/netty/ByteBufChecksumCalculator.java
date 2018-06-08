package net.nanofix.netty;

import io.netty.buffer.ByteBuf;
import net.nanofix.message.FIXConstants;

final class ByteBufChecksumCalculator {

    private ByteBufChecksumCalculator() {
        // can't touch this
    }

    static int calculate(ByteBuf buf) {
        int checksum = 0;
        int bufferLength = buf.readableBytes();
        for (int i = 0; i < bufferLength - FIXConstants.CHECKSUM_SIZE; i++) {
            checksum += buf.getByte(i);
        }
        return checksum % 256;
    }
}
