package com.cars4u.sample.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cars4u.sample.common.StoredProcedure;
import com.cars4u.sample.entity.Info;
import com.cars4u.sample.kafka.producer.KafkaProducer;
import com.cars4u.sample.repo.InfoRepository;

import jakarta.transaction.Transactional;

@Service
public class InfoServiceImpl implements InfoService {
	
	@Autowired
	InfoRepository infoRepository;
	
	@Autowired
	KafkaProducer kafkaProducer;
	
	@Value("${files.vehicle.path}")
	String uploadsTemp;
	
	@Transactional
	@Override
	public Info saveFormDetails(Info info) {
		String fileName = null;
		 Path destionationPath = null;
		 Boolean res = false;
		try
		{
			if(info != null) {
			if(info.getUploadFile() != null && info.getUploadFile().getSize()>0) {
				//Folder name 
				SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
				String dateForFolder = dateFormat.format(new Date());
				//for getting name of the file
				fileName = info.getUploadFile().getOriginalFilename();
				//for getting the file uploaded
				MultipartFile originalFile = info.getUploadFile();
				//Creating a path
				destionationPath = Paths.get(uploadsTemp + "Vehicles/" + dateForFolder);
				
				if(destionationPath.toFile().exists() == false) {
					Files.createDirectories(destionationPath);
				}
				//Create file location with filename
				Path copyFile = Paths.get(destionationPath + File.separator + fileName);
				//transfering file to the location
				originalFile.transferTo(copyFile);
				
				info.setFilePath(copyFile.toString());
			}
			if (info.getDateStr() != null) {
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				   LocalDate localDate = LocalDate.parse(info.getDateStr(), formatter);
				   Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				   info.setDate(date);
			}
				
				info=infoRepository.save(info);
				kafkaProducer.publishKafkaMessageInfo(info.getName());
				 return info;
				  
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	
		
		
	}

	@Override
	public Info getDetailsById(Long id) {
		Info info = new Info();
		if(id != null) {
		info = infoRepository.findAllById(id);
		}
		return info;
	}

	@Override
	public List<Info> getAllInfoList() {
		List<Info> list = new ArrayList<>();
		try
		{
			list = infoRepository.findAll();
			return list;
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Boolean updateDetails(Info info) {
		Info infoObj = new Info();
		Boolean res= false;
		try {
			if (info.getId() != null) {
				infoObj = infoRepository.findAllById(info.getId());
				if (infoObj != null) {
					infoObj.setName(info.getName());
					infoObj.setMailId(info.getMailId());
					infoObj.setMobileNo(info.getMobileNo());
					infoObj.setDate(info.getDate());
					infoObj.setReason(info.getReason());
					infoObj.setStatus(info.getStatus());
					infoObj.setType(info.getType());
					infoObj.setVehicleModel(info.getVehicleModel());
					infoObj.setVehicleName(info.getVehicleName());
				}
					infoRepository.save(infoObj);
					res = true;
					return res;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	

}
