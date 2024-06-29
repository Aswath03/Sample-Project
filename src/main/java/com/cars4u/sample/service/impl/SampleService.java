package com.cars4u.sample.service.impl;

import java.util.List;

import com.cars4u.sample.api.dto.ApiResponseDTO;
import com.cars4u.sample.entity.Info;
import com.cars4u.sample.entity.Sample;

public interface SampleService {

	Sample saveAllDetails(Sample sample);

	Sample getLogin(String userName, String password);

	Boolean updatePassword(String userName, String password);

	List<ApiResponseDTO> fetchAllVehicleDetails(String year);

}
