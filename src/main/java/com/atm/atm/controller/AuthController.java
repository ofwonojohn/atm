package com.atm.atm.controller;

import com.atm.atm.dto.LoginRequest;
import com.atm.atm.dto.AccountDTO;
import com.atm.atm.exception.AccountNotFoundException;
import com.atm.atm.exception.InvalidPinException;
import com.atm.atm.service.AccountService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for authentication-related operations.
 * Handles login and logout functionality.
 */
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;

    /**
     * Display login page.
     *
     * @param model the model object
     * @return login view
     */
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    /**
     * Handle login form submission.
     * Authenticates user and stores account in session on success.
     *
     * @param loginRequest the login credentials
     * @param bindingResult validation results
     * @param session the HTTP session
     * @param model the model object
     * @return redirect to dashboard on success, login page on failure
     */
    @PostMapping("/login")
    public String login(@Valid LoginRequest loginRequest,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {

        // Check for validation errors
        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            // Authenticate account
            AccountDTO account = accountService.authenticate(
                    loginRequest.getAccountNumber(),
                    loginRequest.getPin()
            );

            // Store account in session
            session.setAttribute("account", account);
            session.setAttribute("accountNumber", account.getAccountNumber());

            return "redirect:/atm/dashboard";

        } catch (AccountNotFoundException | InvalidPinException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("loginRequest", new LoginRequest());
            return "login";
        }
    }

    /**
     * Handle logout.
     * Invalidates the session and redirects to login page.
     *
     * @param session the HTTP session
     * @return redirect to login page
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login?logout=true";
    }
}
