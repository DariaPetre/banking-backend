package com.Petre_Daria.banking_app.service;

import com.Petre_Daria.banking_app.model.Account;
import com.Petre_Daria.banking_app.model.AccountType;
import com.Petre_Daria.banking_app.model.User;
import com.Petre_Daria.banking_app.repository.AccountRepository;
import com.Petre_Daria.banking_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(long userId, AccountType accountType) {
        User user =  userRepository.findById(userId).orElseThrow(()
                     -> new RuntimeException("User not found, sorry we can't create the account!"));

        String iban = createIban();
        Account account = new Account();
        account.setIban(iban);
        account.setBalance(0);
        account.setType(accountType);
        account.setCreatedDate(LocalDateTime.now());
        account.setUser(user);
        return accountRepository.save(account);

    }

    public String createIban()
    {
        //RO + CC + DARB + 16-characters
        String countryCode = "RO";
        String bankCode = "DARB";
        String accountCharacters = createAccountCharacters();
        String controlNumbers = createControlNumber(accountCharacters);
        String iban = countryCode + controlNumbers + bankCode + accountCharacters;
        while(accountRepository.existsByIban(iban))
        {
            accountCharacters = createAccountCharacters();
            controlNumbers = createControlNumber(accountCharacters);
            iban = countryCode + controlNumbers + bankCode + accountCharacters;
        }

        return iban;
    }

    public String createAccountCharacters()
    {
        final String characters ="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final int requiredCharCount = 16 ;
        Random rnd = new Random();
        StringBuilder toReturn = new StringBuilder();
        for(int i = 0; i < requiredCharCount; i++)
            toReturn.append(characters.charAt(rnd.nextInt(characters.length())));
        return toReturn.toString();
    }

    public String createControlNumber(String accountCharacters){
        String str = "DARB" +
                     accountCharacters +
                     "RO00";
        StringBuilder stringBuilder = new StringBuilder();

        int remainder = 0;
        int cc;

        for(int i = 0; i < str.length(); i++){
            stringBuilder.append(Character.getNumericValue(str.charAt(i)));
        }

        for(int i = 0; i < stringBuilder.length(); i++){
            int digit = stringBuilder.charAt(i) - '0';
            remainder = (remainder * 10 + digit) % 97;
        }

        cc = 98 - remainder;

        if(cc > 9)
          return String.valueOf(cc);
        else
            return "0" + cc;
    }

    public double getBalance(String iban) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return account.getBalance();
    }

    public Optional<Account> getAccountByIban(String iban) {
        return accountRepository.findByIban(iban);
    }

    public Optional<Account> getAccountById(Long id) { return  accountRepository.findById(id); }

    public List<Account> getAccountsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        return accountRepository.findByUserId(userId);
    }

    public List<Account> getAllAccounts() { return  accountRepository.findAll(); }

    public List<Account> getAccountsByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return accountRepository.findByUserId(user.getId());
    }

    public Account createAccountForUserEmail(String email, AccountType accountType) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return createAccount(user.getId(), accountType);
    }
}
