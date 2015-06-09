package com.maximus.bean;


public class Section {

	private int id;
	private String text;
	private boolean expanded;
	private boolean hasChildren;
//	private String classes;
	private int pid;
//	private List<?> children;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
//	public String getClasses() {
//		return classes;
//	}
//	public void setClasses(String classes) {
//		this.classes = classes;
//	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
//	public List<?> getChildren() {
//		return children;
//	}
//	public void setChildren(List<?> children) {
//		this.children = children;
//	}
//	
}
