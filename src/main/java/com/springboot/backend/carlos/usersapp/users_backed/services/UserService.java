package com.springboot.backend.carlos.usersapp.users_backed.services;

import com.springboot.backend.carlos.usersapp.users_backed.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);
    Optional<User> findByUserName(String name);
    List<User> findAll();
    User save(User user);
    void deleteById(Long id);
}
