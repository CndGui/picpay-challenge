package com.guilhermerodrigues.picpaychallenge.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receive_id")
    private User receive;

    private LocalDateTime timestamp;

    public Transaction(BigDecimal amount, User sender, User receive, LocalDateTime timestamp) {
        this.amount = amount;
        this.sender = sender;
        this.receive = receive;
        this.timestamp = timestamp;
    }
}
