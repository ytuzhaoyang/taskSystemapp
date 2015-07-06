package com.zy.tasksystem.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.zy.tasksystem.Config;
import com.zy.tasksystem.R;
import com.zy.tasksystem.po.CommentBean;
import com.zy.tasksystem.po.ReplyBean;
import com.zy.tasksystem.ui.NoScrollListView;

public class CommentAdapter extends BaseAdapter {

	private Context context;
	private Handler handler;
	private List<CommentBean> comments = new ArrayList<CommentBean>();
	
	public CommentAdapter(Context context,Handler handler) {
           this.context = context;
           this.handler = handler;
	}
	
	
	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public CommentBean getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        CommentBean comment = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
        	view = LayoutInflater.from(getContext(context)).inflate(R.layout.layout_comment_cell_v2, null);
        	viewHolder = new ViewHolder();
        	viewHolder.iv_pubIcon = (SmartImageView) view.findViewById(R.id.iv_detail_comment_icon);
        	viewHolder.tv_pubName = (TextView) view.findViewById(R.id.tv_detail_comment_name);
        	viewHolder.tv_pubTime = (TextView) view.findViewById(R.id.tv_detail_comment_time);
        	viewHolder.tv_cell_reply = (TextView) view.findViewById(R.id.tv_cell_reply);
//        	viewHolder.iv_pubGender = (ImageView) view.findViewById(R.id.iv_detail_comment_gender);
//        	viewHolder.tv_pubSchool = (TextView) view.findViewById(R.id.tv_detail_comment_campus);
        	viewHolder.tv_commentContent = (TextView) view.findViewById(R.id.tv_detail_comment_content);
        	viewHolder.replyList = (NoScrollListView) view.findViewById(R.id.lv_taskdetail_reply_comments);
        	view.setTag(viewHolder);
        }else{
        	view = convertView;
        	viewHolder = (ViewHolder) view.getTag();
        }
        
//        if("male".equals(comment.getPubGender())){
//        	viewHolder.iv_pubGender.setImageResource(R.drawable.v2male);
//        }else{
//        	viewHolder.iv_pubGender.setImageResource(R.drawable.v2female);
//        }
        
        //if(comment.getPubIcon()==null||comment.getPubIcon().trim()=="")
        
        viewHolder.iv_pubIcon.setImageUrl(Config.SERVER_IMAGE_URL+comment.getPubIcon());
        viewHolder.tv_pubName.setText(comment.getPubName());
        viewHolder.tv_pubTime.setText(comment.getPubTime());
        
       // viewHolder.tv_pubSchool.setText(comment.getPubSchool());
        viewHolder.tv_commentContent.setText(comment.getCommentContent());
        CommentReplyAdapter replyAdapter = new CommentReplyAdapter(context,handler);
        replyAdapter.clear();
        replyAdapter.addAll(comment.getReplyList());
        replyAdapter.notifyDataSetChanged();
        viewHolder.replyList.setAdapter(replyAdapter);
        TextviewClickListener tcl = new TextviewClickListener(position);
        viewHolder.tv_cell_reply.setOnClickListener(tcl);
		return view;
	}

	//存放控件的实例化 
	private static class ViewHolder {
		TextView tv_commentContent;
		TextView tv_pubName;
		TextView tv_pubTime;
//		ImageView iv_pubGender;
//		TextView tv_pubSchool;
		SmartImageView iv_pubIcon;
		NoScrollListView replyList;
		TextView tv_cell_reply;
	}

	public Context getContext(Context context){
		return context;
	}
	
	public void addAll(List<CommentBean> data){
		this.comments.addAll(data);
		notifyDataSetChanged();
	}
	
	public void clearAdapter(){
		this.comments.clear();
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
			case R.id.tv_cell_reply:
				handler.sendMessage(handler.obtainMessage(10, position));
				break;
			}
		}
	}
	
	/**
	 * 获取回复评论
	 */
	public void getReplyComment(ReplyBean bean,int position){
		List<CommentBean> beans = new ArrayList<CommentBean>();
		for(CommentBean c:comments){
			if(c.getReplyId()==0){
				beans.add(c);
			}
		}
		List<ReplyBean> rList = beans.get(position).getReplyList();
		rList.add(rList.size(), bean);
	}
}
