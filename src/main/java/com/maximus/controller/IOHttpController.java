package com.maximus.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maximus.util.IOHttp;

@Controller
public class IOHttpController {

	private Map<String, Object> map = new HashMap<String, Object>();
	private final static Logger logger = LoggerFactory.getLogger(IOHttpController.class);
	
	@ResponseBody
	@RequestMapping(value = {"/upload"}, method = RequestMethod.POST)
	public Map<String, Object> upload(HttpServletRequest request, HttpServletResponse response) {
		logger.info("upload a file...");
		if (IOHttp.upload(request) != null) {
			map.put("success", true);
		} else {
			map.put("success", false);
		}
		return map;
	}
}
