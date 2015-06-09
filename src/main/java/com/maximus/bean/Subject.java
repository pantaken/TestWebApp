package com.maximus.bean;

import java.util.List;

public class Subject {

	private int id;
	private String serialcode;
	private String subjectname;
	private List<Category> categorys;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSerialcode() {
		return serialcode;
	}
	public void setSerialcode(String serialcode) {
		this.serialcode = serialcode;
	}
	public String getSubjectname() {
		return subjectname;
	}
	public void setSubjectname(String subjectname) {
		this.subjectname = subjectname;
	}
	public List<Category> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}
}
