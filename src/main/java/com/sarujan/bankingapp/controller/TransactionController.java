package com.sarujan.bankingapp.controller;

import com.sarujan.bankingapp.dto.TransactionDTO;
import com.sarujan.bankingapp.model.Transaction;
import com.sarujan.bankingapp.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public TransactionDTO makeTransfer(@RequestParam Long fromAccountId,
                                       @RequestParam Long toAccountId,
                                       @RequestParam BigDecimal amount) {
        Transaction transaction = transactionService.transfer(fromAccountId, toAccountId, amount);
        return new TransactionDTO(transaction); // map entity -> DTO
    }

    @GetMapping("/history/{userId}")
    public List<TransactionDTO> getUserTransactionHistory(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getTransactionsByUser(userId);

        // Map each Transaction to TransactionDTO
        return transactions.stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }
    @GetMapping("/me")
    public List<TransactionDTO> getMyTransactionHistory() {
        List<Transaction> transactions = transactionService.getMyTransactions();

        return transactions.stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());
    }

}
