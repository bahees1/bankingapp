package com.sarujan.bankingapp.controller;

import com.sarujan.bankingapp.model.Transaction;
import com.sarujan.bankingapp.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public Transaction makeTransfer(@RequestParam Long fromAccountId,
                                    @RequestParam Long toAccountId,
                                    @RequestParam BigDecimal amount) {
        return transactionService.transfer(fromAccountId, toAccountId, amount);
    }
}
