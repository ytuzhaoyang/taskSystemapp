package com.zy.tasksystem.ui;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;
import com.zy.tasksystem.BaseActivity;
import com.zy.tasksystem.Config;
import com.zy.tasksystem.R;
import com.zy.tasksystem.adapter.CommentAdapter;
import com.zy.tasksystem.net.LoadComments;
import com.zy.tasksystem.net.PubComment;
import com.zy.tasksystem.net.TakeCare;
import com.zy.tasksystem.po.CommentBean;
import com.zy.tasksystem.po.ReplyBean;
import com.zy.tasksystem.utils.MakeMD5;

public class TaskDetailActivity extends BaseActivity implements OnClickListener {

	private Button back_to_home, add_expression, pub_comment;
	private TextView tv_taskContent;
	private TextView tv_pubName;
	private TextView tv_pubTime;
	private ImageView iv_pubGender;
	private TextView tv_pubSchool;
	private ImageView iv_detail_comment, iv_detail_care;
	private SmartImageView iv_pubIcon;
	private int taskId;
	private boolean isReply; // 是否是回复
	private int position; // 记录回复评论的索引
	private NoScrollListView lv_taskdetail_comments;
	private List<CommentBean> list;
	private CommentAdapter adapter;
	private String token;
	private EditText comment_content;
	private LinearLayout ll_commentLine, ll_edit_comment;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.layout_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title2);

		sp = getSharedPreferences(Config.APP_ID, Context.MODE_PRIVATE);
		Intent intent = getIntent();
		token = intent.getStringExtra(Config.KEY_TOKEN);
		taskId = intent.getIntExtra(Config.KEY_TASK_ID, 0);

		String pubIcon = intent.getStringExtra(Config.KEY_PUB_ICON);
		String pubName = intent.getStringExtra(Config.KEY_PUB_NAME);
		String pubGender = intent.getStringExtra(Config.KEY_PUB_GENDER);
		String pubSchool = intent.getStringExtra(Config.KEY_PUB_SCHOOL);
		String pubTime = intent.getStringExtra(Config.KEY_PUB_TIME);
		String taskContent = intent.getStringExtra(Config.KEY_TASK_CONTENT);

		iv_pubIcon = (SmartImageView) findViewById(R.id.iv_home_pub_icon);
		tv_pubName = (TextView) findViewById(R.id.tv_home_pub_name);
		tv_pubTime = (TextView) findViewById(R.id.tv_home_pub_time);
		iv_pubGender = (ImageView) findViewById(R.id.iv_home_pub_gender);
		tv_pubSchool = (TextView) findViewById(R.id.tv_home_pub_campus);
		tv_taskContent = (TextView) findViewById(R.id.tv_home_pub_content);

		ll_commentLine = (LinearLayout) findViewById(R.id.ll_commentLine);
		ll_edit_comment = (LinearLayout) findViewById(R.id.ll_detail_edit_comment);

		iv_detail_comment = (ImageView) findViewById(R.id.iv_detail_comment);
		iv_detail_care = (ImageView) findViewById(R.id.iv_detail_care);

		iv_detail_comment.setOnClickListener(this);
		iv_detail_care.setOnClickListener(this);

		back_to_home = (Button) findViewById(R.id.title_back);
		add_expression = (Button) findViewById(R.id.but_taskdetail_add_expression);
		pub_comment = (Button) findViewById(R.id.but_taskdetail_pub_comment);
		comment_content = (EditText) findViewById(R.id.et_taskdetail_comment);

		iv_pubIcon.setImageUrl(Config.SERVER_IMAGE_URL + pubIcon);
		tv_pubName.setText(pubName);
		tv_pubTime.setText(pubTime);
		if ("male".equals(pubGender)) {
			iv_pubGender.setImageResource(R.drawable.v2male);
		} else {
			iv_pubGender.setImageResource(R.drawable.v2female);
		}

		tv_pubSchool.setText(pubSchool);

		tv_taskContent.setText(taskContent);

		back_to_home.setOnClickListener(this);
		add_expression.setOnClickListener(this);
		pub_comment.setOnClickListener(this);

		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.layout_comment_cell_v2, null);
		lv_taskdetail_comments = (NoScrollListView) findViewById(R.id.lv_taskdetail_comments);
		onLoadComments();
		adapter = new CommentAdapter(TaskDetailActivity.this, handler);
		lv_taskdetail_comments.setAdapter(adapter);

	}

	private void onLoadComments() {
		final ProgressDialog pd = ProgressDialog.show(TaskDetailActivity.this,
				getResources().getString(R.string.connecting), getResources()
						.getString(R.string.connecting_for_wait));
		new LoadComments(Config.getPhoneNum(TaskDetailActivity.this),
				Config.KEY_TOKEN, 1, 10, taskId,
				new LoadComments.SuccessCallback() {

					@Override
					public void onSuccess(int page, int pagesize,
							List<CommentBean> comments) {
						// comments代表第一层的评论
						List<CommentBean> beans = new ArrayList<CommentBean>();
						pd.dismiss();
						adapter.clearAdapter();
						list = comments;
						for (int i = 0; i < comments.size(); i++) {
							List<ReplyBean> replylist = new ArrayList<ReplyBean>();
							for (int j = 0; j < comments.size(); j++) {
								if (comments.get(i).getCommentId() == comments
										.get(j).getReplyId()) {
									ReplyBean bean = new ReplyBean();
									bean.setId(comments.get(i).getCommentId());
									bean.setCommentAccount(comments.get(i)
											.getPubPhone());
									bean.setCommentNickname(comments.get(i)
											.getPubName());
									bean.setReplyAccount(comments.get(j)
											.getPubPhone());
									bean.setReplyNickname(comments.get(j)
											.getPubName());
									bean.setReplyContent(comments.get(j)
											.getCommentContent());
									replylist.add(bean);

									continue;
								}
							}
							comments.get(i).setReplyList(replylist);
							if (comments.get(i).getReplyId() == 0) {
								beans.add(comments.get(i));
							}
						}
						adapter.addAll(beans);
					}
				}, new LoadComments.FailCallback() {

					@Override
					public void onFail() {
						pd.dismiss();
						Toast.makeText(TaskDetailActivity.this,
								R.string.fail_to_load_comments,
								Toast.LENGTH_SHORT).show();
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.but_taskdetail_pub_comment:
			// 发布评论
			if (!isReply) {
				pubComment(0);
				ll_edit_comment.setVisibility(View.GONE);
			} else {
				// 回复
				CommentBean commentBean = list.get(position);
				System.out.println(position + "??????"
						+ commentBean.getCommentContent());
				ReplyBean bean = new ReplyBean(sp.getString(
						Config.KEY_PHONE_MD5, ""), sp.getString(
						Config.KEY_USER_NAME, ""), commentBean.getPubPhone(),
						commentBean.getPubName(), comment_content.getText()
								.toString());
				adapter.getReplyComment(bean, position);
				adapter.notifyDataSetChanged();
				pubComment(commentBean.getCommentId());
				ll_edit_comment.setVisibility(View.GONE);
			}
			break;
		case R.id.but_taskdetail_add_expression:

			break;
		case R.id.title_back:
			Intent intent = new Intent(TaskDetailActivity.this,
					HomeActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.tran_pre_enter,
					R.anim.tran_pre_out);
			break;
		case R.id.iv_detail_comment:
			if (!"login_token".equals(token)) {
				Toast.makeText(TaskDetailActivity.this, R.string.please_login,
						Toast.LENGTH_SHORT).show();
				return;
			}
			isReply = false;
			ll_commentLine.setVisibility(View.VISIBLE);
			ll_edit_comment.setVisibility(View.VISIBLE);
			onFocusChange(true);
			break;

		case R.id.iv_detail_care:
			takeCare();
			break;
		default:
			break;
		}
	}

	// 加关注
	private void takeCare() {
		if (!"login_token".equals(token)) {
			Toast.makeText(TaskDetailActivity.this, R.string.please_login,
					Toast.LENGTH_SHORT).show();
			return;
		}
		new TakeCare(token, MakeMD5.md5(Config
				.getPhoneNum(TaskDetailActivity.this)), taskId,
				new TakeCare.SuccessCallback() {

					public void onSuccess() {
						Toast.makeText(TaskDetailActivity.this,
								R.string.success_to_care, Toast.LENGTH_SHORT)
								.show();
					}
				}, new TakeCare.FailCallback() {

					public void onFail(int errorCode) {

						switch (errorCode) {
						case Config.RESULT_STATUS_TOKEN_INVALID:
							Toast.makeText(TaskDetailActivity.this,
									R.string.please_login, Toast.LENGTH_SHORT)
									.show();
							break;
						case Config.RESULT_NO_ANY:
							Toast.makeText(TaskDetailActivity.this,
									R.string.already_care, Toast.LENGTH_SHORT)
									.show();
							break;
						default:
							Toast.makeText(TaskDetailActivity.this,
									R.string.fail_to_pub_care,
									Toast.LENGTH_SHORT).show();
							break;
						}
					}
				});
	}

	// 发布评论
	private void pubComment(int replyId) {

		new PubComment(
				MakeMD5.md5(Config.getPhoneNum(TaskDetailActivity.this)),
				token, comment_content.getText().toString(), taskId, replyId,
				new PubComment.SuccessCallback() {

					@Override
					public void onSuccess() {
						comment_content.setText("");
						ll_edit_comment.setVisibility(View.GONE);
						CommentBean bean = new CommentBean();
						bean.setCommentContent(comment_content.getText()
								.toString());
						bean.setPubGender(sp
								.getString(Config.KEY_USER_ICON, ""));
						bean.setPubIcon(sp.getString(Config.KEY_USER_ICON, ""));
						bean.setPubName(sp.getString(Config.KEY_USER_NAME, ""));
						bean.setPubPhone(sp.getString(Config.KEY_PHONE_MD5, ""));
						bean.setPubTime("ooo");
						bean.setReplyId(0);
						Toast.makeText(TaskDetailActivity.this,
								R.string.success_to_pub_comment,
								Toast.LENGTH_SHORT).show();
						// 重新加载评论列表
						onLoadComments();
					}
				}, new PubComment.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						if (errorCode == Config.RESULT_STATUS_TOKEN_INVALID) {
							// token过期，提醒用户重新登录
							Toast.makeText(TaskDetailActivity.this,
									R.string.please_login, Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(TaskDetailActivity.this,
									R.string.fail_to_pub_comment,
									Toast.LENGTH_SHORT).show();
						}

					}
				});
	}

	/**
	 * 显示或隐藏输入法
	 */
	private void onFocusChange(boolean hasFocus) {
		final boolean isFocus = hasFocus;
		(new Handler()).postDelayed(new Runnable() {
			public void run() {
				InputMethodManager imm = (InputMethodManager) comment_content
						.getContext().getSystemService(INPUT_METHOD_SERVICE);
				if (isFocus) {
					// 显示输入法
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					comment_content.setFocusable(true);
					comment_content.setFocusableInTouchMode(true);
					comment_content.requestFocus();
				} else {
					// 隐藏输入法
					imm.hideSoftInputFromWindow(
							comment_content.getWindowToken(), 0);
				}
			}
		}, 100);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// 判断控件是否显示
		if (ll_commentLine.getVisibility() == View.VISIBLE) {
			ll_commentLine.setVisibility(View.VISIBLE);
			ll_edit_comment.setVisibility(View.VISIBLE);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (!"login_token".equals(token)) {
				Toast.makeText(TaskDetailActivity.this, R.string.please_login,
						Toast.LENGTH_SHORT).show();
				return;
			}
			switch (msg.what) {
			case 10:
				isReply = true;
				comment_content
						.setHint("回复:" + list.get(position).getPubName());
				position = (Integer) msg.obj;
				ll_commentLine.setVisibility(View.VISIBLE);
				ll_edit_comment.setVisibility(View.VISIBLE);
				onFocusChange(true);
				break;
			case 11:
				isReply = true;
				position = (Integer) msg.obj;
				ll_commentLine.setVisibility(View.VISIBLE);
				ll_edit_comment.setVisibility(View.VISIBLE);
				onFocusChange(true);
				break;
			default:
				break;
			}

		}
	};

	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		Intent intent = new Intent(TaskDetailActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_out);
		return isReply;

	};
}
