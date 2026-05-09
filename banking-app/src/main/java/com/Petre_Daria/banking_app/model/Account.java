package com.Petre_Daria.banking_app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String iban;
    private double balance;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    private LocalDateTime createdDate =  LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Account() {  }
    public Account(String iban, double balance, AccountType type, LocalDateTime createdDate, User user) {
        this.iban = iban;
        this.balance = balance;
        this.type = type;
        this.createdDate = createdDate;
        this.user = user;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountType getType() {
        return type;
    }
    public void setType(AccountType type) {
        this.type = type;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
