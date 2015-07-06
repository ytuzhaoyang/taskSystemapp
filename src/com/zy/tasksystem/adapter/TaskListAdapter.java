package com.zy.tasksystem.adapter;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.image.SmartImageView;
import com.zy.tasksystem.R;
import com.zy.tasksystem.po.TaskBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListAdapter extends BaseAdapter {

	private List<TaskBean> lists = new ArrayList<TaskBean>();
	private Context context;

	public TaskListAdapter(Context context) {
		this.context = context;
	}

	public int getCount() {
		return lists.size();
	}

	public TaskBean getItem(int position) {
		return lists.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TaskBean bean = getItem(position);
		View view = null;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext(context)).inflate(
					R.layout.layout_task_cell, null);
			viewHolder = new ViewHolder();
			viewHolder.iv_pubIcon = (SmartImageView) view
					.findViewById(R.id.iv_home_pub_icon);
			viewHolder.tv_pubName = (TextView) view
					.findViewById(R.id.tv_home_pub_name);
			viewHolder.tv_pubTime = (TextView) view
					.findViewById(R.id.tv_home_pub_time);
			viewHolder.iv_pubGender = (ImageView) view
					.findViewById(R.id.iv_home_pub_gender);
			viewHolder.tv_pubSchool = (TextView) view
					.findViewById(R.id.tv_home_pub_campus);
			viewHolder.tv_taskContent = (TextView) view
					.findViewById(R.id.tv_home_pub_content);
			viewHolder.tv_commentTimes = (TextView) view
					.findViewById(R.id.tv_home_coment_times);
			viewHolder.tv_attens = (TextView) view
					.findViewById(R.id.tv_home_attens_time);

			view.setTag(viewHolder); // 将ViewHolder存储在View中
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		String s =bean.getPubIcon();
			viewHolder.iv_pubIcon.setImageUrl(com.zy.tasksystem.Config.SERVER_IMAGE_URL+s);
	
		viewHolder.tv_pubName.setText(bean.getPubName());
		viewHolder.tv_pubTime.setText(bean.getPubTime());
		if("male".equals(bean.getPubGender())){
		viewHolder.iv_pubGender.setImageResource(R.drawable.v2male);
		}else{
			viewHolder.iv_pubGender.setImageResource(R.drawable.v2female);
		}
		viewHolder.tv_pubSchool.setText(bean.getPubSchool());
		viewHolder.tv_taskContent.setText(bean.getTaskContent());
		viewHolder.tv_commentTimes.setText(bean.getCommentTimes()+"条评论");
		viewHolder.tv_attens.setText(bean.getAttens()+"人关注");
		return view;
	}

	public Context getContext(Context context) {
		return context;
	}

	public void addAll(List<TaskBean> lists) {
		this.lists.addAll(lists);
		notifyDataSetChanged(); // 数据更新时通知更新
	}

	public void removeAll() {
		this.lists.clear();
		notifyDataSetChanged();
	}

	private static class ViewHolder {
		TextView tv_taskContent;
		TextView tv_pubName;
		TextView tv_pubTime;
		ImageView iv_pubGender;
		TextView tv_pubSchool;
		SmartImageView iv_pubIcon;
		TextView tv_commentTimes;
		TextView tv_attens;
	}

}
