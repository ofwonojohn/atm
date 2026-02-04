package com.atm.atm.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the ATM application.
 * Handles all custom exceptions and provides error views.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle AccountNotFoundException.
     *
     * @param ex the exception
     * @param model the model object
     * @return error view
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public String handleAccountNotFound(AccountNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Account Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    /**
     * Handle InvalidPinException.
     *
     * @param ex the exception
     * @param model the model object
     * @return error view
     */
    @ExceptionHandler(InvalidPinException.class)
    public String handleInvalidPin(InvalidPinException ex, Model model) {
        model.addAttribute("errorTitle", "Authentication Failed");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    /**
     * Handle InsufficientBalanceException.
     *
     * @param ex the exception
     * @param model the model object
     * @return error view
     */
    @ExceptionHandler(InsufficientBalanceException.class)
    public String handleInsufficientBalance(InsufficientBalanceException ex, Model model) {
        model.addAttribute("errorTitle", "Insufficient Balance");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    /**
     * Handle InvalidAmountException.
     *
     * @param ex the exception
     * @param model the model object
     * @return error view
     */
    @ExceptionHandler(InvalidAmountException.class)
    public String handleInvalidAmount(InvalidAmountException ex, Model model) {
        model.addAttribute("errorTitle", "Invalid Amount");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    /**
     * Handle generic exceptions.
     *
     * @param ex the exception
     * @param model the model object
     * @return error view
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("errorTitle", "An Error Occurred");
        model.addAttribute("errorMessage", "Something went wrong. Please try again later.");
        return "error";
    }
}
