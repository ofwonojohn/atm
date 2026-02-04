package com.atm.atm.init;

import com.atm.atm.entity.Account;
import com.atm.atm.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialize sample data on application startup.
 * Creates test accounts for demonstration purposes.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) {
        // Clear existing data
        accountRepository.deleteAll();

        // Create sample accounts
        Account account1 = new Account();
        account1.setAccountNumber("1001");
        account1.setPin("1234");
        account1.setAccountHolderName("John Paul");
        account1.setBalance(50000.0);
        account1.setStatus("ACTIVE");
        account1.setEmail("johnp@gmail.com");
        account1.setPhoneNumber("0706647669");

        Account account2 = new Account();
        account2.setAccountNumber("1002");
        account2.setPin("5678");
        account2.setAccountHolderName("Derrick");
        account2.setBalance(75000.0);
        account2.setStatus("ACTIVE");
        account2.setEmail("derrick@gmail.com");
        account2.setPhoneNumber("0778654321");

        Account account3 = new Account();
        account3.setAccountNumber("1003");
        account3.setPin("9012");
        account3.setAccountHolderName("Mary");
        account3.setBalance(100000.0);
        account3.setStatus("ACTIVE");
        account3.setEmail("mary@gmail.com");
        account3.setPhoneNumber("0741234567");

        // Save sample accounts
        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);

        System.out.println("Sample accounts initialized successfully!");
        System.out.println("Test Credentials:");
        System.out.println("Account 1: 1001 / PIN: 1234");
        System.out.println("Account 2: 1002 / PIN: 5678");
        System.out.println("Account 3: 1003 / PIN: 9012");
    }
}
