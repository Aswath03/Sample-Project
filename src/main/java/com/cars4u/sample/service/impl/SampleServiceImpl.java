package com.cars4u.sample.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cars4u.sample.api.dto.ApiResponseDTO;
import com.cars4u.sample.entity.Sample;
import com.cars4u.sample.feign.service.SampleFeignService;
import com.cars4u.sample.repo.SampleRepo;

@Service
public class SampleServiceImpl implements SampleService {

	@Autowired
	SampleRepo repo;
	@Autowired
	SampleFeignService sampleFeignService;
//	@Autowired
//	CommonUtils commonUtils;
	
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

	@Override
	public List<ApiResponseDTO> fetchAllVehicleDetails(String year) {
		List<ApiResponseDTO> response = null;
 
		try {
//			response = commonUtils.getVehicleDetails(year);
			response = sampleFeignService.getAllVehicleDetails(year);
			if (response != null) {
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return response;
	}
}


