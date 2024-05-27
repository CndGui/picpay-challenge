package com.guilhermerodrigues.picpaychallenge.services;

import com.guilhermerodrigues.picpaychallenge.dtos.AuthenticationDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.LoginResponseDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.UserRequestDTO;
import com.guilhermerodrigues.picpaychallenge.entities.User;
import com.guilhermerodrigues.picpaychallenge.entities.UserType;
import com.guilhermerodrigues.picpaychallenge.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;

@SpringBootTest
@ActiveProfiles("test")
public class AuthenticationServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @BeforeAll
    static void createUser(@Autowired UserRepository userRepository) {
        String encode = new BCryptPasswordEncoder().encode("guilherme");

        UserRequestDTO userRequestDTO = new UserRequestDTO(
            "Guilherme", "Rodrigues", "99988899989", "gui@hotmail.com", "guilherme", UserType.COMMON
        );

        User user = new User(userRequestDTO, encode);

        userRepository.save(user);
    }

    @Test
    @DisplayName("Should return a token when email and password is correct.")
    void login() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("gui@hotmail.com", "guilherme");

        ResponseEntity<LoginResponseDTO> login = authenticationService.login(authenticationDTO);

        Assertions.assertNotNull(Objects.requireNonNull(login.getBody()).access_token());
    }

    @Test
    @DisplayName("Should return a forbidden exception when password is incorrect.")
    void loginCase2() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("gui@hotmail.com", "guilhermeee");

        Assertions.assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(authenticationDTO);
        });
    }
}
