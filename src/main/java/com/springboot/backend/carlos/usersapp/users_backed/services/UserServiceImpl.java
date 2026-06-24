package com.springboot.backend.carlos.usersapp.users_backed.services;

import com.springboot.backend.carlos.usersapp.users_backed.model.User;
import com.springboot.backend.carlos.usersapp.users_backed.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<User> findByUserName(String name) {
        return userRepository.findByUserName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List)this.userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
