package com.zy.tasksystem.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.zy.tasksystem.Config;
import com.zy.tasksystem.utils.HttpMethod;

public class TakeCare {

	public TakeCare(String token, String phone_md5,int taskId ,final SuccessCallback successCallback, final FailCallback failCallback){
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
						case Config.RESULT_NO_ANY:
							//已经评论
							if(failCallback!=null){
								failCallback.onFail(Config.RESULT_NO_ANY);
							}
							break;
						case Config.RESULT_STATUS_TOKEN_INVALID:
							if(failCallback!=null){
								failCallback.onFail(Config.RESULT_STATUS_TOKEN_INVALID);
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
					if(failCallback !=null){
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
		}, Config.KEY_METHOD,Config.METHOD_TAKECAR,Config.KEY_TOKEN,token,Config.KEY_PHONE_MD5,phone_md5,Config.KEY_TASK_ID,taskId+"");
	}
	
	public static interface SuccessCallback{
		void onSuccess();
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
}
