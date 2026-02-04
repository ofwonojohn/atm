package com.atm.atm.exception;

/**
 * Exception thrown when an invalid PIN is provided during authentication.
 */
public class InvalidPinException extends RuntimeException {
    public InvalidPinException(String message) {
        super(message);
    }

    public InvalidPinException(String message, Throwable cause) {
        super(message, cause);
    }
}
