package com.atm.atm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * DTO for Account response.
 * Used to transfer account data without exposing sensitive information like PIN.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;
    private String accountNumber;
    private String accountHolderName;
    private Double balance;
    private String status;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime lastTransactionDate;
}
