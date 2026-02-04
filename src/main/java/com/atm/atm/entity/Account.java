package com.atm.atm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Account entity representing a bank account in the ATM system.
 * Contains account details, balance, and PIN for authentication.
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Account number - unique identifier for customers
    @Column(nullable = false, unique = true)
    private String accountNumber;

    // PIN for authentication (in production, this should be hashed)
    @Column(nullable = false)
    private String pin;

    // Account holder's name
    @Column(nullable = false)
    private String accountHolderName;

    // Current balance in the account
    @Column(nullable = false)
    private Double balance;

    // Account status (ACTIVE, INACTIVE, LOCKED, etc.)
    @Column(nullable = false)
    private String status;

    // Email for notifications
    @Column(unique = true)
    private String email;

    // Phone number
    private String phoneNumber;

    // Account creation timestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    // Last transaction timestamp
    private LocalDateTime lastTransactionDate;

    // Failed login attempts counter
    @Column(nullable = false)
    private Integer failedLoginAttempts;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        failedLoginAttempts = 0;
        if (status == null) {
            status = "ACTIVE";
        }
    }
}
