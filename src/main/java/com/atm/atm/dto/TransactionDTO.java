package com.atm.atm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO for Transaction response.
 * Used to transfer transaction data to the client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;
    private String transactionType;
    private Double amount;
    private Double balanceAfterTransaction;
    private String description;
    private String status;
    private LocalDateTime transactionDate;
}
