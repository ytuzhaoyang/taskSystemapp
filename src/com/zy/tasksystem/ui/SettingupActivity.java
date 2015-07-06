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
import android.widget.ImageView;
import android.widget.Toast;

import com.zy.tasksystem.BaseActivity;
import com.zy.tasksystem.Config;
import com.zy.tasksystem.R;
import com.zy.tasksystem.net.SettingUp;

public class SettingupActivity extends BaseActivity implements OnClickListener {

	private Button title_back, btn_setting_submit;
	private ImageView iv_setting_notfition, iv_setting_gender;
	private EditText et_setting_real_name, et_setting_campus;
    private String token,phone,gender,name,school;
    private SharedPreferences sp ;
    
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.layout_setting_up);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_setting_up);

		sp = getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE);
		Intent intent = getIntent();
		token = intent.getStringExtra(Config.KEY_TOKEN);
        phone = intent.getStringExtra(Config.KEY_PHONE_MD5);
        name =intent.getStringExtra(Config.KEY_USER_NAME);
        school = intent.getStringExtra(Config.KEY_USER_SCHOOL);
        gender = intent.getStringExtra(Config.KEY_USER_SEX);
        
		iv_setting_notfition = (ImageView) findViewById(R.id.iv_setting_notfition);
		iv_setting_gender = (ImageView) findViewById(R.id.iv_setting_gender);
		et_setting_real_name = (EditText) findViewById(R.id.et_setting_real_name);
		et_setting_campus = (EditText) findViewById(R.id.et_setting_campus);
		title_back = (Button) findViewById(R.id.title_back);
		btn_setting_submit = (Button) findViewById(R.id.btn_setting_submit);

		
		if (!TextUtils.isEmpty(name)) {
			et_setting_real_name.setText(name);
		}
		if (!TextUtils.isEmpty(school)) {
			et_setting_campus.setText(school);
		}

		if ("male".equals(gender)) {
			iv_setting_gender.setImageResource(R.drawable.male);
		}
		if ("female".equals(gender)) {
			iv_setting_gender.setImageResource(R.drawable.female);
		}

		iv_setting_gender.setOnClickListener(this);
		iv_setting_notfition.setOnClickListener(this);
		title_back.setOnClickListener(this);
		btn_setting_submit.setOnClickListener(this);

	}

	private static int i = 0, j = 0;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_setting_notfition:
			if (i % 2 == 0) {
				iv_setting_notfition
						.setImageResource(R.drawable.msp_switch_2x_off);
			} else {
				iv_setting_notfition
						.setImageResource(R.drawable.msp_switch_2x_on);
			}
			i++;
			break;
		case R.id.iv_setting_gender:
			if("male".equals(gender)){
				if (j % 2 == 0) {
					iv_setting_gender.setImageResource(R.drawable.female);
					gender = "female";
				} else {
					iv_setting_gender.setImageResource(R.drawable.male);
					gender = "male";
				}
			}else{
				if (j % 2 == 0) {
					iv_setting_gender.setImageResource(R.drawable.male);
					gender = "male";
				} else {
					iv_setting_gender.setImageResource(R.drawable.female);
					gender = "female";
				}
			}
			
			j++;
			break;
		case R.id.title_back:
			Intent intent = new Intent(SettingupActivity.this, HomeActivity.class);
			intent.putExtra(Config.KEY_TOKEN, token);
			intent.putExtra(Config.KEY_PHONE_NUM, phone);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_out);
			break;

		case R.id.btn_setting_submit:
			if (TextUtils.isEmpty(et_setting_real_name.getText())) {
				Toast.makeText(SettingupActivity.this,
						R.string.realname_not_empty, Toast.LENGTH_LONG).show();
				return;
			}
			if (TextUtils.isEmpty(et_setting_campus.getText())) {
				Toast.makeText(SettingupActivity.this,
						R.string.campul_not_empty, Toast.LENGTH_LONG).show();
				return;
			}
			/*
			 * 把填写的数据送回服务器
			 */
			
			name = et_setting_real_name.getText().toString();
			school = et_setting_campus.getText().toString();
			
			new SettingUp(phone, gender, name, school, new SettingUp.SuccessCallback() {
				
				@Override
				public void onSuccess() {
					Toast.makeText(SettingupActivity.this, R.string.success_setting_up,
							Toast.LENGTH_LONG).show();
					Editor edit = sp.edit();
					edit.putString(Config.KEY_USER_NAME, name);
					edit.putString(Config.KEY_USER_SEX, gender);
					edit.putString(Config.KEY_USER_SCHOOL, school);
					edit.commit();
					et_setting_campus.setText("");
					et_setting_real_name.setText("");
					iv_setting_gender.setImageResource(R.drawable.male);
					
				}
			}, new SettingUp.FailCallback() {
				
				@Override
				public void onFail() {
					Toast.makeText(SettingupActivity.this, R.string.fail_to_setting_up,Toast.LENGTH_LONG).show();
					
				}
			});
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent(SettingupActivity.this, HomeActivity.class);
		intent.putExtra(Config.KEY_TOKEN, token);
		intent.putExtra(Config.KEY_PHONE_NUM, phone);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_out);
		return super.onKeyDown(keyCode, event);
	}
}
