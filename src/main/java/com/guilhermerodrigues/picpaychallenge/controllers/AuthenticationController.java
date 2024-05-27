package com.guilhermerodrigues.picpaychallenge.controllers;

import com.guilhermerodrigues.picpaychallenge.dtos.AuthenticationDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.LoginResponseDTO;
import com.guilhermerodrigues.picpaychallenge.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService service;

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO data) {
        return this.service.login(data);
    }
}
