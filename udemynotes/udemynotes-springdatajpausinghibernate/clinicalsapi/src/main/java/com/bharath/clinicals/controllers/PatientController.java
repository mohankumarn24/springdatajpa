package com.bharath.clinicals.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bharath.clinicals.model.ClinicalData;
import com.bharath.clinicals.model.Patient;
import com.bharath.clinicals.repos.PatientRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PatientController {

	private PatientRepository repository;
	Map<String, String> filters = new HashMap<>();

	PatientController(PatientRepository repository) {
		this.repository = repository;
	}

	@GetMapping(value = "/patients")
	public List<Patient> getPatients() {
		return repository.findAll();

	}

	@GetMapping(value = "/patients/{id}")
	public Patient getPatient(@PathVariable int id) {
		return repository.findById(id).get();
	}

	@PostMapping(value = "/patients")
	public Patient savePatient(@RequestBody Patient patient) {
		return repository.save(patient);
	}

	@GetMapping(value = "/patients/analyze/{id}")
	public Patient analyze(@PathVariable int id) {
		Patient patient = repository.findById(id).get();
		List<ClinicalData> clinicalData = patient.getClinicalData();
		List<ClinicalData> duplicateClinicalData = new ArrayList<>(clinicalData);
		for (ClinicalData eachEntry : duplicateClinicalData) {

			if (filters.containsKey(eachEntry.getComponentName())) {
				clinicalData.remove(eachEntry);
				continue;
			} else {
				filters.put(eachEntry.getComponentName(), null);
			}

			if (eachEntry.getComponentName().equals("hw")) {
				String[] heightAndWeight = eachEntry.getComponentValue().split("/");
				if (heightAndWeight != null && heightAndWeight.length > 1) {

					float heightInMeters = Float.parseFloat(heightAndWeight[0]) * 0.4536F;
					float bmi = Float.parseFloat(heightAndWeight[1]) / (heightInMeters * heightInMeters);
					ClinicalData bmiData = new ClinicalData();
					bmiData.setComponentName("bmi");
					bmiData.setComponentValue(Float.toString(bmi));
					clinicalData.add(bmiData);
				}
			}
		}
		filters.clear();
		return patient;

	}

}
