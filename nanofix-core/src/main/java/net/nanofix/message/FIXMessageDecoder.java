package net.nanofix.message;

/**
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 07:52
 */
public interface FIXMessageDecoder {
    FIXMessage decodeMessage(byte[] bytes) throws MessageException, MissingFieldException;
}
