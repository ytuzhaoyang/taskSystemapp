package com.zy.tasksystem.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.zy.tasksystem.Config;
import com.zy.tasksystem.utils.HttpMethod;

public class Regist {

	public Regist(String sex,String phonenum,String pwd,String user_icon,final SuccessCallback successCallback,final FailCallback failCallback){
           new NetConnection(Config.SERVER_URL, HttpMethod.POST,new NetConnection.SuccessCallback(){

			@Override
			public void onSuccess(String result) {
				
				try {
					JSONObject jsonObj = new JSONObject(result);
					switch (jsonObj.getInt(Config.KEY_STATUS)) {
					case Config.RESULT_STATUS_SUCCESS:
						if(successCallback!=null){
							successCallback.onSuccess(jsonObj.getString(Config.KEY_TOKEN));
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
					if(failCallback != null){
						failCallback.onFail(Config.RESULT_STATUS_FAIL);
					}
				}
			}
        	   
           }, new NetConnection.FailCallback() {
			
			@Override
			public void onFail() {
				if(failCallback!= null){
					failCallback.onFail(Config.RESULT_STATUS_FAIL);
				}
			}
		}, Config.KEY_METHOD,Config.METHOD_REGIST,Config.KEY_USER_SEX,sex,Config.KEY_PHONE_MD5,phonenum,Config.KEY_USER_PWD,pwd,Config.KEY_USER_ICON,user_icon);
	}

	public static interface SuccessCallback{
		void onSuccess(String token);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
}
