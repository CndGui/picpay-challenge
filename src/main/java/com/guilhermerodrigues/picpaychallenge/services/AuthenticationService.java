package com.guilhermerodrigues.picpaychallenge.services;

import com.guilhermerodrigues.picpaychallenge.dtos.AuthenticationDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.LoginResponseDTO;
import com.guilhermerodrigues.picpaychallenge.exceptions.BadRequestException;
import com.guilhermerodrigues.picpaychallenge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    public ResponseEntity<LoginResponseDTO> login(AuthenticationDTO data) {
        if(data.email() == null || data.password() == null) {
            throw new BadRequestException("The body is incorrect.");
        }

        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authenticate = authenticationManager.authenticate(user);

        String token = tokenService.generateToken((UserDetails) authenticate.getPrincipal());

        return ResponseEntity.ok().body(new LoginResponseDTO(token));
    }
}
