package com.cars4u.sample.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cars4u.sample.entity.SequenceMaster;

import jakarta.transaction.Transactional;

public interface SequenceMasterRepo  extends JpaRepository<SequenceMaster, Long> {

	@Transactional
	@Query(value = "call sample.new_token_generation(?1, ?2, ?3)", nativeQuery = true)
	@Modifying
	void callSequenceGenerator(Long id, Integer month, Integer year);
	

}
