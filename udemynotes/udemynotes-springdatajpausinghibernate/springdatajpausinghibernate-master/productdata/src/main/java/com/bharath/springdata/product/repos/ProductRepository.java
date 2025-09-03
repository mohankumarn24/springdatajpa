package com.bharath.springdata.product.repos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bharath.springdata.product.entities.Product;
import java.lang.String;
import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	// spring data finder methods
	// define abstract method and query is auto-generated
	// https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
	List<Product> findByName(String name);

	List<Product> findByNameAndDesc(String name, String desc);

	List<Product> findByPriceGreaterThan(Double price);

	List<Product> findByDescContains(String desc);

	List<Product> findByPriceBetween(Double price1, Double price2);

	List<Product> findByDescLike(String desc);

	List<Product> findByIdIn(List<Integer> ids,Pageable pageable);

}

/*
	interface StudentRepository extends CrudRepository<Student, Long>
	- This will generate default methods:  save(), findById(), findAll(), deleteById()
 */

 /*
  * From Spring Boot 3.0.2 and onwards we need to extend both CrudReposity and PagingAndSortingRepository for Pagination support
  
  public interface ProductRepository extends CrudRepository<Product, Integer> ,PagingAndSortingRepository<Product, Integer> {

  	...
   }
  */