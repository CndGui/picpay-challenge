package com.guilhermerodrigues.picpaychallenge.dtos;

import com.guilhermerodrigues.picpaychallenge.entities.Transaction;
import com.guilhermerodrigues.picpaychallenge.entities.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(BigDecimal amount, Long payer, Long payee, LocalDateTime timestamp) {

    public TransactionResponseDTO(Transaction transaction) {
        this(
            transaction.getAmount(),
            transaction.getSender().getId(),
            transaction.getReceive().getId(),
            transaction.getTimestamp()
        );
    }
}
