package com.guilhermerodrigues.picpaychallenge.dtos;

import java.math.BigDecimal;

public record TransactionRequestDTO(BigDecimal amount, Long payee) {
}
