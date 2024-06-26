package com.example.MiniZanzibarBack.repository;

import com.example.MiniZanzibarBack.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseJPARepository<User> {
    Optional<User> findByEmail(String email);
}
