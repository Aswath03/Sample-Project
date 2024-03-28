package com.cars4u.sample.common;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cars4u.sample.repo.SequenceMasterRepo;

import jakarta.transaction.Transactional;

@Component
public class StoredProcedure {
	
	@Autowired
	SequenceMasterRepo sequenceMasterRepo;
	
	@Transactional
	public void callSequenceGenerator(Long id) {
		Date localDate = new Date();
		LocalDate currentDate = localDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Integer month = currentDate.getMonthValue();
		Integer year = currentDate.getYear();
		try
		{
			sequenceMasterRepo.callSequenceGenerator(id , month , year);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
