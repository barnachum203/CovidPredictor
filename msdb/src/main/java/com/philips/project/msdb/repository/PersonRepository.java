package com.philips.project.msdb.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.philips.project.msdb.beans.Person;




public interface PersonRepository extends JpaRepository<Person, Integer>{

//	public Person findBy_Id(int id);

//	public Person findByResult(String result);
//	public Person findByGender(String gender);
//	public Person findByIdCity(int idCity);


	@Transactional
	@Modifying
	@Query(value = "update person set result=:result where id=:id", nativeQuery=true)
	public int updatePersonResult(int id,String result);

	@Transactional
	@Modifying
	@Query(value = "update person set date=:date where id=:id", nativeQuery=true)
	public int updatePersonDate(int id,String date);

	@Transactional
	@Modifying
	@Query(value = "SELECT * FROM person where date=:date", nativeQuery=true)
	public List<Person> findByResultDate(String date);


}
