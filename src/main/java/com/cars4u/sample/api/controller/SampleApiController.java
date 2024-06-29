package com.cars4u.sample.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cars4u.sample.api.dto.ApiResponseDTO;
import com.cars4u.sample.service.impl.SampleService;

@Controller
@RequestMapping(value = "api")
public class SampleApiController {
	
	@Autowired
	SampleService sampleService;

	@RequestMapping(value = "info",method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView getVehicleDetails(@RequestParam String year){
		ModelAndView modelAndView = new ModelAndView();
		List<ApiResponseDTO> response = null;
		if(year != null && !year.isEmpty()) {
			response = sampleService.fetchAllVehicleDetails(year);
			if(response != null) {
				modelAndView.addObject("response", response);
			}
		}
		modelAndView.setViewName("VehicleList");
		return modelAndView;
		
	}
}
