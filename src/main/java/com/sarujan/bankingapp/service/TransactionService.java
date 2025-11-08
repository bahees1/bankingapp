package com.sarujan.bankingapp.service;

import com.sarujan.bankingapp.model.Account;
import com.sarujan.bankingapp.model.Transaction;
import com.sarujan.bankingapp.exception.TransactionException;
import com.sarujan.bankingapp.repository.AccountRepository;
import com.sarujan.bankingapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (fromAccountId.equals(toAccountId)) {
            throw new TransactionException("Sender and receiver accounts must be different");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Transfer amount must be positive");
        }

        // Fetch accounts
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new TransactionException("Sender account not found"));

        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new TransactionException("Receiver account not found"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new TransactionException("Insufficient funds");
        }

        // Perform transfer safely with BigDecimal
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction(fromAccount, toAccount, amount, "SUCCESS");
        return transactionRepository.save(transaction);
    }

    @Transactional(readOnly = true) // optional, improves performance for read-only queries
    public List<Transaction> getTransactionsByUser(Long userId) {
        return transactionRepository.findAllByUserId(userId);
    }
}
