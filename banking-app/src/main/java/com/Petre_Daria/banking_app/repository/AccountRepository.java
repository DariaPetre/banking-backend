package com.Petre_Daria.banking_app.repository;

import com.Petre_Daria.banking_app.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByIban(String iban);
    Optional<Account> findByIban(String iban);
    List<Account> findByUserId(Long userId);
}
