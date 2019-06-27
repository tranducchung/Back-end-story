package com.codegym.service;

import com.codegym.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    void save(User user);
    User findUserByID(Long id);

    List<User> findAllUser();

}
