package com.cars4u.sample.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.cars4u.sample.common.EncryptionUtils;
import com.cars4u.sample.common.MaskParam;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "information", schema = "sample", catalog = "sample")
@Data
public class Info {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String mailId;
	@Column(name = "mobile")
	private String mobileNo;
	@Column(name = "vehicle_name")
	private String vehicleName;
	@Column(name = "vehicle_model")
	private String vehicleModel;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sequence_id")
	private SequenceMaster sequenceMaster;
	@Column(name="sequence_no")
	private String sequenceNo;
	@Column(name = "sequence_date")
	private Date sequenceDate;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name = "date")
	private Date date;
	@Column(name = "status")
	private String status="CONFIRMED";
	@Column(name = "reason")
	private String reason;
	@Column(name = "type")
	private String type;
	@Column(name = "file_path")
	private String filePath;
	
	@Transient
	private MultipartFile uploadFile;
	@Transient
	private String dateStr;
	@Transient
	private String fileName; 
	@Transient
	private String particulars;
	
//	public String idEnc() {
//		return new MaskParam().mask(null, this.id.toString());
//	}
	public String idEnc(String string) {
		new EncryptionUtils();
		return EncryptionUtils.encrypt(this.id);
	}
	
//	public String idEnc(String target) {
//		return new MaskParam().mask(target, this.id.toString());
//	}
	
	
	
	
	
	
	

}
