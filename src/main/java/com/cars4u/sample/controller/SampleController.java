package com.cars4u.sample.controller;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cars4u.sample.common.CommonForm;
import com.cars4u.sample.common.EncryptionUtils;
import com.cars4u.sample.common.MaskParam;
import com.cars4u.sample.common.StoredProcedure;
import com.cars4u.sample.common.UnMaskParam;
import com.cars4u.sample.entity.Info;
import com.cars4u.sample.entity.Sample;
import com.cars4u.sample.service.impl.InfoService;
import com.cars4u.sample.service.impl.SampleService;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Controller
@RequestMapping("sample")
public class SampleController {

	@Autowired
	SampleService sampleService;
	@Autowired
	InfoService infoService;
	@Autowired
	StoredProcedure storedProcedure;

	@RequestMapping(value = "reg", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView landingPage() {
		ModelAndView modelAndView = new ModelAndView();
		Sample sample = new Sample();
		modelAndView.addObject("sample", sample);
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping(value = "log", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView save(Sample sample) {
		ModelAndView modelAndView = new ModelAndView();
		if (sample.getUserName() != null && sample != null) {
			sampleService.saveAllDetails(sample);
		}
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping(value = "logine", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView logine(@RequestParam(required = false)String ajaxMessage) {
		ModelAndView modelAndView = new ModelAndView();
		Sample sample2 = new Sample();
		modelAndView.addObject("sample2", sample2);
		if(ajaxMessage != null && ajaxMessage.equals("Y")) {
			modelAndView.addObject("ajaxMessage", "Your Password has been updated Successfully");
		}
//		modelAndView.addObject("string", string);
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@RequestMapping(value = "home", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView home(Sample sample2) {
		ModelAndView modelAndView = new ModelAndView();
		Sample log = sampleService.getLogin(sample2.getUserName(), sample2.getPassword());
		if (log != null) {
			modelAndView.setViewName("homePage");
		} else if (log == null) {
			return new ModelAndView("redirect:/sample/logine");
		}
		return modelAndView;
	}

	@RequestMapping(value = "frgtPasswd", method = { RequestMethod.GET, RequestMethod.POST })
	public String ForgotPass(ModelMap map) {
//	ModelAndView modelAndView = new ModelAndView();
		int length = 6;
		String numbs = "0123456789";
		Random random = new Random();
		char[] otp = new char[length];
		for (int i = 0; i < length; i++) {
			otp[i] = numbs.charAt(random.nextInt(numbs.length()));
		}
		String string = new String(otp);
		map.addAttribute("string", string);
//		model.addAttribute("string", string);
//		return new ModelAndView("redirect:/logine",model);
//		modelAndView.addObject("string", string);
//		modelAndView.setViewName("otp");
		return "otp";
	}

	@RequestMapping(value = "confirmotp", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView confirmOtp(@RequestParam(required = false) String currentOtp) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("currentOtp", currentOtp);
		modelAndView.setViewName("otpConfirmation");
		return modelAndView;

	}

	@RequestMapping(value = "newpass", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView newPasswd(@RequestParam(required = false)String ajaxMessage) {
		ModelAndView modelAndView = new ModelAndView();
		CommonForm commonForm = new CommonForm();
		modelAndView.addObject("commonForm", commonForm);
		if(ajaxMessage != null && ajaxMessage.equals("N")) {
		modelAndView.addObject("ajaxMessage", "No such user Found!");
		}
		modelAndView.setViewName("newPasswd");
		return modelAndView;
	}

	@RequestMapping(value = "pasave", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView savePassword(CommonForm commonForm,ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView();
		Boolean result = null;
//		String ajaxMessage = null;
		if (commonForm != null) {
			result = sampleService.updatePassword(commonForm.getUserName(), commonForm.getPassword());
			if (result) {
				modelMap.addAttribute("ajaxMessage", "Y");
				return new ModelAndView("redirect:/sample/logine",modelMap);
			} else {
				modelMap.addAttribute("ajaxMessage", "N");
				return new ModelAndView("redirect:/sample/newpass",modelMap);
			}
		}
		return modelAndView;	
	}
	
	@RequestMapping(value="scheduleAppointment" , method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView scheduleAppointment()
	{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("appointmentForm");
		return modelAndView;
		
	}
	
	@RequestMapping(value = "appointmentForm" , method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView appointmentForm() {
		ModelAndView modelAndView = new ModelAndView();
		Info info = new Info();
		modelAndView.addObject("info", info);
		modelAndView.setViewName("Form");
		return modelAndView;
	}
	
	@RequestMapping(value = "saveForm", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView saveForm(Info info, ModelMap model) {
		Info result = null;
		if (info != null) {
			result = infoService.saveFormDetails(info);
			if (result != null) {
				if (result.getId() != null) {
					model.addAttribute("encryptId", result.idEnc("/sample"));
					return new ModelAndView("redirect:/sample/saveSuccess",model);
				}
			}
		}
		return null;
	}
	
	@RequestMapping(value="saveSuccess" , method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView saveSuccess( @RequestParam String encryptId) {
		
		ModelAndView modelAndView =new ModelAndView();
		Long id = EncryptionUtils.decrypt(encryptId);
		if(id != null) {
		storedProcedure.callSequenceGenerator(id);
		Info info= infoService.getDetailsById(id);
		modelAndView.addObject("info", info);
		}
		
		modelAndView.setViewName("SuccessInfo");
		return modelAndView;
	}
	
	@RequestMapping(value="status" , method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView showStatus() {
		ModelAndView modelAndView = new ModelAndView();
		List<Info> info = infoService.getAllInfoList();
		modelAndView.addObject("info", info);
		modelAndView.setViewName("StatusInfo");
		return modelAndView;
		
	}
	
	@RequestMapping(value="editDetails" , method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView editDetails(@RequestParam String id) {
		ModelAndView modelAndView = new ModelAndView();
		Long infoId = EncryptionUtils.decrypt(id);
		Info info = new Info();
		if(infoId != null) {
			info = infoService.getDetailsById(infoId);
		}
//		Date date = info.getDate();
//		Instant instant = date.toInstant();
//		LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		String dateStr = localDate.format(formatter);
		modelAndView.addObject("info", info);
		//modelAndView.addObject("dateStr", dateStr);
		modelAndView.setViewName("EditForm");
		return modelAndView;	
	}
	
	@RequestMapping(value="updateForm" , method = {RequestMethod.GET , RequestMethod.POST})
	public ModelAndView updateForm(Info info) {
		ModelAndView modelAndView = new ModelAndView();
		
		Info res = infoService.updateDetails(info);
		return modelAndView;
		
	}


}
