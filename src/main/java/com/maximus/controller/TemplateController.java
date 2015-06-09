package com.maximus.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maximus.util.ConstantUtil;
import com.maximus.util.IOHttp;

@Controller
public class TemplateController {

	private final Map<String, Object> maps = new HashMap<String, Object>();
	
	@ResponseBody
	@RequestMapping("/templateUpload")
	public Map<?, ?> templateUpload(HttpServletRequest request, HttpServletResponse response) {
		String fileName = IOHttp.upload(request, ConstantUtil.TEMPLATE_DIR);
		if (fileName != null) {
			maps.put("success", true);
		} else {
			maps.put("success", false);
		}
		return maps;
	}
}
