package net.nanofix.message;

/**
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 07:39
 */
public class MissingFieldException extends Exception {

    private final int tag;

    public MissingFieldException(int tag, String message) {
        super(message);
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }
}
