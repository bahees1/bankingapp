package com.sarujan.bankingapp.repository;

import com.sarujan.bankingapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
