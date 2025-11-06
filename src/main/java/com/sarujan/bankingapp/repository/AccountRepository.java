package com.sarujan.bankingapp.repository;

import com.sarujan.bankingapp.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);  // Custom query to get all accounts for a user
}
