package com.sarujan.bankingapp.service;

import com.sarujan.bankingapp.exception.TransactionException;
import com.sarujan.bankingapp.model.Account;
import com.sarujan.bankingapp.model.Transaction;
import com.sarujan.bankingapp.repository.AccountRepository;
import com.sarujan.bankingapp.repository.TransactionRepository;
import com.sarujan.bankingapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository,
                              UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new TransactionException("Unauthenticated request");
        }
        return auth.getName();
    }

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new TransactionException("Sender and receiver accounts must be different");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Transfer amount must be positive");
        }

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new TransactionException("Sender account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new TransactionException("Receiver account not found"));

        // Authorization: sender account must belong to logged-in user
        String username = getAuthenticatedUsername();
        Long authUserId = userRepository.findByUsername(username)
                .orElseThrow(() -> new TransactionException("Authenticated user not found"))
                .getId();

        if (!fromAccount.getUser().getId().equals(authUserId)) {
            throw new TransactionException("You are not allowed to transfer from this account");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new TransactionException("Insufficient funds");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction(fromAccount, toAccount, amount, "SUCCESS");
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getTransactionsByUser(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Transaction> getMyTransactions() {
        String username = getAuthenticatedUsername();

        Long authUserId = userRepository.findByUsername(username)
                .orElseThrow(() -> new TransactionException("Authenticated user not found"))
                .getId();

        return transactionRepository.findAllByUserId(authUserId);
    }

}
