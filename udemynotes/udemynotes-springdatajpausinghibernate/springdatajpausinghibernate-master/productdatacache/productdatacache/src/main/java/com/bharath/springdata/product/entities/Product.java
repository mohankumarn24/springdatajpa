package com.bharath.springdata.product.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY) // Step 4 & 5: Make entities cacheable and make the entity serializable
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private int id;
	private String name;
	@Column(name = "description")
	private String desc;
	private Double price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}

/*
Level 2 Cache:
- Objects are cached at Hibernate SessionFactory level (cached objects are shared across different Hibernate sessions)
- additional configuration required
- Multiple sessions will share common cache

Steps:
Step 1: Add ehcache dependency
 	<dependency>
		<groupId>org.hibernate</groupId>
		<artifactId>hibernate-ehcache</artifactId>
	</dependency>
 
Step 2: Add L2 Cache properties
	spring.jpa.properties.hibernate.cache.use_second_level_cache=true
	spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.ehcache.EhCacheRegionFactory
	spring.cache.ehcache.config=classpath:ehcache.xml
	spring.jpa.properties.javax.persistence.sharedCache.mode=ALL
	
Step 3: Add ehcache.xml
	<ehcache>
		<diskStore path="java.io.tmpdir"/>  --> objects temporarily cached in this file
		<defaultCache 
			maxElementsInMemory="100"       --> max objects to cache
			eternal="false" 				--> don't cache forever
			timeToIdleSeconds="5" 			--> remove object if it is not accessed for 5 seconds
			timeToLiveSeconds="10" 			--> remove cached object permamently after 10 seconds
			overflowToDisk="true"			--> overflow from memory to disk in case of any memory issues
		/>
	</ehcache>
	
Step 4: Cache Concurrency Strategy
	- READ_ONLY				--> useful when application performs only read operations (create, update, delete not supported)
	- NONSTRICT_READ_WRITE	--> Provides eventual consistency: 
										Cache will be updated only when a particular transaction completely commits data to the database. 
										If any other transaction reads cache before commit, it will get stale data
	- READ_WRITE			--> Provides total consistency. Uses soft locks. If there is lock on cache object, then another transaction reads latest data directly from db instead of cache
	- TRANSACTIONAL			--> Used in XA/distributed transactions (A change in a cache which could be a commit or a rollback will happen across databases and those changes will impact the cache as well)
	
Step 5: Make entities cacheable and make the entity serializable
	import org.hibernate.annotations.Cache;
	import org.hibernate.annotations.CacheConcurrencyStrategy;
	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
 */