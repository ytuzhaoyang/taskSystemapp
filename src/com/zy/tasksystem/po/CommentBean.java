package com.zy.tasksystem.po;

import java.util.ArrayList;
import java.util.List;

public class CommentBean {

	private int commentId;
	private String commentContent;
	private String pubName;
	private String pubTime;
	private String pubGender;
	private String pubSchool;
	private String pubIcon;
	private String pubPhone;
	private int replyId;
	private List<ReplyBean> replyList = new ArrayList<ReplyBean>();  //回复内容列表

	public CommentBean() {
	}

	

	public CommentBean(int commentId, String commentContent, String pubName, String pubTime, String pubGender, String pubSchool, String pubIcon,int replyId, String pubPhone) {
		super();
		this.commentId = commentId;
		this.commentContent = commentContent;
		this.pubName = pubName;
		this.pubTime = pubTime;
		this.pubGender = pubGender;
		this.pubSchool = pubSchool;
		this.pubIcon = pubIcon;
		this.replyId = replyId;
		this.pubPhone = pubPhone;
		//this.replyList = replyList;
	}

	public String getPubPhone() {
		return pubPhone;
	}



	public void setPubPhone(String pubPhone) {
		this.pubPhone = pubPhone;
	}



	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
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

	public List<ReplyBean> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<ReplyBean> replyList) {
		this.replyList = replyList;
	}
}
