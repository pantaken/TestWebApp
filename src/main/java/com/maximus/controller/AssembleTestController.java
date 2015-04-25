package com.maximus.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.maximus.service.OfficeWordParserService;
/**
 * 组题、组卷
 * @author wangxianlei
 *
 */
@Controller
public class AssembleTestController {

	/**
	 * 返回前端的参数
	 */
	private final Map<String, Object> maps = new HashMap<String, Object>();
	@Autowired
	private OfficeWordParserService officeWordParserService;
	
	@RequestMapping(value = "/assembletestpaper", method = RequestMethod.GET)
	public ModelAndView assembletestpaper(HttpServletRequest request, HttpServletResponse response) {
		maps.put("lists", officeWordParserService.query());
		return new ModelAndView("assembletestpaper", maps);
	}
}
