package com.guilhermerodrigues.picpaychallenge.dtos;

import com.guilhermerodrigues.picpaychallenge.entities.UserType;

public record UserRequestDTO(String firstName, String lastName, String document, String email, String password, UserType userType) {
}
