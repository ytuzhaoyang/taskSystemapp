package com.zy.tasksystem.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.image.SmartImageView;
import com.zy.tasksystem.BaseActivity;
import com.zy.tasksystem.Config;
import com.zy.tasksystem.R;
import com.zy.tasksystem.adapter.TaskListAdapter;
import com.zy.tasksystem.net.TaskList;
import com.zy.tasksystem.po.TaskBean;
import com.zy.tasksystem.utils.MakeMD5;

public class HomeActivity extends BaseActivity implements OnClickListener {

	private SharedPreferences sp;
	private static final int PAGESIZE = 10;
	protected static final String TAG = "HomeActivity";
	private static final int SUCCESS_PUB_TASK = 1;

	private Button but_pub_task, but_hometitle_menu;

	private String token, phone, userName, userSchool, userGender, userIcon;
	private TaskBean bean;

	private TaskListAdapter adapter;

	private PullToRefreshListView lv_task_list;
	private static int pageNum;
	private int i = 0;
	private SlidingMenu slidingMenu;

	private LinearLayout slidingmenu_login, slidingmenu_regist,
			slidingmenu_setting, slidingmenu_logout, slidingmenu_main;

	private LinearLayout slidinglayout_show_info, slidinglogin_enter_home,
			slidinglogin_mytask, slidinglogin_setting, slidinglogin_logout,
			slidinglogin_about_us;
	private SmartImageView iv_user_icon;
	private TextView tv_user_name;
	// 标示对学校的选择
	private String select_school = "all";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.aty_list_task);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		Spinner spinner = (Spinner) findViewById(R.id.sp_home_selectschool);

		sp = getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE);
		token = sp.getString(Config.KEY_TOKEN, "");
		phone = sp.getString(Config.KEY_PHONE_NUM, "");
		userName = sp.getString(Config.KEY_USER_NAME, "");
		userSchool = sp.getString(Config.KEY_USER_SCHOOL, "");
		userGender = sp.getString(Config.KEY_USER_SEX, "");
		userIcon = sp.getString(Config.KEY_USER_ICON, "");

		String[] strs = { "所有学校", "我的学校" };
		ArrayAdapter<String> ad = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, strs);
		ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(ad);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					select_school = "all";

				} else if (position == 1) {
					select_school = userSchool;
				}
				loadTasks(1, select_school);
			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		lv_task_list = (PullToRefreshListView) findViewById(R.id.lv_task_list);
		lv_task_list.setMode(Mode.BOTH); // 设置PullToRefreshListView的模式为上下

		lv_task_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				bean = adapter.getItem(position - 1);
				Intent intent = new Intent(HomeActivity.this,
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
				slidingMenu.showContent();
				overridePendingTransition(R.anim.tran_next_enter,
						R.anim.tran_next_out);
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
							// 下拉刷新
							pageNum = 1;
							loadTasks(1, select_school);
						} else {
							// 下拉加载更多
							pageNum++;
							// Toast.makeText(HomeActivity.this,
							// "这是第" + pageNum + "页数据", Toast.LENGTH_SHORT)
							// .show();
							loadTasks(pageNum, select_school);
						}

						lv_task_list.onRefreshComplete();
					};
				}.execute();
			}
		});

		// 加载任务
		loadTasks(1, select_school);
		adapter = new TaskListAdapter(HomeActivity.this);
		lv_task_list.setAdapter(adapter);

		but_hometitle_menu = (Button) findViewById(R.id.but_hometitle_menu);
		but_pub_task = (Button) findViewById(R.id.but_hometitle_pubtask);

		but_pub_task.setOnClickListener(this);
		but_hometitle_menu.setOnClickListener(this);

		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setBehindOffsetRes(R.dimen.shadow_width);// SlidingMenu划出时主页面显示的剩余宽度
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		if (TextUtils.isEmpty(token)) {
			// token 不合法
			unloginHandler();

		} else {
			loginHandler();
		}
	}

	private void loginHandler() {
		slidingMenu.setMenu(R.layout.slidingmenu_login);
		slidinglayout_show_info = (LinearLayout) findViewById(R.id.ll_slidinglogin_show_info);
		slidinglogin_enter_home = (LinearLayout) findViewById(R.id.ll_slidinglogin_enter_home);
		slidinglogin_mytask = (LinearLayout) findViewById(R.id.ll_slidinglogin_mytask);
		slidinglogin_setting = (LinearLayout) findViewById(R.id.ll_slidinglogin_setting);
		slidinglogin_logout = (LinearLayout) findViewById(R.id.ll_slidinglogin_logout);
		slidinglogin_about_us = (LinearLayout) findViewById(R.id.ll_slidinglogin_about_us);

		iv_user_icon = (SmartImageView) findViewById(R.id.iv_user_icon);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);

		iv_user_icon.setImageUrl(Config.SERVER_IMAGE_URL + userIcon);

		if (!TextUtils.isEmpty(userName)) {
			tv_user_name.setText(userName);
		} else {
			tv_user_name.setText(phone);
		}

		slidinglayout_show_info.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

			}
		});

		slidinglogin_enter_home.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				slidingMenu.toggle(false);
			}
		});

		slidinglogin_mytask.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						MyTaskActivity.class);
				intent.putExtra(Config.KEY_TOKEN, token);
				intent.putExtra(Config.KEY_PHONE_MD5, MakeMD5.md5(phone));
				startActivity(intent);
				slidingMenu.showContent();
				overridePendingTransition(R.anim.tran_next_enter,
						R.anim.tran_next_out);
			}
		});

		/*
		 * 设置
		 */
		slidinglogin_setting.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						SettingupActivity.class);
				// 把当前用户的信息带给SettingActivity，用于用户信息回显

				intent.putExtra(Config.KEY_PHONE_MD5, MakeMD5.md5(phone));
				intent.putExtra(Config.KEY_TOKEN, token);
				intent.putExtra(Config.KEY_USER_NAME, userName);
				intent.putExtra(Config.KEY_USER_SEX, userGender);
				intent.putExtra(Config.KEY_USER_SCHOOL, userSchool);

				startActivity(intent);
				slidingMenu.showContent();
				overridePendingTransition(R.anim.tran_next_enter,
						R.anim.tran_next_out);

			}
		});

		/*
		 * 直接退出时候，查除数据
		 */
		slidinglogin_logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Config.setToken(HomeActivity.this, "");
				Config.setPhoneNum(HomeActivity.this, "");
				Editor editor = sp.edit();

				editor.putString(Config.KEY_USER_NAME, "");
				editor.putString(Config.KEY_USER_SCHOOL, "");
				editor.putString(Config.KEY_USER_SEX, "");
				editor.commit();
				finish();

			}
		});
		slidinglogin_about_us.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						AboutUsActivity.class);
				startActivity(intent);
				slidingMenu.showContent();
				overridePendingTransition(R.anim.tran_next_enter,
						R.anim.tran_next_out);
			}
		});
	}

	private void unloginHandler() {
		slidingMenu.setMenu(R.layout.slidingmenu_unlogin);
		slidingmenu_login = (LinearLayout) findViewById(R.id.ll_slidingmenu_login);
		slidingmenu_regist = (LinearLayout) findViewById(R.id.ll_slidingmenu_regist);
		// slidingmenu_setting = (LinearLayout)
		// findViewById(R.id.ll_slidingmenu_setting);
		slidingmenu_logout = (LinearLayout) findViewById(R.id.ll_slidingmenu_logout);
		slidingmenu_main = (LinearLayout) findViewById(R.id.ll_slidingmenu_main);
		slidingmenu_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
				slidingMenu.showContent();
				// 定义activity切换时的动画效果
				overridePendingTransition(R.anim.tran_next_enter,
						R.anim.tran_next_out);
			}
		});

		slidingmenu_regist.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						RegistActivity.class);
				startActivity(intent);
				finish();
				slidingMenu.showContent();
				overridePendingTransition(R.anim.tran_next_enter,
						R.anim.tran_next_out);
			}
		});

		slidingmenu_logout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		slidingmenu_main.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				slidingMenu.showContent();
			}
		});

	}

	// 加载任务列表
	private void loadTasks(int page, String school) {

		new TaskList(token, page, PAGESIZE, school,
				new TaskList.SuccessCallback() {

					public void onSuccess(int page, int pageSize,
							List<TaskBean> items) {
						pageNum = page;
						if(page==1){
						adapter.removeAll();
						}
						adapter.addAll(items);
						adapter.notifyDataSetChanged();
					}
				}, new TaskList.FailCallback() {
					public void onFail(int errorCode) {
						// pd.dismiss();
						if (errorCode == Config.RESULT_STATUS_FAIL) {
							// 服务器异常
							Toast.makeText(HomeActivity.this,
									R.string.fail_to_load_task,
									Toast.LENGTH_SHORT).show();
						} else if (errorCode == Config.RESULT_NO_ANY) {
							Toast.makeText(HomeActivity.this, R.string.not_any,
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.but_hometitle_pubtask:

			userName = sp.getString(Config.KEY_USER_NAME, "");
			userSchool = sp.getString(Config.KEY_USER_SCHOOL, "");
			userGender = sp.getString(Config.KEY_USER_SEX, "");
			if (!"login_token".equals(token)) {
				Toast.makeText(HomeActivity.this, R.string.please_login,
						Toast.LENGTH_SHORT).show();
				return;
			}
			if ("".equals(userName) || "".equals(userSchool)) {
				Toast.makeText(HomeActivity.this, R.string.please_setting_info,
						Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent = new Intent(HomeActivity.this, PubTaskActivity.class);
			intent.putExtra(Config.KEY_TOKEN, token);
			intent.putExtra(Config.KEY_PHONE_MD5, MakeMD5.md5(phone));
			startActivityForResult(intent, SUCCESS_PUB_TASK);
			overridePendingTransition(R.anim.tran_next_enter,
					R.anim.tran_next_out);
			// slidingMenu.showContent();
			break;
		case R.id.but_hometitle_menu:
			if (i % 2 == 0) {
				slidingMenu.showMenu();
			} else {
				slidingMenu.showContent();
				
			}
			i++;
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SUCCESS_PUB_TASK:
			loadTasks(1, select_school);
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	private long exitTime = 0; // 记录按键时间计算时间差
	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}

}
