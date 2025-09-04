package com.bharath.springdata.componentmapping.entities;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Employee {

	@Id
	private int id;
	private String name;
	@Embedded
	private Address address;

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}

/*
1. SQL:
	use mydb;

	create table employee(
		id int,
		name varchar(20),
		streetaddress varchar(30),
		city varchar(20),
		state varchar(20),
		zipcode varchar(20),
		country varchar(20)
	);

	select * from employee;

2. Sample data:
	employee:
	-------------------------------------------------------------------------
	id		name		streetaddress		city	state	zipcode	  country
	-------------------------------------------------------------------------
	123		Bharath		Spicewood Springs	Austin	TEXAS	78750	  USA
	-------------------------------------------------------------------------
*/