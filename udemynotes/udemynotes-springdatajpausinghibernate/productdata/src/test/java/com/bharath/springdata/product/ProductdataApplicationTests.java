package com.bharath.springdata.product;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.springdata.product.entities.Product;
import com.bharath.springdata.product.repos.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductdataApplicationTests {

	@Autowired
	ProductRepository repository;

	@Autowired
	EntityManager entityManager;

	@Test
	public void contextLoads() {
	}

	@Test
	public void testCreate() {
		Product product = new Product();
		product.setId(1);
		product.setName("Iphone");
		product.setDesc("Awesome");
		product.setPrice(1000d);

		repository.save(product);
	}

	@Test
	public void testRead() {
		Product product = repository.findById(1).get();
		assertNotNull(product);
		assertEquals("Iphone", product.getName());
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + product.getDesc());
	}

	@Test
	public void testUpdate() {
		Product product = repository.findById(1).get();
		product.setPrice(1200d);
		repository.save(product);

	}

	@Test
	public void testDelete() {
		if (repository.existsById(1)) {
			System.out.println("Deleting a product");
			repository.deleteById(1);
		}
	}

	@Test
	public void testCount() {
		System.out.println("Total Records===============>>>>>>>>>>>>>>>" + repository.count());

	}

	@Test
	public void testFindByName() {
		List<Product> products = repository.findByName("IWatch");
		products.forEach(p -> System.out.println(p.getPrice()));

		List<Product> products1 = repository.findByName("IWatch");
		products1.forEach(p -> System.out.println(p.getPrice()));
	}

	@Test
	public void testFindByNameAndDesc() {
		List<Product> products = repository.findByNameAndDesc("TV", "From Samsung Inc");
		products.forEach(p -> System.out.println(p.getPrice()));
	}

	@Test
	public void testFindByPriceGreaterThan() {
		List<Product> products = repository.findByPriceGreaterThan(1000d);
		products.forEach(p -> System.out.println(p.getName()));
	}

	@Test
	public void testFindByDescContains() {
		List<Product> products = repository.findByDescContains("Apple");
		products.forEach(p -> System.out.println(p.getName()));
	}

	@Test
	public void testFindByPriceBetween() {
		List<Product> products = repository.findByPriceBetween(500d, 2500d);
		products.forEach(p -> System.out.println(p.getName()));
	}

	@Test
	public void testFindByDescLike() {
		List<Product> products = repository.findByDescLike("%LG%");
		products.forEach(p -> System.out.println(p.getName()));
	}

	// test pagination on custom finder method: List<Product> findByIdIn(List<Integer> ids,Pageable pageable);
	@Test
	public void testFindByIdsIn() {
		// Pageable pageable = new PageRequest(0, 2);
		Pageable pageable = PageRequest.of(0, 2);
		List<Product> products = repository.findByIdIn(Arrays.asList(1, 2, 3), pageable);
		products.forEach(p -> System.out.println(p.getName()));
	}

	// pagination
	@Test
	public void testFindAllPaging() {
		Pageable pageable = PageRequest.of(0, 2); // get 2 records from first page
		Iterable<Product> results = repository.findAll(pageable);
		// Page<Product> results = repository.findAll(pageable);
		results.forEach(p -> System.out.println(p.getName()));

	}

	// sorting
	@Test
	public void testFindAllSorting() {
		// sort by multiple properties
		repository.findAll(Sort.by(new Sort.Order(Direction.DESC, "name"), new Sort.Order(null, "price")))
				.forEach(p -> System.out.println(p.getName()));

		// sort by single property
		// repository.findAll(Sort.by("name", "price")).forEach(p -> System.out.println(p.getName()));

	}

	// pagination and sorting
	@Test
	public void testFindAllPagingAndSorting() {
		Pageable pageable = PageRequest.of(0, 2, Direction.DESC, "name");
		repository.findAll(pageable).forEach(p -> System.out.println(p.getName()));

		// Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending().and(Sort.by("email").descending()));

	}

	// Level 1 Cache
	/*
	 * Level 1 Cache:
	 * - Objects are cached at Hibernate Session level
	 * - Enabled by default
	 * - Session and SessionFactory are lower level Hiberate objects which are internally used by Hibernate
	 * - L1 Cache: Each session will have its own cache
	 * 
	 * Level 2 Cache:
	 * - Objects are cached at Hibernate SessionFactory level (cached objects are shared across different Hibernate sessions)
	 * - additional configuration required
	 * - Multiple sessions will share common cache
	 */
	@Test
	@Transactional 	// needed to enable L1 Cache
	public void testCaching() {

		/* without cache evict. 1 select query generated
		repository.findById(1); // reads value from db
		repository.findById(1); // reads value from L1 Cache
		*/

		// with cache evict. 2 select queries generated
		// entityManager is needed for cache evict
		Session session = entityManager.unwrap(Session.class);
		Product product = repository.findById(1).get();
		repository.findById(1).get();
		session.evict(product);
		repository.findById(1).get();
	}

	// test stored procedures
	@Test
	public void testFindAllProducts() {
		System.out.println(repository.findAllProducts());		
	}
	
	@Test
	public void testFindAllProductsByPrice() {
		System.out.println(repository.findAllProductsByPrice(800));		
	}
	
	@Test
	public void testFindAllProductsCountByPrice() {
		System.out.println(repository.findAllProductsCountByPrice(800));		
	}	
}
