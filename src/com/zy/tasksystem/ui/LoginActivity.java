package com.zy.tasksystem.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.tasksystem.BaseActivity;
import com.zy.tasksystem.Config;
import com.zy.tasksystem.R;
import com.zy.tasksystem.net.Login;
import com.zy.tasksystem.po.UserBean;
import com.zy.tasksystem.utils.MakeMD5;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText et_phoneNum;
	private EditText et_pwd;
	private Button but_login;
	private TextView tv_login_registnow;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.layout_login);

		sp = getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE);

		et_phoneNum = (EditText) findViewById(R.id.et_login_phonenum);
		et_pwd = (EditText) findViewById(R.id.et_login_pwd);
		but_login = (Button) findViewById(R.id.but_login_login);

		tv_login_registnow = (TextView) findViewById(R.id.tv_login_registnow);

		tv_login_registnow.setOnClickListener(this);
		but_login.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_login_registnow:
			Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.tran_next_enter,
					R.anim.tran_next_out);
			break;
		case R.id.but_login_login:
			if (TextUtils.isEmpty(et_phoneNum.getText())) {
				Toast.makeText(LoginActivity.this, R.string.phone_not_empty,
						Toast.LENGTH_LONG).show();
				break;
			}
			if (TextUtils.isEmpty(et_pwd.getText())) {
				Toast.makeText(LoginActivity.this,
						R.string.login_please_input_pwd, Toast.LENGTH_LONG)
						.show();
				break;
			}
			String phone_num = MakeMD5.md5(et_phoneNum.getText().toString());
			String pwd = MakeMD5.md5(et_pwd.getText().toString());

			new Login(phone_num, pwd, new Login.SuccessCallback() {

				@Override
				public void onSuccess(String token, UserBean bean) {
					// 登录成功，缓存token，缓存当前用户手机号
					Config.setToken(LoginActivity.this, token);
					Config.setPhoneNum(LoginActivity.this, et_phoneNum
							.getText().toString());
					Editor editor = sp.edit();
					editor.putString(Config.KEY_USER_NAME, bean.getUserName());
					editor.putString(Config.KEY_USER_SEX, bean.getUserSex());
					editor.putString(Config.KEY_USER_SCHOOL,
							bean.getSchoolName());
					editor.putString(Config.KEY_USER_ICON, bean.getUserPhoto());
					editor.putString(Config.KEY_USER_ICON, bean.getUserPhoto());
					editor.commit();
					// 跳转到主界面
					Intent intent = new Intent(LoginActivity.this,
							HomeActivity.class);
					intent.putExtra(Config.KEY_TOKEN, token);
					intent.putExtra(Config.KEY_PHONE_NUM, et_phoneNum.getText()
							.toString());
					startActivity(intent);
					finish();
				}

			}, new Login.FailCallback() {

				@Override
				public void onFail(int errorCode) {
					if (errorCode == 1) {
						Toast.makeText(LoginActivity.this,
								R.string.login_login_fail, Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(LoginActivity.this,
								R.string.login_fail_unknow, Toast.LENGTH_SHORT)
								.show();
					}
				}
			});

			break;

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.tran_pre_enter,
					R.anim.tran_pre_out);
		}
		return super.onKeyDown(keyCode, event);
	}
}
