//package com.cars4u.sample.api.utils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import com.cars4u.sample.api.dto.ApiResponseDTO;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.JsonObject;
//
//@Controller
//public class CommonUtils {
//
//	@Value("${sample.api}")
//	private String url;
//
//	public List<ApiResponseDTO> getVehicleDetails(String year) {
//
//		JsonObject requestParam = new JsonObject();
//		requestParam.addProperty("year", year);
//
//		RestTemplate restTemplate = new RestTemplate();
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
//
//		HttpEntity<String> entity = new HttpEntity<>(requestParam.toString(), headers);
//
////	     ParameterizedTypeReference<List<ApiResponseDTO>> responseType = new ParameterizedTypeReference<List<ApiResponseDTO>>() {};
//
//		// Build the URI with the required query parameter 'year'
//		String fullUrl = UriComponentsBuilder.fromHttpUrl(url + "details").queryParam("year", year).toUriString();
//
//		ResponseEntity<String> responseEntity = restTemplate.exchange(fullUrl, HttpMethod.POST, entity, String.class);
////		 ResponseEntity<String> responseEntity = restTemplate.exchange(url + "details" , HttpMethod.POST ,entity,String.class ); 
//
////		 ResponseEntity<List<ApiResponseDTO>> responseEntity = restTemplate.exchange(url + "details" , HttpMethod.GET ,entity,responseType ); 
//
//		String responseBody = responseEntity.getBody();
//		System.out.println("Response Body: " + responseBody);
//
////		 List<ApiResponseDTO> responseBody = responseEntity.getBody();
//
//		if (responseBody != null) {
////			Gson gson = new Gson();
////			JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
////			List<ApiResponseDTO> apiResponseDTO = new ArrayList<>();
////			for (JsonElement jsonElement : jsonArray) {
////				System.out.println(jsonElement.toString());
////				ApiResponseDTO dto = gson.fromJson(jsonElement, ApiResponseDTO.class);
////				apiResponseDTO.add(dto);
////			}
////			return apiResponseDTO;
//			 ObjectMapper objectMapper = new ObjectMapper();
//	            List<ApiResponseDTO> apiResponseDTOList = new ArrayList<>();
//	            try {
//	                apiResponseDTOList = objectMapper.readValue(responseBody, new TypeReference<List<ApiResponseDTO>>() {});
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
//	            return apiResponseDTOList;
//		}
//		
//		return null;
//	}
//
//}
