package com.guilhermerodrigues.picpaychallenge.repositories;

import com.guilhermerodrigues.picpaychallenge.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
