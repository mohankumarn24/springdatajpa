package com.bharath.springdata.idgenerators.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Employee {

	//@TableGenerator(name = "employee_gen", table = "id_gen", pkColumnName = "gen_name", valueColumnName = "gen_val", allocationSize=100)
	@GenericGenerator(name="emp_id",strategy="com.bharath.springdata.idgenerators.CustomRandomIDGenerator")
	@GeneratedValue(generator="emp_id")
	@Id
	//@GeneratedValue(strategy = GenerationType.TABLE, generator="employee_gen")
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

-- Create the table used by TABLE generator
CREATE TABLE id_generator (
    gen_name VARCHAR(50) PRIMARY KEY,  -- sequence name (e.g., user_id)
    gen_val BIGINT NOT NULL            -- next value to use
);

-- Insert initial value for the 'user_id' sequence (mandatory)
INSERT INTO id_generator (gen_name, gen_val) VALUES ('user_id', 1);

gen_name → Holds the sequence name (user_id in our example). Acts as the primary key.
gen_val → Holds the next ID value to be assigned.
When JPA inserts a new User, it reads gen_val, uses it as id, then increments gen_val in this table as specified by allocationSize (100 (initial), 200, 300, 400...)

Sample values:
sequence_name   		next_val
user_id_seq				1
order_id_seq			101
employee_id_seq			1

When you insert a new Employee, JPA:
 - Looks up employee_id_seq in id_generator.
 - Uses the next_val as the primary key.
 - Increments next_val by 1. (it depends on allocationSize value)

*/

/*
	@GeneratedValue(strategy = GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(strategy = GenerationType.TABLE)
*/