package com.Petre_Daria.banking_app.controller;

import com.Petre_Daria.banking_app.dto.AccountResponseDTO;
import com.Petre_Daria.banking_app.dtoMapper.AccountMapper;
import com.Petre_Daria.banking_app.model.Account;
import com.Petre_Daria.banking_app.model.AccountType;
import com.Petre_Daria.banking_app.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@PreAuthorize("hasRole('USER')")
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(AccountService accountService,
                             AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/iban/{iban}")
    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponseDTO getAccountByIban(@PathVariable String iban) {
        Account account = accountService.getAccountByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return accountMapper.toDto(account);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponseDTO getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return accountMapper.toDto(account);
    }

    @GetMapping("/userId/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountResponseDTO> getAccountsByUserId(@PathVariable Long userId) {
        return accountService.getAccountsByUserId(userId)
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AccountResponseDTO> getAllAccounts() {
        return accountService.getAllAccounts()
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }

    @PostMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponseDTO createAccount(@PathVariable Long userId,
                                            @RequestParam AccountType accountType) {

        Account account = accountService.createAccount(userId, accountType);

        return accountMapper.toDto(account);
    }

    @PostMapping("/me")
    public AccountResponseDTO createMyAccount(@RequestParam AccountType accountType,
                                              Authentication authentication) {

        String email = authentication.getName();

        Account account = accountService.createAccountForUserEmail(email, accountType);

        return accountMapper.toDto(account);
    }

    @GetMapping("/me")
    public List<AccountResponseDTO> getMyAccounts(Authentication authentication) {

        String email = authentication.getName();

        return accountService.getAccountsByUserEmail(email)
                .stream()
                .map(accountMapper::toDto)
                .toList();
    }
}