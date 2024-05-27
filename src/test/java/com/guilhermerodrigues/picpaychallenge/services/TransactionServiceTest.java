package com.guilhermerodrigues.picpaychallenge.services;

import com.guilhermerodrigues.picpaychallenge.dtos.TransactionRequestDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.TransactionResponseDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.UserRequestDTO;
import com.guilhermerodrigues.picpaychallenge.entities.User;
import com.guilhermerodrigues.picpaychallenge.entities.UserType;
import com.guilhermerodrigues.picpaychallenge.exceptions.BadRequestException;
import com.guilhermerodrigues.picpaychallenge.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TransactionServiceTest {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionService transactionService;

    @BeforeAll
    static void createUsers(@Autowired UserRepository userRepository) {
        String encode = new BCryptPasswordEncoder().encode("1234");

        User user1 = new User(1L, "Guilherme", "Rodrigues", "99988899989", "gui@hotmail.com", encode, BigDecimal.valueOf(100), UserType.COMMON);
        User user2 = new User(2L, "Kaue", "Palota", "99988899988", "kaue@hotmail.com", encode, BigDecimal.valueOf(100), UserType.MERCHANT);

        userRepository.saveAll(List.of(user1, user2));
    }

    @Test
    @DisplayName("Should create a transaction when payer pass an amount correct a exist user.")
    void createTransaction() {
        User payer = userRepository.findByEmail("gui@hotmail.com");
        User payee = userRepository.findByEmail("kaue@hotmail.com");

        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(payer, null, payer.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(BigDecimal.TEN, payee.getId());

        transactionService.createTransaction(transactionRequestDTO);

        User payerAfter = userRepository.findById(payer.getId()).orElseThrow();
        User payeeAfter = userRepository.findById(payee.getId()).orElseThrow();

        BigDecimal subtract = payeeAfter.getBalance().subtract(payerAfter.getBalance());

        Assertions.assertEquals(20, subtract.intValue());
    }

    @Test
    @DisplayName("Should return a bad request exception when payer as a MERCHANT user type.")
    void createTransactionCase2() {
        User payer = userRepository.findByEmail("kaue@hotmail.com");
        User payee = userRepository.findByEmail("gui@hotmail.com");

        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(payer, null, payer.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(BigDecimal.TEN, payee.getId());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            transactionService.createTransaction(transactionRequestDTO);
        });

        Assertions.assertEquals("You can't do transactions if you is a merchant.", exception.getMessage());
    }

    @Test
    @DisplayName("Should return a bad request exception when payer amount is than lower of request amount.")
    void createTransactionCase3() {
        User payer = userRepository.findByEmail("gui@hotmail.com");
        User payee = userRepository.findByEmail("kaue@hotmail.com");

        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(payer, null, payer.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO(BigDecimal.valueOf(200), payee.getId());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            transactionService.createTransaction(transactionRequestDTO);
        });

        Assertions.assertEquals("You don't have balance for this transaction.", exception.getMessage());
    }
}