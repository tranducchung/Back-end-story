package com.codegym.service;

import com.codegym.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
<<<<<<< HEAD
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
=======

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

>>>>>>> nbthanh
    void save(User user);
}
