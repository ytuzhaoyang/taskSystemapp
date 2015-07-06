package com.zy.tasksystem.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.zy.tasksystem.BaseActivity;
import com.zy.tasksystem.Config;
import com.zy.tasksystem.R;
import com.zy.tasksystem.net.Regist;
import com.zy.tasksystem.utils.HttpAssist;
import com.zy.tasksystem.utils.MakeMD5;

public class RegistActivity extends BaseActivity {
	/**
	 * 选择文件
	 */
	protected static final int TO_SELECT_PHOTO = 1;
	private static final String TAG = "RegistActivity";
	private Button but_regist_getcode, but_regist_submit;
	private EditText et_regist_code, et_regist_phone, et_regist_pwd;
	private ImageView iv_regist_gender, iv_regist_uploadicon;
	private TextView tv_regist_login_now;
	private SharedPreferences sp;
	int i = 0;
	private String gender = "male";
	private String picPath = null,filename = null;
	private File imageFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_regist);
		
		sp = getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE);

		// but_regist_getcode = (Button) findViewById(R.id.but_regist_getcode);
		et_regist_phone = (EditText) findViewById(R.id.et_regist_phone);
		et_regist_pwd = (EditText) findViewById(R.id.et_regist_pwd);
		et_regist_code = (EditText) findViewById(R.id.et_regist_phone);
		iv_regist_gender = (ImageView) findViewById(R.id.iv_regist_gender);
		tv_regist_login_now = (TextView) findViewById(R.id.tv_regist_login_now);
		but_regist_submit = (Button) findViewById(R.id.but_regist_submit);
		iv_regist_uploadicon = (ImageView) findViewById(R.id.iv_regist_uploadicon);

		iv_regist_uploadicon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegistActivity.this,
						SelectPhotoActivity.class);
				startActivityForResult(intent, TO_SELECT_PHOTO);

			}
		});

		tv_regist_login_now.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegistActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.tran_pre_enter,
						R.anim.tran_pre_out);
			}
		});

		iv_regist_gender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (i % 2 == 0) {
					iv_regist_gender.setImageResource(R.drawable.female);
					gender = "female";
				} else {
					iv_regist_gender.setImageResource(R.drawable.male);
					gender = "male";
				}
				i++;
			}
		});

		// but_regist_getcode.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if(TextUtils.isEmpty(et_regist_code.getText())){
		// Toast.makeText(getApplicationContext(), R.string.phone_not_empty,
		// Toast.LENGTH_LONG).show();
		// return;
		// }else{
		// new GetCode(et_regist_code.getText().toString(), new
		// GetCode.SuccessCallback() {
		//
		// @Override
		// public void onSuccess() {
		// Toast.makeText(RegistActivity.this, R.string.get_code_success,
		// Toast.LENGTH_SHORT).show();
		//
		// }
		// }, new GetCode.FailCallback() {
		//
		// @Override
		// public void onFail() {
		// Toast.makeText(getApplicationContext(), R.string.fail_to_get_code,
		// Toast.LENGTH_LONG).show();
		//
		// }
		// });
		// }
		//
		// }
		// });

		but_regist_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(et_regist_phone.getText())) {
					Toast.makeText(getApplicationContext(),
							R.string.phone_not_empty, Toast.LENGTH_LONG).show();
					return;
				}
				if (TextUtils.isEmpty(et_regist_pwd.getText())) {
					Toast.makeText(getApplicationContext(),
							R.string.pwd_not_empty, Toast.LENGTH_LONG).show();
					return;
				}

				new Regist(gender, MakeMD5.md5(et_regist_phone.getText()
						.toString()), MakeMD5.md5(et_regist_pwd.getText()
						.toString()),filename, new Regist.SuccessCallback() {

					@Override
					public void onSuccess(String token) {
						
						if (filename != null) {
							final File imageFile = new File(
									Environment.getExternalStorageDirectory(), filename);
							new AsyncTask<Void, Void, String>(){

								@Override
								protected String doInBackground(Void... params) {
									HttpAssist.uploadFile(imageFile);
									return null;
								}
								
							}.execute();
						}
						
						Config.setToken(getApplicationContext(), token);
						Config.setPhoneNum(getApplicationContext(),
								et_regist_phone.getText().toString());
						sp.edit().putString(Config.KEY_USER_SEX, gender)
								.commit();
						if(filename!=null){
                        sp.edit().putString(Config.KEY_USER_ICON, filename).commit();
						}else{
							sp.edit().putString(Config.KEY_USER_ICON, "default_icon.png").commit();
						}
						Intent intent = new Intent(RegistActivity.this,
								HomeActivity.class);
						intent.putExtra(Config.KEY_TOKEN, token);
						intent.putExtra(Config.KEY_PHONE_NUM, et_regist_phone
								.getText().toString());
						startActivity(intent);
						finish();
					}
				}, new Regist.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						if (errorCode == 1) {
							Toast.makeText(getApplicationContext(),
									R.string.fail_to_regist, Toast.LENGTH_LONG)
									.show();
							return;
						}
					}
				});

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
			picPath = data.getStringExtra(SelectPhotoActivity.KEY_PHOTO_PATH);
			Bitmap bm = BitmapFactory.decodeFile(picPath);
			Bitmap bitmap = getCroppedRoundBitmap(bm, 60);
			// bitmap = getRoundedBitmap(bitmap);
			iv_regist_uploadicon.setImageBitmap(bitmap);
			filename = UUID.randomUUID().toString() + ".png";

			saveBitmap2file(bitmap, filename);
		}
		

	}

	/**
	 * 获取裁剪后的圆形图片
	 * 
	 * @param radius
	 *            半径
	 */
	public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;  //直径

		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth) {// 高大于宽
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			// 截取正方形图片
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else if (bmpHeight < bmpWidth) {// 宽大于高
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		} else {
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter
				|| squareBitmap.getHeight() != diameter) {
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
					diameter, true);

		} else {
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		// bitmap回收(recycle导致在布局文件XML看不到效果)
		// bmp.recycle();
		// squareBitmap.recycle();
		// scaledSrcBmp.recycle();
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}

	// 图片圆角处理
	public Bitmap getRoundedBitmap(Bitmap mBitmap) {

		// int diameter = radius * 2;
		// 创建新的位图
		Bitmap bgBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
		// 把创建的位图作为画板
		Canvas mCanvas = new Canvas(bgBitmap);

		Paint mPaint = new Paint();
		Rect mRect = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
		RectF mRectF = new RectF(mRect);
		// 设置圆角半径为20
		float roundPx = 15;
		mPaint.setAntiAlias(true);
		// 先绘制圆角矩形
		mCanvas.drawRoundRect(mRectF, roundPx, roundPx, mPaint);

		// 设置图像的叠加模式
		mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// 绘制图像
		mCanvas.drawBitmap(mBitmap, mRect, mRect, mPaint);

		return bgBitmap;
	}

	public Bitmap getScaleBitmap(Bitmap mBitmap) {

		int width = mBitmap.getWidth();
		int height = mBitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(0.35f, 0.35f);
		Bitmap mScaleBitmap = Bitmap.createBitmap(mBitmap, 0, 0, 60, 60,
				matrix, true);

		return mScaleBitmap;
	}

	static boolean saveBitmap2file(Bitmap bmp, String filename) {
		CompressFormat format = Bitmap.CompressFormat.PNG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream("/sdcard/" + filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bmp.compress(format, quality, stream);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(RegistActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.tran_pre_enter,
					R.anim.tran_pre_out);
		}
		return super.onKeyDown(keyCode, event);
	}

}
