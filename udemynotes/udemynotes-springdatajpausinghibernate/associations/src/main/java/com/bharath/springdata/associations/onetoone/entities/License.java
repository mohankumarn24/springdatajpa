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





/*
 1 Person has 1 Passport:
- Usually, cascade is set on the owner side that manages the relationship.
 - In the example, Person “owns” the cascade because:
 - Deleting a Person deletes the associated Passport.
 - In Passport (@OneToOne) you normally do NOT set cascade, unless you want operations on the child (Passport) to propagate to the parent (Person).
 - This is rarely needed.
 - ✅ Rule of thumb: Cascade flows from parent → child in any association ()


| Cascade Type | Meaning / Effect                                                         | When to Use                                                                                                                                  |
| ------------ | ------------------------------------------------------------------------ | -------------------------------------------------------------------------------------------------------------------------------------------- |
| **ALL**      | Applies all operations (PERSIST, MERGE, REMOVE, REFRESH, DETACH)         | When you want **everything on the parent to affect the child**. Common in One-to-Many or One-to-One where child lifecycle depends on parent. |
| **PERSIST**  | When parent is saved, child is also saved                                | Use when creating a parent also needs to save new child entities automatically.                                                              |
| **MERGE**    | When parent is merged/updated, child is also merged                      | Use when updating a detached parent and you want changes propagated to child entities.                                                       |
| **REMOVE**   | When parent is deleted, child is also deleted                            | Only when deleting a parent should delete its children (One-to-Many, One-to-One). Not usually for Many-to-Many.                              |
| **REFRESH**  | When parent is refreshed from DB, child is refreshed too                 | Rarely used; ensures in-memory child state matches DB.                                                                                       |
| **DETACH**   | When parent is detached from persistence context, child is also detached | Rarely used; useful if you’re managing detached entities and don’t want the child still attached to persistence context.                     |


| Association  | Recommended Cascade              | orphanRemoval | Note                                        |
| ------------ | -------------------------------- | ------------- | ------------------------------------------- |
| One-to-One   | `CascadeType.ALL`                | ✅             | Child tied to parent’s lifecycle            |
| One-to-Many  | `CascadeType.ALL` on parent side | ✅             | Parent manages children                     |
| Many-to-One  | None                             | ❌             | Child shouldn’t cascade to parent           |
| Many-to-Many | `{PERSIST, MERGE}`               | ❌             | Avoid REMOVE (would delete linked entities) |

@OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
private Passport passport;


@OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Book> books = new ArrayList<>();
@ManyToOne
@JoinColumn(name = "author_id")
private Author author;


@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinTable(
    name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private Set<Course> courses = new HashSet<>();


The guiding principle:
	-	If the child’s lifecycle fully depends on the parent → cascade from parent.
	-	If the entities are more independent (many-to-many, many-to-one) → keep cascade minimal.
 */