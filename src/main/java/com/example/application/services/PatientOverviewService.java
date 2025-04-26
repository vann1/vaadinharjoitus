package com.example.application.services;

import com.example.application.data.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PatientOverviewService {
    private final UserService userService;
    private final PatientOverviewRepository repository;

    public PatientOverviewService(PatientOverviewRepository repository,
                                  UserService userService) {
        this.userService = userService;
        this.repository = repository;
    }

    public Optional<PatientOverview> get(Long id) {
        return repository.findById(id);
    }

    public PatientOverview save(PatientOverview entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<PatientOverview> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<PatientOverview> list(Pageable pageable, Specification<PatientOverview> filter) {
        return repository.findAll(filter, pageable);
    }



    public Optional<PatientOverview> getPatientOverviewByUser(User user) {
        return repository.findByUser(user);
    }

    public int count() {
        return (int) repository.count();
    }
    public List<PatientOverview> findByNameStartingWith(String name) {
        // Hae käyttäjät, joiden username alkaa annetulla merkkijonolla
        List<User> users = userService.findByNameStartingWith(name);
        if (users.isEmpty()) {
            return Collections.emptyList();
        }
        // Hae PatientOverview-entiteetit näille käyttäjille
        return repository.findByUserIn(users);
    }

}
