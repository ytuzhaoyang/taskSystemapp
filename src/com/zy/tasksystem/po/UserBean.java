package com.zy.tasksystem.po;

public class UserBean {

	private String userName;
    private String userSex;
    private String userPhoto;
    private String schoolName;
    private String userPwd;
    private String userPhone;
    
    public UserBean() {}
    
	public UserBean(String userName, String userSex, String userPhoto,
			String schoolName) {
		super();
		this.userName = userName;
		this.userSex = userSex;
		this.userPhoto = userPhoto;
		this.schoolName = schoolName;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
    
    
}
