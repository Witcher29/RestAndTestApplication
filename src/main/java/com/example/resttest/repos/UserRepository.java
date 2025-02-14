package com.example.resttest.repos;

import com.example.resttest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById (Long id);
    User findByName (String name);
    User findByPassword (String password);

    Boolean existsByName (String name);

}
