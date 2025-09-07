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
		id int PRIMARY KEY AUTO_INCREMENT,
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


/*
-- Clean up old tables if they exist
DROP TABLE IF EXISTS phone_number;
DROP TABLE IF EXISTS customer;

-- =====================
-- CREATE TABLES
-- =====================
CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20)
);

CREATE TABLE phone_number (
    id SERIAL PRIMARY KEY,
    customer_id INT,
    number VARCHAR(20),
    type VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customer(id) ON DELETE CASCADE
);

-- If you delete a customer, their numbers disappear automatically.

-- =====================
-- CREATE (Insert)
-- =====================
-- Insert customers
INSERT INTO customer (name)
VALUES ('Alice'),
       ('Bob');

-- Insert phone numbers for Alice (id = 1)
INSERT INTO phone_number (customer_id, number, type)
VALUES (1, '123-456-7890', 'Mobile'),
       (1, '111-222-3333', 'Home');

-- Insert phone number for Bob (id = 2)
INSERT INTO phone_number (customer_id, number, type)
VALUES (2, '999-888-7777', 'Work');

-- =====================
-- READ (Select)
-- =====================
-- Get all customers with their phone numbers
SELECT c.id AS customer_id, c.name,
       p.id AS phone_id, p.number, p.type
FROM customer c
LEFT JOIN phone_number p ON c.id = p.customer_id
ORDER BY c.id, p.id;

-- Get phone numbers for a specific customer (Alice, id = 1)
SELECT p.id, p.number, p.type
FROM phone_number p
WHERE p.customer_id = 1;

-- =====================
-- UPDATE
-- =====================
-- Update customer name
UPDATE customer
SET name = 'Alice Cooper'
WHERE id = 1;

-- Update a phone number (change type)
UPDATE phone_number
SET type = 'Personal'
WHERE id = 1;

-- =====================
-- DELETE
-- =====================
-- Delete one phone number
DELETE FROM phone_number WHERE id = 2;

-- Delete a customer (this will also delete their phone numbers due to ON DELETE CASCADE)
DELETE FROM customer WHERE id = 1;

-- =====================
-- SUMMARY QUERY
-- =====================
-- Show each customer with the count of their phone numbers
SELECT c.id AS customer_id,
       c.name AS customer_name,
       COUNT(p.id) AS phone_count
FROM customer c
LEFT JOIN phone_number p ON c.id = p.customer_id
GROUP BY c.id, c.name
ORDER BY phone_count DESC;

*/