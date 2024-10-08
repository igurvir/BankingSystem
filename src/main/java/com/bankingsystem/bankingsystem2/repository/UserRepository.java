package com.bankingsystem.bankingsystem2.repository;

import com.bankingsystem.bankingsystem2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
