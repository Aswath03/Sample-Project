package com.cars4u.sample.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cars4u.sample.entity.Sample;

@Repository
public interface SampleRepo extends JpaRepository<Sample, Long>{

	@Query(value = "FROM Sample where userName=?1 and password=?2")
	Sample findByUserNameAndPassword(String userName, String password);

	Sample findIdByUserName(String userName);
	
//	@Query(value = "update sample.registration set pass= ?1 where id=?2" , nativeQuery = true)
//	Sample updatePasswordById(String password, Long id);
	




}
