package com.maximus.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.maximus.service.OfficeWordParserService;
import com.maximus.service.SubjectSectionService;

@Controller
public class WebPageController {

	private final Map<String, Object> maps = new HashMap<String, Object>();
	
	@Autowired
	private SubjectSectionService sss;
	@Autowired
	private OfficeWordParserService officeWordParserService;
	/**
	 * 返回目录管理视图页面
	 * @return
	 */
	@RequestMapping("/directory_manager")
	public ModelAndView directory_manager(HttpServletRequest request, HttpServletResponse response) {
		String tab = request.getParameter("tab");
		boolean isPjax = Boolean.parseBoolean(request.getHeader("X-PJAX"));
		List<?> list = sss.querySubject();
		maps.put("subjects", list);
		if (isPjax && (!"".equals(tab) && null != tab)) {
			return new ModelAndView("tab/" + tab, maps);		
		} else if (!"".equals(tab) && null != tab){
			return new ModelAndView("link/" + tab, maps);
		} else {
			return new ModelAndView("directory_manager", maps);
		}
	}
	/**
	 * 返回首页左侧导航页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		String tab = request.getParameter("tab");
		boolean isPjax = Boolean.parseBoolean(request.getHeader("X-PJAX"));
		List<?> list = sss.querySubject();
		maps.put("subjects", list);
		if (isPjax && (!"".equals(tab) && null != tab)) {
			if ("testfiling".equals(tab)) {
				maps.put("lists", officeWordParserService.query());
				return new ModelAndView("tab/" + tab, maps);
			} else {
				return new ModelAndView("tab/" + tab, maps);
			}
		} else {
			return new ModelAndView("link/" + tab, maps);
		}
	}
}
