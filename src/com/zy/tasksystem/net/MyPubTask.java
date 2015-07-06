package com.zy.tasksystem.net;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zy.tasksystem.Config;
import com.zy.tasksystem.po.TaskBean;
import com.zy.tasksystem.utils.DateTools;
import com.zy.tasksystem.utils.HttpMethod;

public class MyPubTask {

	public MyPubTask(String token,int page, int pagesize,String phone_md5,final SuccessCallback successCallback, final FailCallback failCallback) {
            new NetConnection(Config.SERVER_URL, HttpMethod.POST, new NetConnection.SuccessCallback() {
				
				@Override
				public void onSuccess(String result) {
					try {
						JSONObject obj = new JSONObject(result);
						switch (obj.getInt(Config.KEY_STATUS)) {
						case Config.RESULT_STATUS_SUCCESS:
							if (successCallback != null) {
								List<TaskBean> lists = new ArrayList<TaskBean>();
								JSONArray array = obj
										.getJSONArray(Config.KEY_GET_TASKS);
								JSONObject json = new JSONObject();
								for (int i = 0; i < array.length(); i++) {
									
									json = array.getJSONObject(i);
									
									String time = "";
									String str = json.getString(Config.KEY_PUB_TIME);
									
									time = DateTools.dateFormat(str);
									
//									String[] strs= str.split("-");
//									int[] times =new int[10];
//									for(int j = 0; j<strs.length;j++){
//										times[j]=Integer.parseInt(strs[j]);
//									}
//									Calendar calendar = Calendar.getInstance();
//									if(calendar.get(Calendar.YEAR)-times[0]!=0){
//										time = times[0]+"年"+times[1]+"月"+times[2]+"日"+times[3]+":"+times[4];
//										
//									}else if(times[1]-calendar.get(Calendar.MONTH)!=1){
//										time = times[1]+"月"+times[2]+"日"+times[3]+":"+times[4];
//									}else if(calendar.get(Calendar.DATE)-times[2]>2){
//										time = times[1]+"月"+times[2]+"日"+times[3]+":"+times[4];
//									}else if(calendar.get(Calendar.DATE)-times[2]==2){
//										time = "前天"+times[3]+":"+times[4];
//									}else if(calendar.get(Calendar.DATE)-times[2]==1){
//										time = "昨天"+times[3]+":"+times[4];
//									}else if(calendar.get(Calendar.DATE)-times[2]==0){
//										time = "今天"+times[3]+":"+times[4];
//									}
									
									lists.add(new TaskBean(
											json.getInt(Config.KEY_TASK_ID),
											json.getString(Config.KEY_TASK_CONTENT),
											json.getString(Config.KEY_PUB_NAME),
											time,
											json.getString(Config.KEY_PUB_GENDER),
											json.getString(Config.KEY_PUB_SCHOOL),
											json.getString(Config.KEY_PUB_ICON),
											json.getInt(Config.KEY_COMMENT_TIMES),
											json.getInt(Config.KEY_ATTEN_TIMES)));
								}

								successCallback.onSuccess(
										obj.getInt(Config.KEY_PAGE_NUM),
										obj.getInt(Config.KEY_PAGE_SIZE),
										lists);
							}
							break;
							
						case Config.RESULT_STATUS_FAIL:
							if(failCallback!=null){
								failCallback.onFail(Config.RESULT_STATUS_FAIL);
							}
							break;
						case Config.RESULT_NO_ANY:
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
							if (failCallback != null) {
								failCallback.onFail(Config.RESULT_STATUS_FAIL);
							}
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						System.out.println(e.toString());
						if (failCallback != null) {
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
			}, Config.KEY_METHOD,Config.METHOD_MY_TASK,Config.KEY_TOKEN,token,Config.KEY_PHONE_MD5,phone_md5,Config.KEY_PAGE_NUM, page + "", Config.KEY_PAGE_SIZE,
			pagesize + "");
	}
	
	public static interface SuccessCallback{
		//成功时返回token
		void onSuccess(int page, int pageSize, List<TaskBean> items);
	}
	
	public static interface FailCallback{
		void onFail(int errorCode);
	}
}
