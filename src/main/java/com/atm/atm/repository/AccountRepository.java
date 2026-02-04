package com.atm.atm.repository;

import com.atm.atm.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Account entity.
 * Provides CRUD operations and custom queries for Account data access.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find an account by account number.
     * @param accountNumber the account number to search for
     * @return Optional containing the Account if found
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Check if an account exists by account number.
     * @param accountNumber the account number to check
     * @return true if account exists, false otherwise
     */
    boolean existsByAccountNumber(String accountNumber);
}
