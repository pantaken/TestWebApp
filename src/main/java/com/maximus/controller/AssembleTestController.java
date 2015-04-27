package com.maximus.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.maximus.db.QueryHelper;
import com.maximus.service.OfficeWordParserService;
import com.maximus.util.BeautyOutputWord;
import com.maximus.util.MixOutputWord;
/**
 * 组题、组卷
 * @author wangxianlei
 *
 */
@Controller
public class AssembleTestController {

	@Autowired
	private QueryHelper queryHelper;
	/**
	 * 返回前端的参数
	 */
	private final Map<String, Object> maps = new HashMap<String, Object>();
	@Autowired
	private OfficeWordParserService officeWordParserService;
	
	@Autowired
	private BeautyOutputWord beautyOutputWord;
	
	@Autowired
	private MixOutputWord mixOutputWord;
	/**
	 * 返回完美公式视图
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/assembletestpaper", method = RequestMethod.GET)
	public ModelAndView assembletestpaper(HttpServletRequest request, HttpServletResponse response) {
		maps.put("lists", officeWordParserService.query());
		return new ModelAndView("assembletestpaper", maps);
	}
	/**
	 * 试卷概要分析
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/totalanalyse", method = RequestMethod.GET)
	public ModelAndView totalanalyse(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("totalanalyse", maps);
	}
	/**
	 * 组卷下载
	 * @param request
	 * @param repoHttpServletResponse
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/downloadassembletest", method = RequestMethod.GET)
	public ModelAndView downloadassembletest(HttpServletRequest request, HttpServletResponse repoHttpServletResponse) throws Exception {
		beautyOutputWord.setOutputFileName("123456789.docx");
		beautyOutputWord.save();
		return null;
	}
	/**
	 * 图片公式组卷下载
	 * @param request
	 * @param repoHttpServletResponse
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/downloadmixassembletest", method = RequestMethod.GET)
	public ModelAndView downloadmixassembletest(HttpServletRequest request, HttpServletResponse repoHttpServletResponse) throws Exception {
		mixOutputWord.setOutputFileName("abcdefghi.docx");
		mixOutputWord.save();
		return null;
	}	
	/**
	 * 返回图片公式视图
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/mixedequation", method = RequestMethod.GET)
	public ModelAndView mixedequation(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> lists = queryHelper.query("SELECT content FROM pub_question_common ORDER BY id ASC");
		maps.put("questionCommons", lists);
		return new ModelAndView("mixedequation", maps);
	}
	
	@RequestMapping(value = "/standardequation", method = RequestMethod.GET)
	public ModelAndView standardequation(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("standardequation");
	}
}
