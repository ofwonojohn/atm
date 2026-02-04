package com.atm.atm.controller;

import com.atm.atm.dto.*;
import com.atm.atm.entity.Account;
import com.atm.atm.exception.InsufficientBalanceException;
import com.atm.atm.exception.InvalidAmountException;
import com.atm.atm.service.AccountService;
import com.atm.atm.service.ATMService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for ATM operations.
 * Handles dashboard, withdrawals, deposits, and transaction history.
 */
@Controller
@RequestMapping("/atm")
@RequiredArgsConstructor
public class ATMController {

    private final AccountService accountService;
    private final ATMService atmService;

    /**
     * Helper method to get account from session and check if logged in.
     *
     * @param session the HTTP session
     * @return Account entity if logged in
     */
    private Account getSessionAccount(HttpSession session) {
        String accountNumber = (String) session.getAttribute("accountNumber");
        if (accountNumber == null) {
            return null;
        }
        return accountService.getAccountEntityByNumber(accountNumber);
    }

    /**
     * Display ATM dashboard with current balance.
     *
     * @param session the HTTP session
     * @param model the model object
     * @return dashboard view
     */
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        String accountNumber = (String) session.getAttribute("accountNumber");
        if (accountNumber == null) {
            return "redirect:/auth/login";
        }

        Account account = getSessionAccount(session);
        AccountDTO accountDTO = accountService.getAccountByNumber(accountNumber);
        model.addAttribute("account", accountDTO);

        return "dashboard";
    }

    /**
     * Display withdraw page.
     *
     * @param session the HTTP session
     * @param model the model object
     * @return withdraw view
     */
    @GetMapping("/withdraw")
    public String showWithdrawPage(HttpSession session, Model model) {
        if (session.getAttribute("accountNumber") == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("withdrawRequest", new WithdrawRequest());
        return "withdraw";
    }

    /**
     * Handle withdrawal transaction.
     *
     * @param withdrawRequest the withdrawal details
     * @param bindingResult validation results
     * @param session the HTTP session
     * @param model the model object
     * @return dashboard on success, withdraw page on failure
     */
    @PostMapping("/withdraw")
    public String withdraw(@Valid WithdrawRequest withdrawRequest,
                           BindingResult bindingResult,
                           HttpSession session,
                           Model model) {

        if (session.getAttribute("accountNumber") == null) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "withdraw";
        }

        try {
            Account account = getSessionAccount(session);
            TransactionDTO transaction = atmService.withdraw(account, withdrawRequest.getAmount());

            // Update session account
            AccountDTO updatedAccount = accountService.getAccountByNumber(account.getAccountNumber());
            session.setAttribute("account", updatedAccount);

            model.addAttribute("successMessage",
                    String.format("Successfully withdrawn: Rs. %.2f", transaction.getAmount()));
            model.addAttribute("account", updatedAccount);

            return "withdraw";

        } catch (InvalidAmountException | InsufficientBalanceException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("withdrawRequest", new WithdrawRequest());
            return "withdraw";
        }
    }

    /**
     * Display deposit page.
     *
     * @param session the HTTP session
     * @param model the model object
     * @return deposit view
     */
    @GetMapping("/deposit")
    public String showDepositPage(HttpSession session, Model model) {
        if (session.getAttribute("accountNumber") == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("depositRequest", new DepositRequest());
        return "deposit";
    }

    /**
     * Handle deposit transaction.
     *
     * @param depositRequest the deposit details
     * @param bindingResult validation results
     * @param session the HTTP session
     * @param model the model object
     * @return dashboard on success, deposit page on failure
     */
    @PostMapping("/deposit")
    public String deposit(@Valid DepositRequest depositRequest,
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model) {

        if (session.getAttribute("accountNumber") == null) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "deposit";
        }

        try {
            Account account = getSessionAccount(session);
            TransactionDTO transaction = atmService.deposit(account, depositRequest.getAmount());

            // Update session account
            AccountDTO updatedAccount = accountService.getAccountByNumber(account.getAccountNumber());
            session.setAttribute("account", updatedAccount);

            model.addAttribute("successMessage",
                    String.format("Successfully deposited: Rs. %.2f", transaction.getAmount()));
            model.addAttribute("account", updatedAccount);

            return "deposit";

        } catch (InvalidAmountException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("depositRequest", new DepositRequest());
            return "deposit";
        }
    }

    /**
     * Display transaction history page.
     *
     * @param session the HTTP session
     * @param model the model object
     * @return transaction history view
     */
    @GetMapping("/history")
    public String showTransactionHistory(HttpSession session, Model model) {
        if (session.getAttribute("accountNumber") == null) {
            return "redirect:/auth/login";
        }

        Account account = getSessionAccount(session);
        List<TransactionDTO> transactions = atmService.getTransactionHistory(account);
        AccountDTO accountDTO = accountService.getAccountByNumber(account.getAccountNumber());

        model.addAttribute("account", accountDTO);
        model.addAttribute("transactions", transactions);

        return "transaction-history";
    }
}
