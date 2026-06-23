package com.springboot.backend.carlos.usersapp.users_backed.services;

import com.springboot.backend.carlos.usersapp.users_backed.model.User;

import java.util.List;

public interface UsersService {
    User findById(Long id);
    User findByName(String name);
    List<User> findAll();
    User save(User user);
    void delete(Long id);
}
