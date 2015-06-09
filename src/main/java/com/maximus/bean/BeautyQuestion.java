package com.maximus.bean;

public class BeautyQuestion {

	private int id;
	/**
	 * 试题内容
	 */
	private String content;
	/**
	 * 课程编码
	 */
	private String subjectcode;
	/**
	 * 试题类型(题目类型编码)
	 */
	private String categorycode;
	/**
	 * 备选答案A、B、C、D
	 */
	private String a;
	private String b;
	private String c;
	private String d;
	private String img;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSubjectcode() {
		return subjectcode;
	}
	public void setSubjectcode(String subjectcode) {
		this.subjectcode = subjectcode;
	}
	public String getCategorycode() {
		return categorycode;
	}
	public void setCategorycode(String categorycode) {
		this.categorycode = categorycode;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		this.d = d;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
}
