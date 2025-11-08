package com.sarujan.bankingapp.dto;

import com.sarujan.bankingapp.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {

    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private String status;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.fromAccountId = transaction.getFromAccount().getId();
        this.toAccountId = transaction.getToAccount().getId();
        this.amount = transaction.getAmount();
        this.timestamp = transaction.getTimestamp();
        this.status = transaction.getStatus();
    }

    // Getters only
    public Long getId() { return id; }
    public Long getFromAccountId() { return fromAccountId; }
    public Long getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getStatus() { return status; }
}
