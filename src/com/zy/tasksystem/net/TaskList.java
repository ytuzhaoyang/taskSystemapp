package com.zy.tasksystem.net;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zy.tasksystem.Config;
import com.zy.tasksystem.po.TaskBean;
import com.zy.tasksystem.utils.DateTools;
import com.zy.tasksystem.utils.HttpMethod;

public class TaskList {

	public TaskList(String token, int page, int pagesize,String school,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(
				Config.SERVER_URL,
				HttpMethod.POST,
				new NetConnection.SuccessCallback() {

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
										String str = json.getString(Config.KEY_PUB_TIME);
										String time = DateTools.dateFormat(str);
										
										
										
										System.out.println("**********************"+time);
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
						if (failCallback != null) {
							
							failCallback.onFail(Config.RESULT_STATUS_FAIL);
						}
					}
				}, Config.KEY_METHOD, Config.METHOD_GET_TASK, Config.KEY_TOKEN,
				token, Config.KEY_PAGE_NUM, page + "", Config.KEY_PAGE_SIZE,
				pagesize + "",Config.KEY_USER_SCHOOL,school);

	}

	public static interface SuccessCallback {
		void onSuccess(int page, int pageSize, List<TaskBean> items);
	}

	public static interface FailCallback {
		void onFail(int errorCode);
	}
}
