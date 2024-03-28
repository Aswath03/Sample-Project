package com.cars4u.sample.service.impl;

import java.util.List;

import com.cars4u.sample.entity.Info;

public interface InfoService {

	Info saveFormDetails(Info info);

	Info getDetailsById(Long id);

	List<Info> getAllInfoList();

	Boolean updateDetails(Info info);

}
