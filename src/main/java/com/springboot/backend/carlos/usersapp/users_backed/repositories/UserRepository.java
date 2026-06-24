package com.springboot.backend.carlos.usersapp.users_backed.repositories;

import com.springboot.backend.carlos.usersapp.users_backed.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository <User, Long>{

    Optional<User> findByName(String name);

}
