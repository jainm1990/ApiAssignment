package com.api.assignment.repository;

import com.api.assignment.dto.User;
import com.api.assignment.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDetails, Long> {

    Optional<UserDetails> findByemail(String email);
}
