package com.guilhermerodrigues.picpaychallenge.services;

import com.guilhermerodrigues.picpaychallenge.dtos.UserRequestDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.UserResponseDTO;
import com.guilhermerodrigues.picpaychallenge.entities.User;
import com.guilhermerodrigues.picpaychallenge.entities.UserType;
import com.guilhermerodrigues.picpaychallenge.exceptions.BadRequestException;
import com.guilhermerodrigues.picpaychallenge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<UserResponseDTO> create(UserRequestDTO data) {
        if(
            data.firstName() == null ||
            data.lastName() == null ||
            data.document() == null ||
            data.email() == null ||
            data.password() == null ||
            data.userType() == null ||
            !(data.userType() == UserType.COMMON || data.userType() == UserType.MERCHANT)
        ) {
            throw new BadRequestException("The body is incorrect.");
        }

        if(userRepository.findByEmail(data.email()) != null) {
            throw new BadRequestException("This email is already registered.");
        }

        if(userRepository.findByDocument(data.document()) != null) {
            throw new BadRequestException("This document is already registered.");
        }

        String encode = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data, encode);

        userRepository.save(newUser);

        UserResponseDTO user = new UserResponseDTO(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream().map(UserResponseDTO::new).collect(Collectors.toList());
    }
}
