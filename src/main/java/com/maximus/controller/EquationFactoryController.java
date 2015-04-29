package com.maximus.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maximus.util.Latex2Image;

@Controller
public class EquationFactoryController {

	@RequestMapping(value = {"/equation"}, method = RequestMethod.GET)
	public void equation(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String laTex = request.getParameter("tex");
		if (null == laTex || "".equals(laTex))
			return;
		response.setContentType("image/png");
		ServletOutputStream outputStream = response.getOutputStream();
		Latex2Image.conv(laTex, outputStream);
		outputStream.flush();
		outputStream.close();
	}
}
