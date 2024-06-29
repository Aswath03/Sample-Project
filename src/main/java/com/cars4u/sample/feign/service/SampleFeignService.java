package com.cars4u.sample.feign.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cars4u.sample.api.dto.ApiResponseDTO;

@FeignClient(value = "sample-api", url = "http://localhost:8080/")
public interface SampleFeignService {

	@PostMapping(value = "details")
	List<ApiResponseDTO> getAllVehicleDetails(@RequestParam String year);

}
