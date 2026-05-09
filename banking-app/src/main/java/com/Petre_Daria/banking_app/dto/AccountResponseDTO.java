package com.Petre_Daria.banking_app.dto;

import com.Petre_Daria.banking_app.model.AccountType;

import java.time.LocalDateTime;

public record AccountResponseDTO(
        Long id,
        String iban,
        double balance,
        AccountType type,
        LocalDateTime createdDate
) {}