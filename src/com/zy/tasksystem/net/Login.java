package com.zy.tasksystem.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.zy.tasksystem.Config;
import com.zy.tasksystem.po.UserBean;
import com.zy.tasksystem.utils.HttpMethod;

public class Login {

	public Login(String phone_md5,String pwd,final SuccessCallback successCallback,final FailCallback failCallback){
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject json = new JSONObject(result);
					switch (json.getInt(Config.KEY_STATUS)) {
					case Config.RESULT_STATUS_SUCCESS:
						if(successCallback!=null){
							JSONObject obj = json.getJSONObject(Config.KEY_GET_USER);
							
							successCallback.onSuccess(json.getString(Config.KEY_TOKEN),new UserBean(obj.getString("userName"), obj.getString("userSex"), obj.getString("userPhoto"), obj.getString("schoolName")));
						}
						break;

					default:
						if(failCallback!=null){
							failCallback.onFail(Config.RESULT_STATUS_FAIL);
						}
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					System.out.println(e.toString());
					if(failCallback!=null){
						System.out.println("*************************");
						failCallback.onFail(Config.RESULT_STATUS_FAIL);
						
					}
				}
			}
		}, new NetConnection.FailCallback() {
			@Override
			public void onFail() {
				if(failCallback!=null){
					failCallback.onFail(Config.RESULT_STATUS_FAIL);
				}
				
			}
		}, Config.KEY_METHOD,Config.METHOD_LOGIN,Config.KEY_PHONE_MD5,phone_md5,Config.KEY_USER_PWD,pwd);
	}
	
	public static interface SuccessCallback{
		//成功时返回token
		void onSuccess(String token,UserBean bean);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
}
