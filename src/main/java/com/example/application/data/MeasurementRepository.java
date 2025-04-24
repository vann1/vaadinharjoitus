package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MeasurementRepository  extends JpaRepository<Measurement, Long>, JpaSpecificationExecutor<Measurement> {

    Optional<Measurement> findByMeasurementName(String measurementName);
}
