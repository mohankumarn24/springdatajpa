package com.bharath.springdata.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bharath.springdata.product.entities.Product;
import com.bharath.springdata.product.repos.ProductRepository;

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

	@Test
	public void testFindByIdsIn() {
		// Pageable pageable = new PageRequest(0, 2);
		Pageable pageable = PageRequest.of(0, 2);
		List<Product> products = repository.findByIdIn(Arrays.asList(1, 2, 3), pageable);
		products.forEach(p -> System.out.println(p.getName()));
	}

	@Test
	public void testFindAllPaging() {
		Pageable pageable = PageRequest.of(0, 2);
		Iterable<Product> results = repository.findAll(pageable);
		results.forEach(p -> System.out.println(p.getName()));

	}

	@Test
	public void testFindAllSorting() {
		repository.findAll(Sort.by(new Sort.Order(Direction.DESC, "name"), new Sort.Order(null, "price")))
				.forEach(p -> System.out.println(p.getName()));

		// repository.findAll(Sort.by("name", "price")).forEach(p ->
		// System.out.println(p.getName()));

	}

	@Test
	public void testFindAllPagingAndSorting() {
		Pageable pageable = PageRequest.of(0, 2, Direction.DESC, "name");
		repository.findAll(pageable).forEach(p -> System.out.println(p.getName()));

	}

	@Test
	@Transactional			// needed to enable caching
	public void testCaching() {
		Session session = entityManager.unwrap(Session.class);	// unwrap the JPA EntityManager to Hibernate’s native Session so you can directly manage cache behavior.
		Product product = repository.findById(1).get();	// Entity stored in both 1st-level and 2nd-level cache. SQL Generated
		repository.findById(1).get();					// no SQL (1st-level cache hit) (within the same session).
		session.evict(product);							// explicitly remove this Product instance from the first-level cache only, entity still lives in 2nd-level cache	
														// session.clear() -> clear entire 1st level cache
														// session.evict(product) -> clear only product element from 1st level cache
		// session.getSessionFactory().getCache().evictEntityData(Product.class);			// clear 2nd level cache for all Product objects
		// session.getSessionFactory().getCache().evictEntityData(Product.class, 1);		// clear 2nd level cache for Product with id 1
		repository.findById(1).get();					// no SQL → served from 2nd-level cache
														// Total = 1 SQL query generated


		/* 
		 * L1 CACHE: Data in cache will be updated/deleted automatically by Hibernate in case of update/delete operation
		 * 
		 * L1 cache:
		 *  - client 1 issues findById(1) -> session manager creates session1 -> create new L1 cache
		 *  - client 2 issues findById(1) -> session manager creates session2 -> create ANOTHER new L1 cache
		 *
		 * L2 cache:
		 *  - client 1 issues findById(1) -> session manager creates session1 -> create new common L2 cache
		 *  - client 2 issues findById(1) -> session manager creates session2 -> uses already created L2 cache
		 */
	}

}


/*
/*
 * ============================================================
 * ENTITY MANAGER - DETACH (JPA L1 CACHE CONTROL)
 * ============================================================
 *
 * Definition:
 * -----------
 * entityManager.detach(entity)
 *
 * - Removes a specific entity from the Persistence Context (L1 cache)
 * - Entity transitions from MANAGED -> DETACHED state
 *
 *
 * ============================================================
 * WHY DETACH?
 * ============================================================
 *
 * 1. Avoid dirty checking (prevent auto updates)
 * 2. Force fresh DB reads (bypass L1 cache)
 * 3. Reduce memory usage (large batch processing)
 *
 *
 * ============================================================
 * BASIC EXAMPLE
 * ============================================================
 *
 * @Transactional
 * public void example() {
 *
 *     User user = repo.findById(1L).get();
 *     // -> DB HIT
 *
 *     entityManager.detach(user);
 *
 *     user.setName("Mohan");
 *     // -> NOT tracked
 *     // -> NO update query will be executed
 *
 *     repo.findById(1L);
 *     // -> DB HIT again (entity not in L1 cache)
 * }
 *
 *
 * ============================================================
 * DIRTY CHECKING BEHAVIOR
 * ============================================================
 *
 * // Without detach
 *
 * @Transactional
 * public void updateUser() {
 *     User user = repo.findById(1L).get();
 *     user.setName("Mohan");
 *     // -> UPDATE query executed at transaction commit
 * }
 *
 *
 * // With detach
 *
 * @Transactional
 * public void updateUser() {
 *     User user = repo.findById(1L).get();
 *
 *     entityManager.detach(user);
 *
 *     user.setName("Mohan");
 *     // -> NO update query (detached entity)
 * }
 *
 *
 * ============================================================
 * REATTACHING ENTITY (MERGE)
 * ============================================================
 *
 * @Transactional
 * public void mergeExample() {
 *
 *     User user = repo.findById(1L).get();
 *
 *     entityManager.detach(user);
 *
 *     user.setName("Mohan");
 *
 *     entityManager.merge(user);
 *     // -> Copies state into managed entity
 *     // -> UPDATE will be executed at commit
 * }
 *
 *
 * ============================================================
 * DETACH VS CLEAR
 * ============================================================
 *
 * entityManager.detach(entity)
 * - Removes ONLY one entity
 *
 * entityManager.clear()
 * - Removes ALL entities from persistence context
 *
 *
 * ============================================================
 * REAL-WORLD USE CASE (BATCH PROCESSING)
 * ============================================================
 *
 * @Transactional
 * public void processBulk(List<User> users) {
 *
 *     int i = 0;
 *
 *     for (User user : users) {
 *
 *         user.setName("Updated");
 *
 *         if (i % 50 == 0) {
 *             entityManager.flush();  // push changes to DB
 *             entityManager.clear();  // clear L1 cache (avoid memory issues)
 *         }
 *
 *         i++;
 *     }
 * }
 *
 *
 * ============================================================
 * IMPORTANT NOTES
 * ============================================================
 *
 * - Detached entity is NOT tracked by JPA
 * - Changes are NOT persisted automatically
 * - Must use merge() to reattach
 * - Next fetch of same entity -> DB hit
 *
 *
 * ============================================================
 * INTERVIEW ONE-LINER
 * ============================================================
 *
 * "detach() removes an entity from the persistence context,
 * disabling dirty checking and forcing subsequent loads
 * to hit the database."
 *
 * ============================================================
 */