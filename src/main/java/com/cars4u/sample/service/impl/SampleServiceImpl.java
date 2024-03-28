package com.cars4u.sample.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cars4u.sample.entity.Sample;
import com.cars4u.sample.repo.SampleRepo;

@Service
public class SampleServiceImpl implements SampleService {

	@Autowired
	SampleRepo repo;

	@Override
	public Sample saveAllDetails(Sample sample) {

		return repo.save(sample);
	}

	@Override
	public Sample getLogin(String userName, String password) {
		Sample sample = new Sample();
		try {
			sample = repo.findByUserNameAndPassword(userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sample;
	}

	@Override
	public Boolean updatePassword(String userName, String password) {
		Sample sample = new Sample();
//		Sample sample2 =  null;
		Boolean res= false; 
		try {
			sample=repo.findIdByUserName(userName);
			if(sample != null)
			{
				sample.setPassword(password);
				repo.save(sample);
//			sample2=repo.updatePasswordById(password,sample.getId());
//			if(sample2 != null)
//			{
//				res =  true;
//			}
			return res=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}
}


