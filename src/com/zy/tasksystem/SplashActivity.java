package com.zy.tasksystem;

import com.zy.tasksystem.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.zy.tasksystem.ui.HomeActivity;

public class SplashActivity extends BaseActivity {

	protected static final int MSG_LOGIN = 10;
	protected static final int MSG_UNLOGIN = 20;
	private RelativeLayout linearLayout;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_LOGIN:
				Intent intent = new Intent(SplashActivity.this,
						HomeActivity.class);
				startActivity(intent);

				break;

			case MSG_UNLOGIN:
				Intent intent1 = new Intent(SplashActivity.this,
						HomeActivity.class);
				startActivity(intent1);
			default:

				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		linearLayout = (RelativeLayout) findViewById(R.id.ll_main_splash);

		// ����ȫ����ʾ
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);;

		// ���ý���
		AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
		alphaAnimation.setDuration(2000);
		linearLayout.setAnimation(alphaAnimation);

		entryNewActivity();

	}

	// ��ת���µĽ���
	private void entryNewActivity() {
		final Message message = new Message();

		new Thread() {

			@Override
			public void run() {
				super.run();
				try {
					sleep(2000);
					String token = Config.getToken(SplashActivity.this);
					if (token != null) {
						//
						message.what = MSG_LOGIN;
					} else {
						message.what = MSG_UNLOGIN;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					handler.sendMessage(message);
					finish();
				}
			}

		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ���ý���
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
		alphaAnimation.setDuration(1000);
		linearLayout.setAnimation(alphaAnimation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
