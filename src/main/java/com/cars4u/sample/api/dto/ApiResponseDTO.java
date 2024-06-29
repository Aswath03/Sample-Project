package com.cars4u.sample.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ApiResponseDTO {
	
	@JsonProperty(value = "VechileName")
	private String vehicleName;
}
