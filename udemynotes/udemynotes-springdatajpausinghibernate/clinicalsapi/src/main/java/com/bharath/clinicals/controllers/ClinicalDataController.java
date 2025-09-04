package com.bharath.clinicals.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bharath.clinicals.dto.ClinicalDataRequest;
import com.bharath.clinicals.model.ClinicalData;
import com.bharath.clinicals.model.Patient;
import com.bharath.clinicals.repos.ClinicalDataRepository;
import com.bharath.clinicals.repos.PatientRepository;

@RestController
@RequestMapping("/api")
public class ClinicalDataController {
	private ClinicalDataRepository clinicalDataRepository;
	private PatientRepository patientRepository;
	
	ClinicalDataController(ClinicalDataRepository clinicalDataRepository,PatientRepository patientRepository){
		this.clinicalDataRepository = clinicalDataRepository;
		this.patientRepository = patientRepository;
	}
	@PostMapping("/clinicals")
	public ClinicalData saveClinicalData(@RequestBody ClinicalDataRequest request) {
		Patient patient = patientRepository.findById(request.getPatientId()).get();
		ClinicalData clinicalData = new ClinicalData();
		clinicalData.setComponentName(request.getComponentName());
		clinicalData.setComponentValue(request.getComponentValue());
		clinicalData.setPatient(patient);

		return clinicalDataRepository.save(clinicalData);
		
	}

}
