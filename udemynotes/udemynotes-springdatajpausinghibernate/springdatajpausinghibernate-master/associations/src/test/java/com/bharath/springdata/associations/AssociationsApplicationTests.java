package com.bharath.springdata.associations;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.springdata.associations.manytomany.entities.Programmer;
import com.bharath.springdata.associations.manytomany.entities.Project;
import com.bharath.springdata.associations.manytomany.repos.ProgrammerRepository;
import com.bharath.springdata.associations.onetomany.entities.Customer;
import com.bharath.springdata.associations.onetomany.entities.PhoneNumber;
import com.bharath.springdata.associations.onetomany.repos.CustomerRepository;
import com.bharath.springdata.associations.onetoone.entities.License;
import com.bharath.springdata.associations.onetoone.entities.Person;
import com.bharath.springdata.associations.onetoone.repos.LicenseRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssociationsApplicationTests {

	@Autowired
	CustomerRepository repository;

	@Autowired
	ProgrammerRepository programmerRepository;

	@Autowired
	LicenseRepository licenseRepository;

	@Test
	public void contextLoads() {
	}

	// OneToMany: save new data
	@Test
	public void testCreateCustomer() {

		Customer customer = new Customer();
		customer.setName("John");

		PhoneNumber ph1 = new PhoneNumber();
		ph1.setNumber("1234567890");
		ph1.setType("cell");

		PhoneNumber ph2 = new PhoneNumber();
		ph2.setNumber("0987654321");
		ph2.setType("home");

		/* NOT A BETTER WAY
		// if below 2 lines are not added: entries will be added to customers and phone_numbers table. But but foreign key column value in phone_number table will be null
		ph1.setCustomer(customer);
		ph2.setCustomer(customer);

		HashSet<PhoneNumber> numbers = new HashSet<>();
		numbers.add(ph1);
		numbers.add(ph2);
		customer.setNumbers(numbers);
		*/

		// BETTER WAY
		customer.addPhoneNumber(ph1);
		customer.addPhoneNumber(ph2);

		repository.save(customer);
	}

	// OneToMany: read data
	@Test
	@Transactional // needed if LAZY loading is enabled
	public void testLoadCustomer() {
		Customer customer = repository.findById(4L).get();
		System.out.println(customer.getName());

		Set<PhoneNumber> numbers = customer.getNumbers(); // in lazy loading, phone_numbers will be fetched only if we call getNumbers() method. Uses separate sql query
		numbers.forEach(number -> System.out.println(number.getNumber()));

		// EAGER: Single SQL query with inner joins to get customer data and phone number data
		// LAZY: First query to get customer data. Second query with inner join to get phone numbers when we invoke customer.getNumbers()
	}

	// OneToMany: update data
	@Test
	public void testUpdateCustomer() {
		Customer customer = repository.findById(4L).get();
		customer.setName("John Bush");

		Set<PhoneNumber> numbers = customer.getNumbers();
		numbers.forEach(number -> number.setType("cell"));   
		// Hibernate does Dirty Check
		// Since we have two numbers with values 'cell' and 'home', only one update query is generated to update 'cell' -> 'home' as other number already has 'cell' value

		repository.save(customer);

	}

	// OneToMany: delete data
	@Test
	public void testDelete() {
		repository.deleteById(4l);
		// deletes 2 associated phone numbers and then deletes customer (3 sql queries generated)
	}

	// ManyToMany: create data
	@Test
	public void testmtomCreateProgrammer() {
		Programmer programmer = new Programmer();
		programmer.setName("John");
		programmer.setSal(10000);

		HashSet<Project> projects = new HashSet<Project>();
		Project project = new Project();
		project.setName("Hibernate Project");
		projects.add(project);

		programmer.setProjects(projects);

		programmerRepository.save(programmer);

		// 3 sql queries: programmer table, project table, programmer_project table insert queries
	}

	// ManyToMany: get data
	@Test
	@Transactional	// needed if LAZY loading is enabled
	public void testmtomFindProgrammer() {
		Programmer programmer = programmerRepository.findById(1).get();
		System.out.println(programmer);					// Programmer [id=1, name=John, sal=10000]
		System.out.println(programmer.getProjects());	// Project [id=1, name=Hibernate Project]
	}

	// OneToOne: create data
	@Test
	public void testOneToOneCreateLicense() {
		License license = new License();
		license.setType("CAR");
		license.setValidFrom(new Date());
		license.setValidTo(new Date());

		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Clinton");
		person.setAge(35);

		license.setPerson(person);

		licenseRepository.save(license);
	}

}
