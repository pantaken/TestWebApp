package com.maximus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maximus.bean.Category;
import com.maximus.bean.Subject;
import com.maximus.bean.SubjectCategoryLink;
import com.maximus.bean.Section;
import com.maximus.dao.ISubjectSectionDao;

@Service
public class SubjectSectionService {

	@Autowired
	private ISubjectSectionDao dao;
	
	public boolean saveSection(Section ss) {
		int count = dao.insert(ss);
		if (count != 0) {
			return true;
		} else {
			return false;
		}
	}
	public boolean saveSubject(Subject s) {
		int count = dao.insertSubject(s);
		if (count != 0) {
			return true;
		} else {
			return false;
		}
	}
	public List<?> query() {
		return dao.selectRoot();
	}
	
	public List<?> query(int pid) {
		return dao.selectNode(pid);
	}
	
	public List<?> querySubject() {
		return dao.selectSubject();
	}
	public boolean saveCategory(Category bean) {
		int count = dao.insertCategory(bean);
		if (count != 0) {
			return true;
		} else {
			return false;
		}
	}
	public boolean saveSubjectCategoryLink(SubjectCategoryLink scl) {
		int count = dao.insertSubjectCategoryLink(scl);
		if (count != 0) 
			return true;
		else
			return false;
	}
	public List<?> queryCategory(String subjectcode) {
		return dao.selectCategory(subjectcode);
		
	}
	public Subject querySubjectBySerialcode(String serialcode) {
		return dao.selectSubjectBySerialcode(serialcode);
	}
}
