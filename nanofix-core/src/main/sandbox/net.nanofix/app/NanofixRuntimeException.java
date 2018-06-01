package net.nanofix.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Mark
 * Date: 08/05/12
 * Time: 06:24
 */
public class NanofixRuntimeException extends RuntimeException {
    public NanofixRuntimeException(String message) {
        super(message);
    }
}
