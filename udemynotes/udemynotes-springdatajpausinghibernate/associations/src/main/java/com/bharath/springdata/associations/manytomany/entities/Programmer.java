package com.bharath.springdata.associations.manytomany.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Programmer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String name;
	@Column(name = "salary")
	private int sal;
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "programmers_projects", 
		joinColumns = @JoinColumn(name = "programmer_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"))
	private Set<Project> projects;

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

	public int getSal() {
		return sal;
	}

	public void setSal(int sal) {
		this.sal = sal;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	@Override
	public String toString() {
		return "Programmer [id=" + id + ", name=" + name + ", sal=" + sal + "]";
	}

}
/*
1. SQL Scripts:
	use mydb;

	create table programmer(
		id int PRIMARY KEY AUTO_INCREMENT,
		name varchar(20),
		salary int
	);

	create table project(
		id int PRIMARY KEY AUTO_INCREMENT,
		name varchar(20)
	);

	create table programmers_projects(
		programmer_id int,
		project_id int,
		FOREIGN KEY (programmer_id) REFERENCES programmer(id),
		FOREIGN KEY (project_id) REFERENCES project(id)
	);

	select * from programmer;
	select * from project;
	select * from programmers_projects;

2. Sample Data:
	programmer:
	------------------
	id 	name 	salary
	------------------
	1	John	1000
	------------------

	project:
	---------------------
	id 	name
	---------------------
	1	Hibernate Project
	---------------------

	programmers_projects
	--------------------------
	programmer_id	project_id
	--------------------------
	1				1
	--------------------------
*/

/*
-- =====================
-- CLEANUP
-- =====================
DROP TABLE IF EXISTS programmers_projects;
DROP TABLE IF EXISTS programmer;
DROP TABLE IF EXISTS project;

-- =====================
-- CREATE TABLES
-- =====================
CREATE TABLE programmer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    salary INT
);

CREATE TABLE project (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20)
);

CREATE TABLE programmers_projects (
    programmer_id INT,
    project_id INT,
    PRIMARY KEY (programmer_id, project_id), -- prevent duplicates
    FOREIGN KEY (programmer_id) REFERENCES programmer(id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE
);

-- =====================
-- CREATE (Insert)
-- =====================
-- Insert programmers
INSERT INTO programmer (name, salary)
VALUES ('Alice', 80000),
       ('Bob', 95000),
       ('Charlie', 70000);

-- Insert projects
INSERT INTO project (name)
VALUES ('Alpha'),
       ('Beta'),
       ('Gamma');

-- Assign programmers to projects
INSERT INTO programmers_projects (programmer_id, project_id)
VALUES (1, 1),  -- Alice -> Alpha
       (1, 2),  -- Alice -> Beta
       (2, 2),  -- Bob -> Beta
       (3, 1),  -- Charlie -> Alpha
       (3, 3);  -- Charlie -> Gamma

-- =====================
-- READ (Select)
-- =====================
-- Get all programmers with their projects
SELECT pr.id AS programmer_id, pr.name AS programmer_name, pr.salary,
       pj.id AS project_id, pj.name AS project_name
FROM programmer pr
INNER JOIN programmers_projects pp ON pr.id = pp.programmer_id
INNER JOIN project pj ON pj.id = pp.project_id
ORDER BY pr.id, pj.id;

-- Get all projects with their programmers
SELECT pj.id AS project_id, pj.name AS project_name,
       pr.id AS programmer_id, pr.name AS programmer_name
FROM project pj
INNER JOIN programmers_projects pp ON pj.id = pp.project_id
INNER JOIN programmer pr ON pr.id = pp.programmer_id
ORDER BY pj.id, pr.id;

-- Get projects for a specific programmer (Alice, id = 1)
SELECT pj.id, pj.name
FROM project pj
INNER JOIN programmers_projects pp ON pj.id = pp.project_id
WHERE pp.programmer_id = 1;

-- =====================
-- UPDATE
-- =====================
-- Update programmer salary
UPDATE programmer
SET salary = 85000
WHERE id = 1;

-- Update project name
UPDATE project
SET name = 'Beta-Revamp'
WHERE id = 2;

-- Change a programmer’s project (move Bob from Beta to Gamma)
DELETE FROM programmers_projects
WHERE programmer_id = 2 AND project_id = 2;

INSERT INTO programmers_projects (programmer_id, project_id)
VALUES (2, 3);

-- =====================
-- DELETE
-- =====================
-- Remove a programmer from a project
DELETE FROM programmers_projects
WHERE programmer_id = 3 AND project_id = 1;

-- Delete a project (this will also remove associations)
DELETE FROM project WHERE id = 1;

-- Delete a programmer (this will also remove associations)
DELETE FROM programmer WHERE id = 2;

-- =====================
-- SUMMARY QUERIES
-- =====================

-- How many projects each programmer is assigned to
SELECT pr.id AS programmer_id,
       pr.name AS programmer_name,
       COUNT(pp.project_id) AS project_count
FROM programmer pr
LEFT JOIN programmers_projects pp ON pr.id = pp.programmer_id
GROUP BY pr.id, pr.name
ORDER BY project_count DESC;

-- How many programmers are on each project
SELECT pj.id AS project_id,
       pj.name AS project_name,
       COUNT(pp.programmer_id) AS programmer_count
FROM project pj
LEFT JOIN programmers_projects pp ON pj.id = pp.project_id
GROUP BY pj.id, pj.name
ORDER BY programmer_count DESC;

*/