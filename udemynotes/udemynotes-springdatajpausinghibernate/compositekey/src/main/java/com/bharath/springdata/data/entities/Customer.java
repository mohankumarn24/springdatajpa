package com.bharath.springdata.data.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class Customer {
	
	@EmbeddedId
	private CustomerId id;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

/*
// use @IdClass or combination of @Embeddable & @EmbeddedId
// @IdClass
@Entity
@IdClass(CustomerId.class)
public class Customer {
	
	@Id
	private int id;
	@Id
	private String email;
	private String name;
	
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public CustomerId getId() {return id;}
	public void setId(CustomerId id) {this.id = id;}
}

public class CustomerId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String email;

	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}
}
	
// Note the method signature
public interface CustomerRepo extends JpaRepository<Customer, CustomerId> {

}
/*
	use mydb

	CREATE TABLE customer (
		id int NOT NULL ,
		email varchar(255) NOT NULL,
		name varchar(255) NOT NULL,
		PRIMARY KEY (id, email)
	);

	select * from customer;
	drop table customer;
*/