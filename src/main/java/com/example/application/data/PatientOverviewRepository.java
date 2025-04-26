package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PatientOverviewRepository extends
        JpaRepository<PatientOverview, Long>,
        JpaSpecificationExecutor<PatientOverview> {
        Optional<PatientOverview> findByUser(User user);
        List<PatientOverview> findByUserIn(List<User> users);


}