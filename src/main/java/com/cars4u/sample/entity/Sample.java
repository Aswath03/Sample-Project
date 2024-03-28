package com.cars4u.sample.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "registration" , schema = "sample" , catalog = "sample")
public class Sample {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;
	@Column(name ="user_name")
	private String userName;
	@Column(name ="mobile_no")
	private Long mobileNo;
	@Column(name ="pass")
	private String password;
}
