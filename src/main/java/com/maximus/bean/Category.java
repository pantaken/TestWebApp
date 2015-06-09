package com.maximus.bean;

import java.util.List;

public class Category {

	private int id;
	private String serialcode;
	private String category;
	private List<Subject> subjects;
	
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public List<Subject> getSubjects() {
		return subjects;
	}
	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}
}
