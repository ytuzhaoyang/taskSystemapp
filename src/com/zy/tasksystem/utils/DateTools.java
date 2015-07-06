package com.zy.tasksystem.utils;

import java.util.Calendar;
import java.util.Date;

public class DateTools {

	public static String dateFormat(String str){
		if(str == null || "".equals(str)){
			return "";
		}
		String time ="";
		String mon = "";
		String da ="";
		String hou = "";
		String min = "";
		
		long l = Long.parseLong(str);
		Date date = new Date(l);
		int year = date.getYear()+1900;
		int month = date.getMonth()+1;
		if(month<10){
			mon = "0"+month;
		}else{
			mon = month+"";
		}
		int day = date.getDate();
		System.out.println("?????"+day);
		if(day<10){
			da = "0"+day;
		}else{
			da = day+"";
		}
		int hour = date.getHours();
		if(hour<10){
			hou = "0"+hour;
		}else{
			hou = hour+"";
		}
		int minut = date.getMinutes();
		if(minut<10){
			min= "0"+minut;
		}else{
			min = minut+"";
		}
		
		Calendar calendar = Calendar.getInstance();
		if(calendar.get(Calendar.YEAR)-year!=0){
			time = year+"年"+mon+"月"+da+"日"+hou+":"+min;
			
		}else if(month-calendar.get(Calendar.MONTH)!=1){
			time = mon+"月"+da+"日"+hou+":"+min;
		}else if(calendar.get(Calendar.DATE)-day>2){
			time = mon+"月"+da+"日"+hou+":"+min;
		}else if(calendar.get(Calendar.DATE)-day==2){
			time = "前天"+hou+":"+min;
		}else if(calendar.get(Calendar.DATE)-day==1){
			time = "昨天"+hou+":"+min;
		}else if(calendar.get(Calendar.DATE)-day==0){
			time = "今天"+hou+":"+min;
		}
		
		return time;
	}
}
