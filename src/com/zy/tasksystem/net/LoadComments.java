package com.zy.tasksystem.net;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zy.tasksystem.Config;
import com.zy.tasksystem.po.CommentBean;
import com.zy.tasksystem.utils.HttpMethod;

public class LoadComments {

	public LoadComments(String phone_md5,String token,int page,int pagesize,int taskId,final SuccessCallback successCallback,final FailCallback failCallback){
		new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
			
			@Override
			public void onSuccess(String result) {
				try {
					JSONObject obj = new JSONObject(result);
					switch (obj.getInt(Config.KEY_STATUS)) {
					case Config.RESULT_STATUS_SUCCESS:
						if(successCallback!=null){
							List<CommentBean> lists = new ArrayList<CommentBean>();
							JSONArray comments = obj.getJSONArray(Config.KEY_COMMENTS);
							JSONObject json = new JSONObject();
						    for (int i = 0; i < comments.length(); i++) {
								json = comments.getJSONObject(i);
								lists.add(new CommentBean(json.getInt(Config.KEY_COMMENT_ID), json.getString(Config.KEY_COMMENT_CONTENT), json.getString(Config.KEY_COMMENT_PUB_NAME), json.getString(Config.KEY_PUB_TIME), json.getString(Config.KEY_COMMENT_PUB_GENDER), json.getString(Config.KEY_COMMENT_PUB_SCHOOL), json.getString(Config.KEY_COMMENT_PUB_PHOTO), json.getInt(Config.KEY_COMMENT_REPLY_ID),json.getString(Config.KEY_COMMENT_PUB_PHONE)));
							}
						    
						    successCallback.onSuccess(obj.getInt(Config.KEY_PAGE_NUM), obj.getInt(Config.KEY_PAGE_SIZE), lists);
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
					System.out.println("8888888");
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
		}, Config.KEY_METHOD,Config.METHOD_GET_COMMENT,Config.KEY_TOKEN,token,Config.KEY_PHONE_MD5,phone_md5,Config.KEY_PAGE_NUM,page+"",Config.KEY_PAGE_SIZE,pagesize+"",Config.KEY_TASK_ID,taskId+"");
	}
	
	public static interface SuccessCallback{
		
		void onSuccess(int page, int pagesize, List<CommentBean> comments);
	}
	
	public static interface FailCallback{
		void onFail();
	}
}
