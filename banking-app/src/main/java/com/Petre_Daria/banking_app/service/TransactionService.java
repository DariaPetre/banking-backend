package com.Petre_Daria.banking_app.service;

import com.Petre_Daria.banking_app.model.Account;
import com.Petre_Daria.banking_app.model.Transaction;
import com.Petre_Daria.banking_app.model.TransactionType;
import com.Petre_Daria.banking_app.model.User;
import com.Petre_Daria.banking_app.repository.AccountRepository;
import com.Petre_Daria.banking_app.repository.TransactionRepository;
import com.Petre_Daria.banking_app.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository,
                              UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void checkAccountOwnership(Account account, User user) {
        if (account.getUser() == null || account.getUser().getId() != user.getId()) {
            throw new RuntimeException("Access denied: account does not belong to authenticated user");
        }
    }

    @Transactional
    public Transaction deposit(long accountId, double amount, String email) {

        User user = getUserByEmail(email);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found, sorry we can't deposit money!"));

        checkAccountOwnership(account, user);

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(long accountId, double amount, String email) {

        User user = getUserByEmail(email);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found, sorry we can't withdraw money!"));

        checkAccountOwnership(account, user);

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        if (amount > account.getBalance()) {
            throw new RuntimeException("Amount must be less than account's balance");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction transfer(String fromIban, String toIban, double amount, String email) {

        User user = getUserByEmail(email);

        Account fromAccount = accountRepository.findByIban(fromIban)
                .orElseThrow(() -> new RuntimeException("FromAccount not found"));

        Account toAccount = accountRepository.findByIban(toIban)
                .orElseThrow(() -> new RuntimeException("ToAccount not found"));

        checkAccountOwnership(fromAccount, user);

        if (fromIban.equals(toIban)) {
            throw new RuntimeException("Source and destination IBAN cannot be the same");
        }

        if (amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        if (amount > fromAccount.getBalance()) {
            throw new RuntimeException("Amount must be less than fromAccount's balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAccount(fromAccount);
        transaction.setDestinationAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsForAccount(Long accountId, String email) {

        User user = getUserByEmail(email);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        checkAccountOwnership(account, user);

        return transactionRepository.findByAccountId(accountId);
    }
}