package net.nanofix.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mark
 * Date: 04/05/12
 * Time: 06:13
 */
public class FIXMessageException extends Throwable {

    public FIXMessageException(String message) {
        super(message);
    }
}
