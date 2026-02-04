package com.atm.atm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Transaction entity representing a transaction history record.
 * Tracks all withdrawals, deposits, and other account activities.
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key to Account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Transaction type: WITHDRAWAL, DEPOSIT, TRANSFER, etc.
    @Column(nullable = false)
    private String transactionType;

    // Transaction amount
    @Column(nullable = false)
    private Double amount;

    // Balance after transaction
    @Column(nullable = false)
    private Double balanceAfterTransaction;

    // Transaction description or remarks
    private String description;

    // Transaction status: SUCCESS, FAILED, PENDING
    @Column(nullable = false)
    private String status;

    // Transaction timestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionDate;

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
        if (status == null) {
            status = "SUCCESS";
        }
    }
}
