package com.atm.atm.exception;

/**
 * Exception thrown when account balance is insufficient for a withdrawal.
 */
public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
