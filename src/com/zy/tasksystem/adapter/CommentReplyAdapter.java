package com.zy.tasksystem.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zy.tasksystem.R;
import com.zy.tasksystem.po.ReplyBean;

public class CommentReplyAdapter extends BaseAdapter {

	private Context context;
	private Handler handler;
	private List<ReplyBean> list = new ArrayList<ReplyBean>();

	public CommentReplyAdapter(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReplyBean bean = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(
					R.layout.layout_comment_reply, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_reply_comment1 = (TextView) view.findViewById(R.id.tv_reply_name);
			viewHolder.tv_reply_comment2 = (TextView) view
					.findViewById(R.id.tv_comment_Nickname);
			viewHolder.tv_reply_commentContent = (TextView) view
					.findViewById(R.id.tv_reply_Content);
			viewHolder.ll_comment = (LinearLayout)view.findViewById(R.id.ll_comment);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.tv_reply_comment1.setText(bean.getReplyNickname());
		viewHolder.tv_reply_comment2.setText(bean.getCommentNickname()+":");
		viewHolder.tv_reply_commentContent.setText(bean.getReplyContent());
		TextviewClickListener listener = new TextviewClickListener(position);
		viewHolder.ll_comment.setOnClickListener(listener);
		return view;
	}
     
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ReplyBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public Context getContext() {
		return context;
	}

	class ViewHolder {
		TextView tv_reply_comment1;
		TextView tv_reply;
		TextView tv_reply_comment2;
		TextView tv_reply_commentContent;
		LinearLayout ll_comment;
	}

	public void addAll(List<ReplyBean> date) {
		this.list.addAll(date);
		notifyDataSetChanged();
	}

	public void clear() {
		this.list.clear();
		notifyDataSetChanged();
	}
	
	/**
	 * 事件点击监听器
	 */
	private final class TextviewClickListener implements OnClickListener{
		private int position;
		public TextviewClickListener(int position){
			this.position = position;
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ll_comment:
				handler.sendMessage(handler.obtainMessage(11, position));
				break;
			}
		}
	}
}
