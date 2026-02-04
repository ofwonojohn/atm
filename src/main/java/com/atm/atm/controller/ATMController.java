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
     * Helper method to get AccountDTO from session.
     */
    private AccountDTO getSessionAccountDTO(HttpSession session) {
        return (AccountDTO) session.getAttribute("account");
    }

    /**
     * Helper method to get Account entity from session.
     */
    private Account getSessionAccount(HttpSession session) {
        AccountDTO dto = getSessionAccountDTO(session);
        if (dto == null) return null;
        return accountService.getAccountEntityByNumber(dto.getAccountNumber());
    }

    /**
     * Display ATM dashboard with current balance.
     *
     * @param session the HTTP session
     * @param model the model object
     * @return dashboard view
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        AccountDTO account = getSessionAccountDTO(session);
        if (account == null) return "redirect:/auth/login";
        model.addAttribute("account", account);
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
    public String showWithdrawForm(HttpSession session, Model model) {
        AccountDTO account = getSessionAccountDTO(session);
        if (account == null) return "redirect:/auth/login";
        model.addAttribute("account", account);
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
    public String withdraw(@Valid @ModelAttribute("withdrawRequest") WithdrawRequest withdrawRequest,
                          BindingResult bindingResult,
                          HttpSession session,
                          Model model) {
        AccountDTO accountDTO = getSessionAccountDTO(session);
        if (accountDTO == null) return "redirect:/auth/login";
        model.addAttribute("account", accountDTO);
        if (bindingResult.hasErrors()) return "withdraw";
        try {
            Account account = accountService.getAccountEntityByNumber(accountDTO.getAccountNumber());
            atmService.withdraw(account, withdrawRequest.getAmount());
            // Update session account
            session.setAttribute("account", accountService.getAccountByNumber(account.getAccountNumber()));
            model.addAttribute("successMessage", "Withdrawal successful!");
            model.addAttribute("withdrawRequest", new WithdrawRequest());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "withdraw";
    }

    /**
     * Display deposit page.
     *
     * @param session the HTTP session
     * @param model the model object
     * @return deposit view
     */
    @GetMapping("/deposit")
    public String showDepositForm(HttpSession session, Model model) {
        AccountDTO account = getSessionAccountDTO(session);
        if (account == null) return "redirect:/auth/login";
        model.addAttribute("account", account);
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
    public String deposit(@Valid @ModelAttribute("depositRequest") DepositRequest depositRequest,
                        BindingResult bindingResult,
                        HttpSession session,
                        Model model) {
        AccountDTO accountDTO = getSessionAccountDTO(session);
        if (accountDTO == null) return "redirect:/auth/login";
        model.addAttribute("account", accountDTO);
        if (bindingResult.hasErrors()) return "deposit";
        try {
            Account account = accountService.getAccountEntityByNumber(accountDTO.getAccountNumber());
            atmService.deposit(account, depositRequest.getAmount());
            // Update session account
            session.setAttribute("account", accountService.getAccountByNumber(account.getAccountNumber()));
            model.addAttribute("successMessage", "Deposit successful!");
            model.addAttribute("depositRequest", new DepositRequest());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "deposit";
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
        AccountDTO accountDTO = getSessionAccountDTO(session);
        if (accountDTO == null) return "redirect:/auth/login";
        Account account = accountService.getAccountEntityByNumber(accountDTO.getAccountNumber());
        List<TransactionDTO> transactions = atmService.getTransactionHistory(account);
        model.addAttribute("account", accountDTO);
        model.addAttribute("transactions", transactions);
        return "transaction-history";
    }

    /**
     * Logout and invalidate session.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login?logout=true";
    }
}
