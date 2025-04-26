package com.example.application.services;

import com.example.application.data.User;
import com.example.application.data.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    public User save(User entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public Boolean userNameAvailable(String username) {
        return repository.findByUsername(username).isEmpty();
    }

    public List<User> getUsers() {
        return this.repository.findAll();
    }

    public List<User> searchUsersByName(String filter) {
        if (filter == null || filter.isEmpty()) {
            return repository.findAll();
        }
        return repository.findByUsernameContainingIgnoreCase(filter);
    }

    public int countUsersByName(String filter) {
        if (filter == null || filter.isEmpty()) {
            return (int) repository.count();
        }
        return repository.countByUsernameContainingIgnoreCase(filter);
    }

    public User findByName(String name) {
        return this.repository.findUserByName(name);
    }

    public List<User> findByNameStartingWith(String name) {
        if (name == null || name.isEmpty()) {
            return Collections.emptyList();
        }
        return repository.findByNameStartingWithIgnoreCase(name);
    }
}
