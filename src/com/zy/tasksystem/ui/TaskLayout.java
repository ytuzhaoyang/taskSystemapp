package com.zy.tasksystem.ui;

import com.zy.tasksystem.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskLayout extends LinearLayout {

	private ImageView iv_icon,iv_sex;
	private TextView tv_name,tv_time,tv_content,tv_criticisms,tv_cares;
	
	public TaskLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public TaskLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public TaskLayout(Context context) {
		super(context);
		initView();
	}

	public void initView() {
//		View view = View.inflate(getContext(), R.layout.layout_task, null);
//		this.addView(view);
		View view = View.inflate(getContext(), R.layout.layout_task, this);
		
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon_home);
	    iv_sex = (ImageView) view.findViewById(R.id.iv_sex_home);
	    tv_name = (TextView) view.findViewById(R.id.tv_name_home);
	    tv_time = (TextView) view.findViewById(R.id.tv_time_home);
	    tv_content = (TextView) view.findViewById(R.id.tv_content_home);
	    tv_criticisms = (TextView) view.findViewById(R.id.tv_criticism_home);
	    tv_cares = (TextView) view.findViewById(R.id.tv_cares_home);
	}

	public void setIv_icon(Drawable drawable) {
		iv_icon.setImageDrawable(drawable);
	}

	public void setIv_sex(Drawable drawable) {
		this.iv_sex.setImageDrawable(drawable);
	}

	public void setTv_name(String name) {
		this.tv_name.setText(name);
	}

	public void setTv_time(String time) {
		this.tv_time.setText(time);
	}

	public void setTv_content(String content) {
		this.tv_content.setText(content);
	}

	public void setTv_criticisms(String criticisms) {
		this.tv_criticisms.setText(criticisms);
	}

	public void setTv_cares(String cares) {
		this.tv_cares.setText(cares);
	}
	
	

}
