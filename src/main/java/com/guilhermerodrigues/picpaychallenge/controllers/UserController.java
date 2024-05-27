package com.guilhermerodrigues.picpaychallenge.controllers;

import com.guilhermerodrigues.picpaychallenge.dtos.UserRequestDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.UserResponseDTO;
import com.guilhermerodrigues.picpaychallenge.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping
    public List<UserResponseDTO> findAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserRequestDTO data) {
        return service.create(data);
    }
}
