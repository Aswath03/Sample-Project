package com.cars4u.sample.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cars4u.sample.entity.Info;

public interface InfoRepository extends JpaRepository<Info, Long>{

	Info findAllById(Long id);

}
