package com.bharath.springdata.idgenerators.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.GenericGenerator;

// https://chatgpt.com/share/68b73cb4-4c20-8004-8126-41d0922cc656
@Entity
public class Employee {

	// @Id
	// @GeneratedValue(strategy = GenerationType.AUTO)

	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)

	// @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_gen")
    // @SequenceGenerator(name = "employee_gen", sequenceName = "emp_id_seq", allocationSize = 1)
	// CREATE SEQUENCE emp_id_seq START 1; -- create sequence in postgreSQL
	
	// @Id
	// @GeneratedValue(strategy = GenerationType.TABLE, generator="employee_gen")
	// @TableGenerator(name = "employee_gen", table = "id_generator", pkColumnName = "gen_name", valueColumnName = "gen_val", allocationSize=100)
	/*
	@TableGenerator(
        name = "employee_gen",      		// generator name
        table = "id_generator",     		// table used for sequence
        pkColumnName = "gen_name",  		// column holding sequence name
        valueColumnName = "gen_val",		// column holding next value
        pkColumnValue = "emp_id_seq",  		// sequence name for this entity
		initialValue = 1,   				// <--- starting value. Some JPA providers (like Hibernate) allow initialValue in @TableGenerator. 
											// Hibernate will automatically insert the initial row (gen_name='user_id', gen_val=1) if it doesn’t exist.
        allocationSize = 100          		// increment step
    )
	*/

	@GenericGenerator(name="emp_id", strategy="com.bharath.springdata.idgenerators.CustomRandomIDGenerator")
	@GeneratedValue(generator="emp_id")
	@Id
	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

/*
// @TableGenerator(name = "employee_gen", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_val",allocationSize=100)
// @GeneratedValue(strategy = GenerationType.TABLE,generator="employee_gen")
// table used to generate and store the sequence


--------- SQL SCRIPT ---------
-- Create the table used by TABLE generator
CREATE TABLE id_generator (
    gen_name VARCHAR(50) PRIMARY KEY,  -- sequence name (e.g., user_id)
    gen_val BIGINT NOT NULL            -- next value to use
);

-- Insert initial value for the 'user_id' sequence (mandatory)
INSERT INTO id_generator (gen_name, gen_val) VALUES ('user_id', 1);
--------- SQL SCRIPT ---------

gen_name → Holds the sequence name (user_id in our example). Acts as the primary key.
gen_val → Holds the next ID value to be assigned.
When JPA inserts a new Employee, it reads gen_val, uses it as id, then increments gen_val in this table as specified by allocationSize (1 (initial), 100, 200, 300, 400...)

Sample values:
---------------         --------------
gen_name   				gen_val
---------------         --------------
user_id_seq				1
order_id_seq			101
emp_id_seq				1
---------------         --------------

When you insert a new Employee, JPA:
 - Looks up emp_id_seq in id_generator.
 - Uses the gen_val as the primary key.
 - Increments gen_val by 1. (it depends on allocationSize value) 
*/

/*
	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(strategy = GenerationType.TABLE)
*/