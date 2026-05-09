package com.Petre_Daria.banking_app.repository;

import com.Petre_Daria.banking_app.model.Account;
import com.Petre_Daria.banking_app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findByAccountId(Long accountId);
}
