package com.atm.atm.service;

import com.atm.atm.dto.AccountDTO;
import com.atm.atm.entity.Account;
import com.atm.atm.exception.AccountNotFoundException;
import com.atm.atm.exception.InvalidPinException;
import com.atm.atm.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for account-related operations.
 * Handles account authentication and retrieval.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private static final int MAX_FAILED_ATTEMPTS = 3;

    /**
     * Authenticate an account with account number and PIN.
     * Locks account after 3 failed attempts.
     *
     * @param accountNumber the account number
     * @param pin the PIN
     * @return AccountDTO of authenticated account
     * @throws AccountNotFoundException if account doesn't exist
     * @throws InvalidPinException if PIN is incorrect
     */
    public AccountDTO authenticate(String accountNumber, String pin) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));

        // Check if account is locked
        if ("LOCKED".equals(account.getStatus())) {
            throw new InvalidPinException("Account is locked due to multiple failed login attempts");
        }

        // Validate PIN
        if (!account.getPin().equals(pin)) {
            account.setFailedLoginAttempts(account.getFailedLoginAttempts() + 1);

            // Lock account after MAX_FAILED_ATTEMPTS
            if (account.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                account.setStatus("LOCKED");
                accountRepository.save(account);
                throw new InvalidPinException("Account is now locked due to multiple failed login attempts");
            }

            accountRepository.save(account);
            throw new InvalidPinException("Invalid PIN. Attempts remaining: " + (MAX_FAILED_ATTEMPTS - account.getFailedLoginAttempts()));
        }

        // Reset failed attempts on successful login
        account.setFailedLoginAttempts(0);
        accountRepository.save(account);

        return mapToDTO(account);
    }

    /**
     * Get account details by account number.
     *
     * @param accountNumber the account number
     * @return AccountDTO
     * @throws AccountNotFoundException if account doesn't exist
     */
    public AccountDTO getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        return mapToDTO(account);
    }

    /**
     * Get account entity by account number.
     *
     * @param accountNumber the account number
     * @return Account entity
     * @throws AccountNotFoundException if account doesn't exist
     */
    public Account getAccountEntityByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    /**
     * Map Account entity to AccountDTO.
     *
     * @param account the account entity
     * @return AccountDTO
     */
    private AccountDTO mapToDTO(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountHolderName(),
                account.getBalance(),
                account.getStatus(),
                account.getEmail(),
                account.getPhoneNumber(),
                account.getCreatedDate(),
                account.getLastTransactionDate()
        );
    }
}
