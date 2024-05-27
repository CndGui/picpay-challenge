package com.guilhermerodrigues.picpaychallenge.services;

import com.guilhermerodrigues.picpaychallenge.dtos.TransactionRequestDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.TransactionResponseDTO;
import com.guilhermerodrigues.picpaychallenge.entities.Transaction;
import com.guilhermerodrigues.picpaychallenge.entities.User;
import com.guilhermerodrigues.picpaychallenge.entities.UserType;
import com.guilhermerodrigues.picpaychallenge.exceptions.BadRequestException;
import com.guilhermerodrigues.picpaychallenge.repositories.TransactionRepository;
import com.guilhermerodrigues.picpaychallenge.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public ResponseEntity<TransactionResponseDTO> createTransaction(TransactionRequestDTO data) {
        if(
            data.amount() == null
            || data.payee() == null
        ) {
            throw new BadRequestException("The body is incorrect.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User payer = (User) authentication.getPrincipal();

        User payee = userRepository.findById(data.payee()).orElseThrow(() ->
            new BadRequestException("The payee user does not exist.")
        );

        if(payer.getUserType() == UserType.MERCHANT) {
            throw new BadRequestException("You can't do transactions if you is a merchant.");
        }

        if(data.amount().compareTo(payer.getBalance()) > 0) {
            throw new BadRequestException("You don't have balance for this transaction.");
        }

        Transaction transaction = new Transaction(data.amount(), payer, payee, LocalDateTime.now());

        payer.setBalance(payer.getBalance().subtract(data.amount()));
        payee.setBalance(payee.getBalance().add(data.amount()));

        userRepository.saveAll(Arrays.asList(payer, payee));
        transactionRepository.save(transaction);

        return ResponseEntity.ok().body(new TransactionResponseDTO(transaction));
    }
}
