package com.cars4u.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VehicleRentalSystem1Application {

	public static void main(String[] args) {
		SpringApplication.run(VehicleRentalSystem1Application.class, args);
	}

}
