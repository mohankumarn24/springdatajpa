package com.bharath.springdata.hibernateinheritance.entities;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

// @Entity
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @DiscriminatorColumn(name="pmode", discriminatorType=DiscriminatorType.STRING)

// @Entity
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Payment {

	@Id
	private int id;
	private double amount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}


/*
1. SINGLE_TABLE: Maintains single table. We need extra discriminator column PMODE to differentiate between Card or a Cheque
	SQL scripts:
	use mydb;

	create table payment(
		id int PRIMARY KEY,
		pmode varchar(2),
		amount decimal(8,3) ,
		cardnumber varchar(20),
		checknumber varchar(20)
	);

	select * from payment;
	
	Annotations in parent class (Payment):
	@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
	@DiscriminatorColumn(name="pmode", discriminatorType=DiscriminatorType.STRING)
	
	Annotations in child class (CreditCard):
	@Entity
	@DiscriminatorValue("cc")

	Annotations in Child class (Check):
	@Entity
	@DiscriminatorValue("ch")
	
	Test methods:
	@Test
	public void createPayment() {
		CreditCard cc = new CreditCard();
		cc.setId(123);
		cc.setAmount(1000);
		cc.setCardnumber("1234567890");
		repository.save(cc);
	}

	@Test
	public void createCheckPayment() {
		Check ch = new Check();
		ch.setId(124);
		ch.setAmount(1000);
		ch.setChecknumber("1234567890");
		repository.save(ch);
	}

	Sample Data:
	-------------------------------------------------
	id		pmode	amount	cardnumber	 checknumber
	-------------------------------------------------
	123		cc		1000	1234567890	 null
	123		cc		1000	null		 1234567890	
	-------------------------------------------------
 */

 /*
2. TABLE_PER_CLASS: Separate tables are maintanied. Improves performance but still not recommended as it maintains duplicate values in two tables (ie., id and amount)
	SQL scripts:
	use mydb;

	create table card(
		id int PRIMARY KEY,
		amount decimal(8,3),
		cardnumber varchar(20)
	);

	create table bankcheck(
		id int PRIMARY KEY,
		amount decimal(8,3),
		checknumber varchar(20)
	);

	
	Annotations in parent class (Payment):
	@Entity
	@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
	
	Annotations in child class (CreditCard):
	@Entity
	@Table(name="card") // optional
	
	Annotations in Child class (Check):
	@Entity
	@Table(name="bankcheck") // added as check is reserved keyword in SQL

	Test methods:
	@Test
	public void createPayment() {
		CreditCard cc = new CreditCard();
		cc.setId(123);
		cc.setAmount(1000);
		cc.setCardnumber("1234567890");
		repository.save(cc);
	}

	@Test
	public void createCheckPayment() {
		Check ch = new Check();
		ch.setId(124);
		ch.setAmount(1000);
		ch.setChecknumber("1234567890");
		repository.save(ch);
	}

	Sample Data:
	card:
	------------------------------
	id 		amount 		cardnumber
	------------------------------
	123		1000		1234567890
	------------------------------

	bankcheck:
	-------------------------------
	id 		amount 		checknumber
	-------------------------------
	124		1000		1234567890
	-------------------------------
  */

/*
3. JOINED: Every class has its own table. So, discriminator column is not needed
	SQL scripts:
	use mydb;

	create table payment(
		id int PRIMARY KEY,
		amount decimal(8,3)
		id INT NOT NULL AUTO_INCREMENT
	);

	create table card(
		id int,
		cardnumber varchar(20),
		FOREIGN KEY (id) REFERENCES payment(id)
	);

	create table bankcheck(
		id int ,
		checknumber varchar(20),
		FOREIGN KEY (id) REFERENCES payment(id)
	);
	
	Annotations in parent class (Payment):
	@Entity
	@Inheritance(strategy = InheritanceType.JOINED)	

	Annotations in child class (CreditCard):
	@Entity
	@Table(name="card")
	@PrimaryKeyJoinColumn(name="id")

	Annotations in Child class (Check):
	@Entity
	@Table(name = "bankcheck")
	@PrimaryKeyJoinColumn(name = "id")

	Test methods:
	@Test
	public void createPayment() {
		CreditCard cc = new CreditCard();
		cc.setId(123);
		cc.setAmount(1000);
		cc.setCardnumber("1234567890");
		repository.save(cc);
	}

	@Test
	public void createCheckPayment() {
		Check ch = new Check();
		ch.setId(124);
		ch.setAmount(1000);
		ch.setChecknumber("1234567890");
		repository.save(ch);
	}

	Sample Data:
	payment:
	--------------
	id 		amount
	--------------
	123		1000
	124		1000
	--------------

	card:
	------------------
	id 		cardnumber
	------------------
	123		1234567890
	------------------

	bankcheck:
	-------------------
	id 		checknumber
	-------------------
	124		1234567890
	-------------------
 */