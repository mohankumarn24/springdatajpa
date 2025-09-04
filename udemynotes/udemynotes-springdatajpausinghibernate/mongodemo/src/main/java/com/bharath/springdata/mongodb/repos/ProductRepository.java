package com.bharath.springdata.mongodb.repos;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bharath.springdata.mongodb.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

}
