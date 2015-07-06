package com.zy.tasksystem.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.zy.tasksystem.BaseActivity;
import com.zy.tasksystem.R;

public class AboutUsActivity extends BaseActivity{

	private Button title_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.layout_aboutus);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_about_us);
		
		title_back = (Button) findViewById(R.id.title_back);
		
		title_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutUsActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_out);
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent(AboutUsActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_out);
		return super.onKeyDown(keyCode, event);
	}
}
