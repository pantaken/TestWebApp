package com.maximus.dao;

import java.util.List;

import com.maximus.bean.Category;
import com.maximus.bean.Subject;
import com.maximus.bean.SubjectCategoryLink;
import com.maximus.bean.Section;

public interface ISubjectSectionDao {

	/**
	 * 返回值为主键id
	 * @param bean
	 * @return
	 */
	public int insert(Section ss);
	
	public int insertSubject(Subject s);
	public List<?> selectRoot();
	public List<?> selectNode(int pid);

	public List<?> selectSubject();

	public int insertCategory(Category bean);

	public int insertSubjectCategoryLink(SubjectCategoryLink scl);

	public List<?> selectCategory(String subjectcode);

	public Subject selectSubjectBySerialcode(String serialcode);
}
