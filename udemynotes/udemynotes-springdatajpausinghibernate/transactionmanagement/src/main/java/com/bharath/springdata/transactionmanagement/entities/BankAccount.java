package com.bharath.springdata.transactionmanagement.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BankAccount {
	
	@Id
	private int accno;
	private String firstname;
	private String lastname;
	private int bal;

	public int getAccno() {
		return accno;
	}

	public void setAccno(int accno) {
		this.accno = accno;
	}

	public int getBal() {
		return bal;
	}

	public void setBal(int bal) {
		this.bal = bal;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
	public String toString() {
		return "BankAccount [accno=" + accno + ", firstname=" + firstname + ", lastname=" + lastname + ", bal=" + bal
				+ "]";
	}
}

/*
1. SQL script:
	use mydb;

	create table bankaccount(
		accno int,
		lastname varchar(25),
		firstname varchar(25),
		bal int
	);

	insert into bankaccount values(1,'obama','barack',5000);
	insert into bankaccount values(2,'donald','trump',4000);
	select * from bankaccount;
	drop table bankaccount;
*/