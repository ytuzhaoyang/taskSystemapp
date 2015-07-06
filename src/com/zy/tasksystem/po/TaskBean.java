package com.zy.tasksystem.po;

public class TaskBean {

	private int taskId;
	private String taskContent;
	private String pubName;
	private String pubTime;
	private String pubGender;
	private String pubSchool;
	private String pubIcon;
	private int commentTimes;
	private int attens;
	
	
	public TaskBean(int taskId, String taskContent, String pubName,
			String pubTime, String pubGender, String pubSchool, String pubIcon,
			int commentTimes, int attens) {
		super();
		this.taskId = taskId;
		this.taskContent = taskContent;
		this.pubName = pubName;
		this.pubTime = pubTime;
		this.pubGender = pubGender;
		this.pubSchool = pubSchool;
		this.pubIcon = pubIcon;
		this.commentTimes = commentTimes;
		this.attens = attens;
	}

	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public String getTaskContent() {
		return taskContent;
	}
	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}
	public String getPubName() {
		return pubName;
	}
	public void setPubName(String pubName) {
		this.pubName = pubName;
	}
	public String getPubTime() {
		return pubTime;
	}
	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}
	public String getPubGender() {
		return pubGender;
	}
	public void setPubGender(String pubGender) {
		this.pubGender = pubGender;
	}
	public String getPubSchool() {
		return pubSchool;
	}
	public void setPubSchool(String pubSchool) {
		this.pubSchool = pubSchool;
	}
	public String getPubIcon() {
		return pubIcon;
	}
	public void setPubIcon(String pubIcon) {
		this.pubIcon = pubIcon;
	}
	public int getCommentTimes() {
		return commentTimes;
	}
	public void setCommentTimes(int commentTimes) {
		this.commentTimes = commentTimes;
	}
	public int getAttens() {
		return attens;
	}
	public void setAttens(int attens) {
		this.attens = attens;
	}
}
