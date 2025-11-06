package com.sarujan.bankingapp.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal; // better for bank balances avoids rounding issues

@Entity
@Table(name = "accounts") // creating accounts table, store account type, balance, etc...
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountType; // e.g., "Checking", "Savings"

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    // Many accounts belong to one user
    @ManyToOne(fetch = FetchType.LAZY) // used to create a foreign key to the users table
    @JoinColumn(name = "user_id", nullable = false) // defines the db col name for this foreign relationship
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    // Constructors
    public Account() {}

    public Account(String accountType, BigDecimal balance, User user) {
        this.accountType = accountType;
        this.balance = balance;
        this.user = user;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
