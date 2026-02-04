package com.atm.atm.exception;

/**
 * Exception thrown when an invalid amount is provided for a transaction.
 */
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
