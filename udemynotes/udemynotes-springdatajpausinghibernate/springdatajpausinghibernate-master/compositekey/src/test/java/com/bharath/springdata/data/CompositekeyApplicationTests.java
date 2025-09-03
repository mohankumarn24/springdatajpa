package com.bharath.springdata.data;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.bharath.springdata.data.entities.Customer;
import com.bharath.springdata.data.entities.CustomerId;
import com.bharath.springdata.data.repos.CustomerRepo;

@SpringBootTest
class CompositekeyApplicationTests {
	
	@Autowired
	CustomerRepo repo;

	/*
	@Test
	void testSaveCustomerUsingId() {

		Customer customer = new Customer();		
		customer.setId(1234);
		customer.setEmail("test@test.com");
		customer.setName("test");
		repo.save(customer);
	}
	*/

	@Test
	void testSaveCustomerUsingEmbeddedId() {

		Customer customer = new Customer();
		CustomerId id = new CustomerId();
		
		id.setId(1234);
		id.setEmail("test@test.com");
		customer.setId(id);
		customer.setName("test");
		repo.save(customer);
	}
}