package com.example.application.data;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);
    User findUserByName(String name);
    List<User> findByUsernameContainingIgnoreCase(String username);
    int countByUsernameContainingIgnoreCase(String username);

    List<User> findByNameStartingWithIgnoreCase(String name);
}
