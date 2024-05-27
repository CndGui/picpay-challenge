package com.guilhermerodrigues.picpaychallenge.services;

import com.guilhermerodrigues.picpaychallenge.entities.User;
import com.guilhermerodrigues.picpaychallenge.exceptions.BadRequestException;
import com.guilhermerodrigues.picpaychallenge.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.findByEmail(email);
        if(user == null) {
            throw new BadRequestException("User with email " + email + " does not exist.");
        }

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .build();
    }
}
