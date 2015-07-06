package com.zy.tasksystem.ui;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.zy.tasksystem.BaseActivity;
import com.zy.tasksystem.Config;
import com.zy.tasksystem.R;
import com.zy.tasksystem.adapter.TaskListAdapter;
import com.zy.tasksystem.net.MyPubTask;
import com.zy.tasksystem.po.TaskBean;

public class MyTaskActivity extends BaseActivity {

	private static final int PAGESIZE = 10;
	private Button title_back;
	private String token, phone_md5;
	private TaskListAdapter adapter;
	private PullToRefreshListView lv_task_list;
	private TaskBean bean;
	private int pageNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.aty_list_task);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_my_task);

		adapter = new TaskListAdapter(getApplicationContext());
		Intent intent = getIntent();
		if (intent != null) {
			token = intent.getStringExtra(Config.KEY_TOKEN);
			phone_md5 = intent.getStringExtra(Config.KEY_PHONE_MD5);
		}

		title_back = (Button) findViewById(R.id.title_back);

		title_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyTaskActivity.this,
						HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.tran_pre_enter,
						R.anim.tran_pre_out);
			}
		});

		lv_task_list = (PullToRefreshListView) findViewById(R.id.lv_task_list);
		lv_task_list.setMode(Mode.BOTH); // 设置PullToRefreshListView的模式为上下

		lv_task_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				bean = adapter.getItem(position - 1);
				Intent intent = new Intent(MyTaskActivity.this,
						TaskDetailActivity.class);
				intent.putExtra(Config.KEY_TASK_ID, bean.getTaskId());
				intent.putExtra(Config.KEY_PUB_ICON, bean.getPubIcon());
				intent.putExtra(Config.KEY_PUB_NAME, bean.getPubName());
				intent.putExtra(Config.KEY_PUB_GENDER, bean.getPubGender());
				intent.putExtra(Config.KEY_PUB_SCHOOL, bean.getPubSchool());
				intent.putExtra(Config.KEY_PUB_TIME, bean.getPubTime());
				intent.putExtra(Config.KEY_TASK_CONTENT, bean.getTaskContent());
				intent.putExtra(Config.KEY_COMMENT_TIMES,
						bean.getCommentTimes());
				intent.putExtra(Config.KEY_ATTEN_TIMES, bean.getAttens());
				intent.putExtra(Config.KEY_TOKEN, token);
				startActivity(intent);
			}
		});
		lv_task_list.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(final PullToRefreshBase<ListView> refreshView) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}

					protected void onPostExecute(Void result) {

						if (refreshView.isHeaderShown()) {
							// 上拉刷新

							loadTasks(1);
						} else {
							// 下拉加载更多
							pageNum++;
							// Toast.makeText(HomeActivity.this,
							// "这是第" + pageNum + "页数据", Toast.LENGTH_SHORT)
							// .show();
							loadTasks(pageNum);
						}

						lv_task_list.onRefreshComplete();
					};
				}.execute();
			}
		});

		// 加载任务
		loadTasks(1);

		lv_task_list.setAdapter(adapter);
	}

	// 加载任务列表
	private void loadTasks(int page) {

		new MyPubTask(token, page, PAGESIZE, phone_md5,
				new MyPubTask.SuccessCallback() {

					@Override
					public void onSuccess(int page, int pageSize,
							List<TaskBean> items) {

						adapter.removeAll();
						adapter.addAll(items);

					}
				}, new MyPubTask.FailCallback() {

					@Override
					public void onFail(int errorCode) {

						switch (errorCode) {
						case Config.RESULT_STATUS_FAIL:
							Toast.makeText(MyTaskActivity.this, "加载失败",
									Toast.LENGTH_SHORT).show();
							break;
						case Config.RESULT_STATUS_TOKEN_INVALID:
							Toast.makeText(MyTaskActivity.this, "请登录",
									Toast.LENGTH_SHORT).show();
							break;
						case Config.RESULT_NO_ANY:
							Toast.makeText(MyTaskActivity.this,
									R.string.not_any, Toast.LENGTH_SHORT)
									.show();
							break;
						default:
							break;
						}
					}
				});
	}

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		Intent intent = new Intent(MyTaskActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_out);
		return super.onKeyDown(keyCode, event);
	};
}
