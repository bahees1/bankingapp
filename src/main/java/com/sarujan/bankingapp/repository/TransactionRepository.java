package com.sarujan.bankingapp.repository;

import com.sarujan.bankingapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Fetch all transactions where the user is either sender or receiver
    @Query("SELECT t FROM Transaction t " +
            "WHERE t.fromAccount.user.id = :userId OR t.toAccount.user.id = :userId " +
            "ORDER BY t.timestamp DESC")
    List<Transaction> findAllByUserId(@Param("userId") Long userId);
}