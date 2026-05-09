package com.Petre_Daria.banking_app.dto;

import com.Petre_Daria.banking_app.model.TransactionType;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long id,
        double amount,
        TransactionType type,
        LocalDateTime timestamp,
        String fromIban,
        String toIban,
        double balance
) {}