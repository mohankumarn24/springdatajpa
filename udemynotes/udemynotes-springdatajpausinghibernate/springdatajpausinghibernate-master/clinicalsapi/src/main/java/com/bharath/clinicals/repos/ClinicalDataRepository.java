package com.bharath.clinicals.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.clinicals.model.ClinicalData;

public interface ClinicalDataRepository extends JpaRepository<ClinicalData, Integer> {

}
