package com.zy.tasksystem.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.zy.tasksystem.Config;
import com.zy.tasksystem.utils.HttpMethod;

public class GetCode {

	public GetCode(String phone, final SuccessCallback successCallback, final FailCallback failCallback) {
		
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
		
			
			@Override
			public void onSuccess(String result) {
             	try {
					JSONObject jsonObj = new JSONObject(result);
					switch (jsonObj.getInt(Config.KEY_STATUS)) {
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
					if(failCallback!= null){
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
		}, Config.KEY_METHOD,Config.METHOD_GET_CODE,Config.KEY_PHONE_NUM,phone);
	}
	
	public static interface SuccessCallback{
		void onSuccess();
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
