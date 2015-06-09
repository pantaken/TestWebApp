package com.maximus.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.maximus.bean.Category;
import com.maximus.bean.Subject;
import com.maximus.bean.SubjectCategoryLink;
import com.maximus.bean.Section;
import com.maximus.db.QueryHelper;
import com.maximus.service.SubjectSectionService;

@Controller
public class SubjectSectionController {

	private final Map<String, Object> mps = new HashMap<String, Object>();
	
	@Autowired
	private SubjectSectionService sss;
	
	@ResponseBody
	@RequestMapping("/section_add")
	public Map addSection(HttpServletRequest request, HttpServletResponse response) {
		String node = request.getParameter("node");
		int pnode = Integer.parseInt(request.getParameter("pnode"));
		Section ss = new Section();
		ss.setText(node);
		ss.setPid(pnode);
		if (sss.saveSection(ss)) {
			mps.put("success", true);
		} else {
			mps.put("success", false);
		}
		return mps;
	}
	
	@ResponseBody
	@RequestMapping("/addcategory")
	public Map addcategory(HttpServletRequest request, HttpServletResponse response) {
		String serialcode = request.getParameter("subjectname");
		String categoryname = request.getParameter("category");
//		System.out.println(serialcode);
//		System.out.println(category);
		
		Category category = new Category();
		category.setCategory(categoryname);
		category.setSerialcode(UUID.randomUUID().toString());
		
		Subject subject = sss.querySubjectBySerialcode(serialcode);
		
		SubjectCategoryLink scl = new SubjectCategoryLink();
		scl.setCategory(category);
		scl.setSubject(subject);
		if (sss.saveCategory(category)) {
			mps.put("success", true);
			sss.saveSubjectCategoryLink(scl);
		} else {
			mps.put("success", false);
		}
		return mps;
	}
	@ResponseBody
	@RequestMapping("/section_query")
	public List<?> querySection(HttpServletRequest request, HttpServletResponse response) {
		String root = request.getParameter("root");
		List<Section> list =  (List<Section>) sss.query();
		for (Section ss : list) {
			ss.setHasChildren(true);
			ss.setExpanded(false);
		}		
		if ("source".equals(root)) {
			return list; 
		} else {
			List<Section> list2 =  (List<Section>) sss.query(Integer.parseInt(root));
			for (Section ss2 : list2) {
				ss2.setHasChildren(true);
			}
			return list2;
		}
	}
	@ResponseBody
	@RequestMapping("/category_query")
	public List<?> queryCategory(HttpServletRequest request,HttpServletResponse response) {
		String subjectcode = request.getParameter("subjectcode");
		return sss.queryCategory(subjectcode);
	}
	@ResponseBody
	@RequestMapping("/savesubjectinfo")
	public ModelAndView savesubjectinfo(HttpServletRequest request, HttpServletResponse response) {
//		System.out.println("*************************");
//		System.out.println(getRequestPayload(request));
//		System.out.println("*************************");
		String subjectname = request.getParameter("subjectname");
		String subjectcode = request.getParameter("subjectcode");
		Subject s = new Subject();
		s.setSerialcode(subjectcode);
		s.setSubjectname(subjectname);
		if (sss.saveSubject(s)) {
			mps.put("success", true);
			mps.put("message", "已成功添加课程编码为["+subjectcode+"],课程名称为["+subjectname+"]的课程信息");
		} else {
			mps.put("success", false);
			mps.put("message", "课程信息添加失败");
		}
		return new ModelAndView("link/subjectinfo", mps);
	}	
	/**
	 * 获取Request Payload参数内容
	 * @param request
	 * @return
	 */
	private String getRequestPayload(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = request.getReader();
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff,0,len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
}
