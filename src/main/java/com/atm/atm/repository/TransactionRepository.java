package com.atm.atm.repository;

import com.atm.atm.entity.Account;
import com.atm.atm.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Transaction entity.
 * Provides CRUD operations and custom queries for Transaction data access.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find all transactions for a specific account, ordered by date descending.
     * @param account the account to find transactions for
     * @return List of transactions for the account
     */
    List<Transaction> findByAccountOrderByTransactionDateDesc(Account account);

    /**
     * Find all transactions for a specific account with a given type.
     * @param account the account to find transactions for
     * @param transactionType the type of transaction (WITHDRAWAL, DEPOSIT, etc.)
     * @return List of matching transactions
     */
    List<Transaction> findByAccountAndTransactionTypeOrderByTransactionDateDesc(Account account, String transactionType);
}
