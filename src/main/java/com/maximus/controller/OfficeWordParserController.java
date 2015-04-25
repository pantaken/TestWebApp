package com.maximus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.maximus.service.OfficeWordParserService;
import com.maximus.util.ConstantUtil;
import com.maximus.util.IOHttp;

@Controller
public class OfficeWordParserController {

	private final Logger logger = LoggerFactory.getLogger(OfficeWordParserController.class);
	
	@Autowired
	private OfficeWordParserService officeWordParserService;
	
	@RequestMapping(value = "/beautyParser", method = RequestMethod.POST)
	public ModelAndView beautyParser(HttpServletRequest request, HttpServletResponse response) {
		String fileName = IOHttp.upload(request);
		String filePath = ConstantUtil.getInstance().getRealPath() + ConstantUtil.UPLOAD_DIR;
		if (fileName != null)
			filePath += fileName;
		else 
			return null;
		try {
			officeWordParserService.process(filePath);
		} catch (Docx4JException e) {
			e.printStackTrace();
			logger.error("when beauty parser word occurred Exception : " + e);
		}
		return null;
	}
}
