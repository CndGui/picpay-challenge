package com.guilhermerodrigues.picpaychallenge.dtos;

import com.guilhermerodrigues.picpaychallenge.entities.User;
import com.guilhermerodrigues.picpaychallenge.entities.UserType;

import java.math.BigDecimal;

public record UserResponseDTO(Long id, String firstName, String lastName, String document, String email, BigDecimal balance, UserType userType) {
    public UserResponseDTO(User user) {
        this(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getDocument(),
            user.getEmail(),
            user.getBalance(),
            user.getUserType()
        );
    }
}
