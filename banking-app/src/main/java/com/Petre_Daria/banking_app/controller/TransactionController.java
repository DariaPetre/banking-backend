package com.Petre_Daria.banking_app.controller;

import com.Petre_Daria.banking_app.dto.TransactionResponseDTO;
import com.Petre_Daria.banking_app.dtoMapper.TransactionMapper;
import com.Petre_Daria.banking_app.model.Transaction;
import com.Petre_Daria.banking_app.service.TransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService,
                                 TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('USER')")
    public TransactionResponseDTO deposit(@RequestParam Long accountId,
                                          @RequestParam double amount,
                                          Authentication authentication) {

        String email = authentication.getName();

        Transaction transaction = transactionService.deposit(accountId, amount, email);

        return transactionMapper.toDto(transaction);
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('USER')")
    public TransactionResponseDTO withdraw(@RequestParam Long accountId,
                                           @RequestParam double amount,
                                           Authentication authentication) {

        String email = authentication.getName();

        Transaction transaction = transactionService.withdraw(accountId, amount, email);

        return transactionMapper.toDto(transaction);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public TransactionResponseDTO transfer(@RequestParam String fromIban,
                                           @RequestParam String toIban,
                                           @RequestParam double amount,
                                           Authentication authentication) {

        String email = authentication.getName();

        Transaction transaction = transactionService.transfer(fromIban, toIban, amount, email);

        return transactionMapper.toDto(transaction);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionService.getAllTransactions()
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public List<TransactionResponseDTO> getTransactionsForAccount(@PathVariable Long accountId,
                                                                  Authentication authentication) {

        String email = authentication.getName();

        return transactionService.getTransactionsForAccount(accountId, email)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }
}