package com.atm.atm.service;

import com.atm.atm.dto.TransactionDTO;
import com.atm.atm.entity.Account;
import com.atm.atm.entity.Transaction;
import com.atm.atm.exception.InsufficientBalanceException;
import com.atm.atm.exception.InvalidAmountException;
import com.atm.atm.repository.AccountRepository;
import com.atm.atm.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for ATM operations.
 * Handles withdrawals, deposits, and transaction history.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ATMService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Withdraw cash from the account.
     * Validates amount and checks for sufficient balance.
     *
     * @param account the account to withdraw from
     * @param amount the amount to withdraw
     * @return TransactionDTO representing the completed transaction
     * @throws InvalidAmountException if amount is invalid
     * @throws InsufficientBalanceException if balance is insufficient
     */
    public TransactionDTO withdraw(Account account, Double amount) {
        // Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0");
        }

        if (amount % 100 != 0) {
            throw new InvalidAmountException("Withdrawal amount must be in multiples of 100");
        }

        // Check balance
        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException(
                    String.format("Insufficient balance. Current balance: %.2f, Requested: %.2f",
                            account.getBalance(), amount));
        }

        // Update account balance
        account.setBalance(account.getBalance() - amount);
        account.setLastTransactionDate(LocalDateTime.now());
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("WITHDRAWAL");
        transaction.setAmount(amount);
        transaction.setBalanceAfterTransaction(account.getBalance());
        transaction.setDescription("Cash withdrawal");
        transaction.setStatus("SUCCESS");
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        return mapToDTO(transaction);
    }

    /**
     * Deposit cash to the account.
     * Validates amount before depositing.
     *
     * @param account the account to deposit to
     * @param amount the amount to deposit
     * @return TransactionDTO representing the completed transaction
     * @throws InvalidAmountException if amount is invalid
     */
    public TransactionDTO deposit(Account account, Double amount) {
        // Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0");
        }

        if (amount % 100 != 0) {
            throw new InvalidAmountException("Deposit amount must be in multiples of 100");
        }

        // Update account balance
        account.setBalance(account.getBalance() + amount);
        account.setLastTransactionDate(LocalDateTime.now());
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setBalanceAfterTransaction(account.getBalance());
        transaction.setDescription("Cash deposit");
        transaction.setStatus("SUCCESS");
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        return mapToDTO(transaction);
    }

    /**
     * Get transaction history for an account.
     *
     * @param account the account to get history for
     * @return List of TransactionDTOs
     */
    public List<TransactionDTO> getTransactionHistory(Account account) {
        List<Transaction> transactions = transactionRepository
                .findByAccountOrderByTransactionDateDesc(account);
        return transactions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get recent transactions (last 10).
     *
     * @param account the account to get transactions for
     * @return List of recent TransactionDTOs
     */
    public List<TransactionDTO> getRecentTransactions(Account account) {
        return getTransactionHistory(account).stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Map Transaction entity to TransactionDTO.
     *
     * @param transaction the transaction entity
     * @return TransactionDTO
     */
    private TransactionDTO mapToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getBalanceAfterTransaction(),
                transaction.getDescription(),
                transaction.getStatus(),
                transaction.getTransactionDate()
        );
    }
}
