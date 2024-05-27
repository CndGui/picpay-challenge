package com.guilhermerodrigues.picpaychallenge.controllers;

import com.guilhermerodrigues.picpaychallenge.dtos.TransactionRequestDTO;
import com.guilhermerodrigues.picpaychallenge.dtos.TransactionResponseDTO;
import com.guilhermerodrigues.picpaychallenge.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/transfer")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping()
    public ResponseEntity<TransactionResponseDTO> sendTransaction(@RequestBody TransactionRequestDTO data) {
        return transactionService.createTransaction(data);
    }
}
