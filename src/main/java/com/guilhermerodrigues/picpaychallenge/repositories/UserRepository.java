package com.guilhermerodrigues.picpaychallenge.repositories;

import com.guilhermerodrigues.picpaychallenge.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByDocument(String document);
}
