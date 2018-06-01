package net.nanofix.session;

/**
 * User: Mark
 * Date: 01/05/12
 * Time: 20:13
 */
public class FatalProtocolException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * This exception will cause the session to be disconnected.
     *
     * @param   message   the detail message. The detail message is saved for
     *          later retrieval by the {@link #getMessage()} method.
     */
    public FatalProtocolException(String message) {
        super(message);
    }

    public FatalProtocolException(Throwable cause) {
        super(cause);
    }
}
