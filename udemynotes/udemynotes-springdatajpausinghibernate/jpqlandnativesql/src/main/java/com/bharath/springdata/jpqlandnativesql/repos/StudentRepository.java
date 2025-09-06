package com.bharath.springdata.jpqlandnativesql.repos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.bharath.springdata.jpqlandnativesql.entities.Student;

// JPQL: Java Persistence Query Language
public interface StudentRepository extends CrudRepository<Student, Long> {

	@Query("from Student")
	// @Query("select st from Student st") // same as above (preferred approach)
	List<Student> findAllStudents(Pageable pageable);

	@Query("select st.firstName, st.lastName from Student st") // referring to Student Object. So, using field names st.firstName and st.lastName in JPQL. Case-sensitive
	List<Object[]> findAllStudentsPartialData();

	@Query("from Student where firstName=:firstName")
	// @Query("select st from Student st where st.firstName=:firstName") // same as above (preferred approach)
	List<Student> findAllStudentsByFirstName(@Param("firstName") String firstName); // :firstName

	@Query("from Student where score>:min and score<:max")
	List<Student> findStudentsForGivenScores(@Param("min") int min, @Param("max") int max);

	@Modifying // use @Modifying in repo layer and @Transactional in service layer for update, delete operations
	@Query("delete from Student where firstName=:firstName")
	void deleteStudentsByFirstName(@Param("firstName") String firstName); // :firstName

	@Query(value = "select * from student", nativeQuery = true)
	List<Student> findAllStudentNQ();

	@Query(value = "select * from student where fname=:firstName", nativeQuery = true) // native query. So, using actual column names fname in qeury
	List<Student> findByFirstNQ(@Param("firstName")String firstName);

	/* 
	------------------------------------------------------------------------------------------------------------------
	// todo: verify these commented methods
    // CREATE (Incorrect approach)
    @Modifying
    @Transactional
    @Query("INSERT INTO Student (name, age, email) VALUES (:name, :age, :email)")
    void saveStudent(@Param("name") String name, @Param("age") int age, @Param("email") String email);
	// One thing: JPQL does not support INSERT directly (only SELECT, UPDATE, DELETE). 
	// That means the saveStudent query above will actually not work in plain JPQL. 
	// Usually, save() from JpaRepository or using EntityManager.persist() is the way.

    // READ
    @Query("SELECT s FROM Student s WHERE s.id = :id")
    Student findStudentById(@Param("id") Long id);

    @Query("SELECT s FROM Student s")
    List<Student> findAllStudents();

    // UPDATE
    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.name = :name, s.age = :age, s.email = :email WHERE s.id = :id")
    int updateStudent(@Param("id") Long id, @Param("name") String name, @Param("age") int age, @Param("email") String email);

    // DELETE
    @Modifying
    @Transactional
    @Query("DELETE FROM Student s WHERE s.id = :id")
    int deleteStudent(@Param("id") Long id);

	------------------------------------------------------------------------------------------------------------------
    // --- CREATE ---
    // save(Employee emp) from JpaRepository already does INSERT

    // --- READ ---
    @Query("SELECT e FROM Employee e")
    List<Employee> findAllEmployees();

    @Query("SELECT e FROM Employee e WHERE e.id = :id")
    Employee findEmployeeById(@Param("id") Long id);

    // --- UPDATE ---
    @Modifying
    @Transactional
    @Query("UPDATE Employee e SET e.name = :name, e.department = :department, e.salary = :salary WHERE e.id = :id")
    int updateEmployee(@Param("id") Long id, @Param("name") String name, @Param("department") String department, @Param("salary") Double salary);

    // --- DELETE ---
	// Adding both @Modifying and @Transactional is mandatory for update and delete operations
	//  - Add both annotations in repository class or 
	//  - Add @Modifying in Repository layer and @Transactional in service layer (preferred)
    @Modifying
    @Transactional
    @Query("DELETE FROM Employee e WHERE e.id = :id")
    int deleteEmployeeById(@Param("id") Long id);	
	*/
}

/*
 use mydb;

create table student(
	id int PRIMARY KEY AUTO_INCREMENT,
	lname varchar(20),
	fname varchar(20),
	score int
);

select * from student;
*/