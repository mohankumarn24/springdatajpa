package com.bharath.springdata.mongodb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.bharath.springdata.mongodb.model.Product;
import com.bharath.springdata.mongodb.repos.ProductRepository;

@SpringBootTest
class MongodemoApplicationTests {

	@Autowired
	ProductRepository repo;
	
	@Test
	void testSave() {
		Product product = new Product();
		product.setName("Mac Book Pro");
		product.setPrice(2000f);
		Product savedProduct = repo.save(product);
		assertNotNull(savedProduct);
	}
	@Test
	public void testFindAll() {
		List<Product> products = repo.findAll();
		assertEquals(1,products.size());
	}
	
	@Test
	public void testDelete() {
		repo.deleteById("5d10bbddce9f75o4413573e");
		Optional<Product> product = repo.findById("5d10bbddce9f75o4413573e");
		assertEquals(Optional.empty(),product);
	}

}
