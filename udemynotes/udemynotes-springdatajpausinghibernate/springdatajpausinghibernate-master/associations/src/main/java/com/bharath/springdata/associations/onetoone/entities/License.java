package com.bharath.springdata.associations.onetoone.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class License {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String type;
	@Temporal(TemporalType.DATE)
	private Date validFrom;
	@Temporal(TemporalType.DATE)
	private Date validTo;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="person_id")
	private Person person;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}


/*
1. Two types of OneToOne relationship: Primary Key Association, Foreign Key Association
	a. Primary Key Association (Bidirectional OneToOne relationship). License is always associated with Person and vice versa
		- Primary key is shared
		- Primary key in License table will also be Primary Key in Person table
		License table:					Person table:
		-----------------------------------------
		PK_column						PK_column
		-----------------------------------------
		1								1
		2								2
		-----------------------------------------

	b. Foreign Key Association (Unidirectional OneToOne relationship). License is always associated with Person. But reverse is not true
		License table:					Person table:
		-----------------------------------------
		PK_column	FK_column			PK_column
		-----------------------------------------
		10			1					1
		20			2					2
		-----------------------------------------

2. SQL Scripts:
	use mydb

	create table person(
		id int PRIMARY KEY AUTO_INCREMENT,
		first_name varchar(20),
		last_name varchar(20),
		age int
	);

	create table license(
		id int PRIMARY KEY AUTO_INCREMENT,
		type varchar(20),
		valid_from date,
		valid_to date,
		person_id int,
		FOREIGN KEY (person_id) REFERENCES person(id)
	);

	select * from person;

	select * from license;

3. Sample data:
	license:
	-------------------------------
	id	first_name	last_name	age
	-------------------------------
	1	John		Clinton		35
	-------------------------------

	person:
	---------------------------------------------------
	id	type	valid_from		valid_to	  person_id
	---------------------------------------------------
	1	CAR		2017-10-23		2017-10-23	  1	
	---------------------------------------------------
*/