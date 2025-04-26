package com.example.application.services;

import com.example.application.data.Measurement;
import com.example.application.data.MeasurementRepository;

import java.util.List;
import java.util.Optional;

import com.example.application.data.PatientOverview;
import com.example.application.data.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class MeasurementService {

    private final MeasurementRepository repository;

    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    public Optional<Measurement> get(Long id) {
        return repository.findById(id);
    }

    public Measurement save(Measurement entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Measurement> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Measurement> list(Pageable pageable, Specification<Measurement> filter) {
        return repository.findAll(filter, pageable);
    }
    public Optional<Measurement> getMeasurmentByUser(User user) {
        return repository.findByUser(user);
    }
    public int count() {
        return (int) repository.count();
    }

    public List<Measurement> getMeasurements (String measurement) {
        return this.repository.findByMeasurementNameStartingWithIgnoreCase(measurement);
    }
}
