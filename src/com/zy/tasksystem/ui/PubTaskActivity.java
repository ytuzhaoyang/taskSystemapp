package com.zy.tasksystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zy.tasksystem.BaseActivity;
import com.zy.tasksystem.Config;
import com.zy.tasksystem.R;
import com.zy.tasksystem.net.PubTask;

public class PubTaskActivity extends BaseActivity implements OnClickListener {

	private Button but_back_home, but_pubtask, but_add_icon;
    private EditText et_pubtask;
    private String token,phone_md5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.layout_pubtask);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_pub_task);

		et_pubtask = (EditText) findViewById(R.id.et_pubtask);
		but_back_home = (Button) findViewById(R.id.title_back);
		but_pubtask = (Button) findViewById(R.id.but_pubtask);
		but_add_icon = (Button) findViewById(R.id.but_pubtask_icon);

		but_back_home.setOnClickListener(this);
		but_pubtask.setOnClickListener(this);
		but_add_icon.setOnClickListener(this);
		
		Intent intent = getIntent();
		token = intent.getStringExtra(Config.KEY_TOKEN);
		phone_md5 = intent.getStringExtra(Config.KEY_PHONE_MD5);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			Intent intent = new Intent(PubTaskActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_out);
			break;
		case R.id.but_pubtask:
			doPub();
			break;
		case R.id.but_pubtask_icon:

			break;

		default:
			break;
		}
	}

	//发布任务
	private void doPub() {
		
		if(TextUtils.isEmpty(et_pubtask.getText())){
			Toast.makeText(PubTaskActivity.this, R.string.content_not_empty, Toast.LENGTH_LONG).show();
			return;
		}
		new PubTask(phone_md5, token, et_pubtask.getText().toString(), new PubTask.SuccessCallback() {
			
			@Override
			public void onSuccess() {
              et_pubtask.setText("");
              Toast.makeText(PubTaskActivity.this, R.string.success_to_pub_task, Toast.LENGTH_SHORT).show();
              setResult(RESULT_OK);
              Intent intent = new Intent(PubTaskActivity.this, HomeActivity.class);
  			startActivity(intent);
  			finish();
  			overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_out);
			}
		}, new PubTask.FailCallback() {
			
			@Override
			public void onFail(int errorCode) {
			       if(errorCode == Config.RESULT_STATUS_TOKEN_INVALID){
			    	   //token 过期
			    	   Toast.makeText(PubTaskActivity.this, R.string.please_login, Toast.LENGTH_LONG).show();
			       }else{
			    	   Toast.makeText(PubTaskActivity.this, R.string.fail_to_pub_task, Toast.LENGTH_LONG).show();
			       }
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent(PubTaskActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_out);
		return super.onKeyDown(keyCode, event);
	}
}
