package com.Petre_Daria.banking_app.dtoMapper;

import com.Petre_Daria.banking_app.dto.TransactionResponseDTO;
import com.Petre_Daria.banking_app.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponseDTO toDto(Transaction transaction) {
        if (transaction == null) return null;

        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getTimestamp(),
                transaction.getAccount().getIban(),
                transaction.getDestinationAccount() != null
                        ? transaction.getDestinationAccount().getIban()
                        : null,
                transaction.getAccount().getBalance()
        );
    }
}