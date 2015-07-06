package com.zy.tasksystem;

import android.content.Context;

public class Config {

	public static final String APP_ID = "com.zy.tasksystem";
//	public static final String SERVER_URL="http://192.168.191.1:8080/taskSystem/myapp";
//	public static final String SERVER_IMAGE_URL="http://192.168.191.1:8080/taskSystem/images/";
//	public static final String SERVER_IMAGE_URL1="http://192.168.191.1:8080/taskSystem/images";
	
	public static final String SERVER_URL="http://123.57.38.138:8081/taskSystem/myapp";
	public static final String SERVER_IMAGE_URL="http://123.57.38.138:8081/taskSystem/images/";
	public static final String SERVER_IMAGE_URL1="http://123.57.38.138:8081/taskSystem/images";
	
    public static final String KEY_TOKEN = "token";
    public static final String KEY_METHOD = "method";
    public static final String KEY_PHONE_NUM = "phonenum";
    public static final String KEY_STATUS = "status";
    public static final String KEY_ICON_URI = "iconUri";
    public static final String KEY_USER_SEX = "user_sex";
    public static final String KEY_USER_PWD = "user_pwd";
	public static final String KEY_CODE = "user_code";
	public static final String KEY_PHONE_MD5 = "phone_md5";
	public static final String KEY_PAGE_NUM = "pagenum";
	public static final String KEY_PAGE_SIZE = "pagesize";
	public static final String KEY_GET_TASKS = "tasks";
	
	public static final String KEY_GET_USER = "user";
	public static final String KEY_TASK_ID = "taskId";
	public static final String KEY_TASK_CONTENT = "taskContent";
	public static final String KEY_ATTEN_TIMES = "interestTimes";
	public static final String KEY_COMMENT_TIMES = "riticismTimes";
	public static final String KEY_PUB_ICON = "userIcon";
	public static final String KEY_PUB_SCHOOL = "userSchool";
	public static final String KEY_PUB_GENDER = "userGender";
	public static final String KEY_PUB_TIME = "dateTime";
	public static final String KEY_PUB_NAME = "userName";
	public static final String KEY_USER_NAME = "user_name";
	public static final String KEY_USER_SCHOOL = "user_school";
	public static final String KEY_USER_ICON = "user_icon";
	public static final String KEY_COMMENT_PUB_PHONE = "pubPhone";
	
	
	public static final String KEY_COMMENTS = "comments";
	public static final String KEY_COMMENT_ID = "commentId";
	public static final String KEY_COMMENT_CONTENT="commentContent";
	public static final String KEY_COMMENT_PUB_NAME = "pubName";
	public static final String KEY_COMMENT_PUB_GENDER = "pubGender";
	public static final String KEY_COMMENT_PUB_SCHOOL = "pubSchool";
	public static final String KEY_COMMENT_PUB_PHOTO = "pubPhoto";
	public static final String KEY_COMMENT_REPLY_ID = "replyId";
	
	
	public static final String METHOD_REGIST = "regist";
	public static final String METHOD_LOGIN = "login";
	public static final String METHOD_GET_TASK = "gettask";
	public static final String METHOD_GET_COMMENT = "get_comment";
	public static final String METHOD_PUB_COMMENT = "pub_comment";
	public static final String METHOD_PUB_TASK = "pub_task";
	public static final String METHOD_SETTING_UP = "setting_up";
	public static final String METHOD_MY_TASK = "my_pub_task";
	public static final String METHOD_TAKECAR = "take_care";
	
    public static final int RESULT_STATUS_SUCCESS = 0;  //成功
    public static final int RESULT_STATUS_FAIL = 1;    //失败
    public static final int RESULT_STATUS_TOKEN_INVALID = 2;  //token过期
    public static final int RESULT_NO_ANY = 3;           //没有更多
    
    
    public static final String METHOD_GET_CODE="getCode";
    
	public static final String CHARSET = "UTF-8";
	
	public static String getToken(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_TOKEN, null);
	}

	public static void setToken(Context context ,String token) {
		context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit().putString(KEY_TOKEN, token).commit();
	}
	
	public static String getPhoneNum(Context context) {
		return context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).getString(KEY_PHONE_NUM, null);
	}

	public static void setPhoneNum(Context context ,String phone_num) {
		context.getSharedPreferences(APP_ID, Context.MODE_PRIVATE).edit().putString(KEY_PHONE_NUM, phone_num).commit();
	}
	
	
}
