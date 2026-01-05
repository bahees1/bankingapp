package com.sarujan.bankingapp.service;

import com.sarujan.bankingapp.model.Account;
import com.sarujan.bankingapp.model.User;
import com.sarujan.bankingapp.repository.AccountRepository;
import com.sarujan.bankingapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }



    public Account createAccount(Long userId, String accountType, BigDecimal initialBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)); // user has to exist for them to have an account

        Account account = new Account(accountType, initialBalance, user);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    public List<Account> getAccountsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return accountRepository.findByUserId(user.getId());
    }

    public Account depositToAccount(String username, Long accountId, BigDecimal amount) {
        // Validate amount
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        // Find authenticated user
        User authUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        // Find account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));

        // Ownership check
        if (!account.getUser().getId().equals(authUser.getId())) {
            throw new AccessDeniedException("You do not own this account");
        }

        // Update balance safely using BigDecimal
        account.setBalance(account.getBalance().add(amount));

        // Save + return updated account
        return accountRepository.save(account);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    public List<Account> getMyAccounts() {
        User user = getCurrentUser();
        return accountRepository.findByUserId(user.getId());
    }

    public Account createMyAccount(String accountType, BigDecimal initialBalance) {
        User user = getCurrentUser();

        if (!accountType.equalsIgnoreCase("Checking") &&
                !accountType.equalsIgnoreCase("Savings")) {
            throw new RuntimeException("Invalid account type");
        }

        Account account = new Account(accountType, initialBalance, user);
        return accountRepository.save(account);
    }




}
