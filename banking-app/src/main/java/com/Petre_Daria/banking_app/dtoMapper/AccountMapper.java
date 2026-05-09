package com.Petre_Daria.banking_app.dtoMapper;

import com.Petre_Daria.banking_app.dto.AccountResponseDTO;
import com.Petre_Daria.banking_app.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponseDTO toDto(Account account) {
        if (account == null) return null;

        return new AccountResponseDTO(
                account.getId(),
                account.getIban(),
                account.getBalance(),
                account.getType(),
                account.getCreatedDate()
        );
    }
}