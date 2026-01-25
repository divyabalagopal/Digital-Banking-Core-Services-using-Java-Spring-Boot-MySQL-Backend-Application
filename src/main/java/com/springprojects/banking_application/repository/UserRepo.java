package com.springprojects.banking_application.repository;

import com.springprojects.banking_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepo extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);

    User findByAccountNumber(String accountNumber);
}
