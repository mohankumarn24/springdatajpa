package com.bharath.springdata.product.repos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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

	// call stored procedures
	@Query(value="CALL GetAllProducts", nativeQuery = true)
	List<Product> findAllProducts();
	
	@Query(value="CALL GetAllProductsByPrice (:price_in)", nativeQuery = true)
	List<Product> findAllProductsByPrice(double price_in);
	
	@Query(value="CALL GetAllProductsByPrice (:price_in)", nativeQuery = true)
	int findAllProductsCountByPrice(double price_in);

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

/*
	create stored procedures:

	select * from product;

	DELIMITER //

	CREATE PROCEDURE GetAllProducts()
	BEGIN
		SELECT *  FROM product;
	END //

	DELIMITER //

	CREATE PROCEDURE GetAllProductsByPrice(IN price_in decimal)
	BEGIN
		SELECT *  FROM product where price>price_in;
	END //

	DELIMITER //

	CREATE PROCEDURE GetAllProductsCountByPrice(IN price_in decimal)
	BEGIN
		SELECT count(*)  FROM product where price>price_in;
	END //

	drop procedure GetAllProducts;
	drop procedure GetAllProductsByPrice;
	drop procedure GetAllProductsCountByPrice;
*/