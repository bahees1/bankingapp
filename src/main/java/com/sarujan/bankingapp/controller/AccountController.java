package com.sarujan.bankingapp.controller;

import com.sarujan.bankingapp.model.Account;
import com.sarujan.bankingapp.service.AccountService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Create new account for a user
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> payload) {
        Long userId = ((Number) payload.get("userId")).longValue();
        String accountType = (String) payload.get("accountType");
        BigDecimal initialBalance = new BigDecimal(payload.get("initialBalance").toString());

        Account account = accountService.createAccount(userId, accountType, initialBalance);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    // Get all accounts for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUserId(@PathVariable Long userId) {
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }

    // Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }
}
