package com.cars4u.sample.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="sequence_master" ,schema = "sample" ,catalog = "sample")
@Data
public class SequenceMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "month")
	private Integer month;
	@Column(name = "year")
	private Integer year;
	@Column(name = "token")
	private Long token;
	@Column(name = "token_number")
	private String tokenNumber;
	@Column(name = "token_date")
	private Date tokenDate;
	@Column(name = "info_id")
	private Long infoId;
}
