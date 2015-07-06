package com.zy.tasksystem.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.zy.tasksystem.Config;
import com.zy.tasksystem.utils.HttpMethod;

public class SettingUp {

	public SettingUp(String phone_md5,String gender,String name,String school,final SuccessCallback successCallback, final FailCallback failCallback){
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject obj = new JSONObject(result);
							switch (obj.getInt(Config.KEY_STATUS)) {
					case Config.RESULT_STATUS_SUCCESS:
						if(successCallback!=null){
							successCallback.onSuccess();
						}
						break;

					default:
						if(failCallback!=null){
							failCallback.onFail();
						}
						break;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(failCallback!=null){
						failCallback.onFail();
					}
				}
				}
		}, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				if(failCallback!=null){
					failCallback.onFail();
				}
			}
		}, Config.KEY_METHOD,Config.METHOD_SETTING_UP,Config.KEY_PHONE_MD5,phone_md5,Config.KEY_USER_SEX,gender,Config.KEY_USER_NAME,name,Config.KEY_USER_SCHOOL,school);
		
	}
	
	public static interface SuccessCallback{
		//成功时返回token
		void onSuccess();
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
