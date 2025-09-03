package com.bharath.springdata.associations.onetomany.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;
	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<PhoneNumber> numbers;  // if cascade is not specified, only customer table entry will be added but not phone_number
	// CascadeType.ALL: whatever operation we do on Customer table, same must be cascaded/propogated to phone_number table as well
	// Default is LAZY. we must add @Transactional for LAZY loading

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<PhoneNumber> getNumbers() {
		return numbers;
	}

	public void setNumbers(Set<PhoneNumber> numbers) {
		this.numbers = numbers;
	}

	// added to handle foreign key value being null in phone_number table
	public void addPhoneNumber(PhoneNumber number) {
		if (number != null) {
			if (numbers == null) {
				numbers = new HashSet<>();
			}
			number.setCustomer(this);			// if this is not added, foreign key column value in phone number table will be null
			numbers.add(number);
		}

	}

}

/*
1. SQL Scripts:
	use mydb;

	create table customer(
		id int PRIMARY KEY AUTO_INCREMENT,
		name varchar(20)
	);

	create table phone_number(
		id int PIRMARY KEY AUTO_INCREMENT,
		customer_id int,
		number varchar(20),
		type varchar(20),
		FOREIGN KEY (customer_id) REFERENCES customer(id)
	)

	select * from customer

	select * from phone_number

2. Sample data:
	customer:
	--------
	id 	name
	--------
	1	John
	--------

	phone_number:
	------------------------------------
	id	customer_id		number		type
	------------------------------------
	1		1			1234567890	cell
	2		1			0987654321	home
	------------------------------------

*/

/*
CascadeType:
PERSIST	: insert operation will be cascaded from parent to child
MERGE	: insert/update operation will be cascaded from parent to child
REMOVE	: delete
REFRESH	: 
DETACH	:
ALL		: all
*/