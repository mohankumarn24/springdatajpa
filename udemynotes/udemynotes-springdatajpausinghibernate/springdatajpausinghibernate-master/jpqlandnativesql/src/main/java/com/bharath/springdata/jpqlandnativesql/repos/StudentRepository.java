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
	List<Student> findAllStudents(Pageable pageable);

	@Query("select st.firstName, st.lastName from Student st") // referring to Student Object. So, using field names st.firstName and st.lastName in JPQL. Case-sensitive
	List<Object[]> findAllStudentsPartialData();

	@Query("from Student where firstName=:firstName")
	List<Student> findAllStudentsByFirstName(@Param("firstName") String firstName); // :firstName

	@Query("from Student where score>:min and score<:max")
	List<Student> findStudentsForGivenScores(@Param("min") int min, @Param("max") int max);

	@Modifying
	@Query("delete from Student where firstName=:firstName")
	void deleteStudentsByFirstName(@Param("firstName") String firstName); // :firstName

	@Query(value = "select * from student", nativeQuery = true)
	List<Student> findAllStudentNQ();

	@Query(value = "select * from student where fname=:firstName", nativeQuery = true) // native query. So, using actual column names fname in qeury
	List<Student> findByFirstNQ(@Param("firstName")String firstName);

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