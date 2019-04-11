package com.jeycorp.impressletters.utill;

import android.content.Context;

import com.jeycorp.impressletters.define.ValueDefine;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeUtil {
	private Context context;
	private String year;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private String seconds;
	
	
	public TimeUtil(Context context) {
		this.context = context;	
	}
	
	public void setTimeStampConvertString(String date){
		String[] dateArray = date.split(" ");
		String[] dayArray = dateArray[0].split("-");
		String[] timeArray = dateArray[1].split(":");
		
		year = dayArray[0];
		month = dayArray[1];
		day = dayArray[2];
		
		hour = timeArray[0];
		minute = timeArray[1];
		seconds = timeArray[2];	
	}
	
	public String getYear(){
		return year;
	}
	public String getMonth(){
		return month;
	}
	public String getDay(){
		return day;
	}
	public String getHour(){
		int h = Integer.parseInt(hour);
		String apm = "오전";
		if(h>12){
			apm = "오후";
		}
		h = h%12;
		String t = String.valueOf(h);
		if(h<10){
			t = "0"+t;
		}

		String time = apm+String.valueOf(t);
		return time;
	}
	public String getMinute(){
		return minute;
	}
	public String getSeconds(){
		return seconds;
	}

	public String getToDate(){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String strMonth = setOverTime(month);
		String strDay = setOverTime(day);

		return year+"-"+strMonth+"-"+strDay;
	}
	public String getNextDate(int value){
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, value);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String strMonth = setOverTime(month);
		String strDay = setOverTime(day);

		String strToday = strMonth + "월 " + strDay + "일";
		String[] week = { "일", "월", "화", "수", "목", "금", "토" };

		//return strToday + "\n"+"("+week[cal.get(Calendar.DAY_OF_WEEK) - 1]+"요일)";
		return strToday;
	}

	public String getTodaytWeek(){
		Calendar oCalendar = Calendar.getInstance( );
		final String[] week = { "일", "월", "화", "수", "목", "금", "토" };

		return week[oCalendar.get(Calendar.DAY_OF_WEEK) - 1];
	}
	public String getFormatTime(Long diffTime) {
		String msg = null;
		if (diffTime < ValueDefine.SEC) {
			// sec
			msg = "방금 전";
		} else if ((diffTime /= ValueDefine.SEC) < ValueDefine.MIN) {
			// min
			msg = diffTime + "분 전";
		} else if ((diffTime /= ValueDefine.MIN) < ValueDefine.HOUR) {
			// hour
			msg = (diffTime) + "시간 전";
		} else if ((diffTime /= ValueDefine.HOUR) < ValueDefine.DAY) {
			// day
			msg = (diffTime) + "일 전";
		} else if ((diffTime /= ValueDefine.DAY) < ValueDefine.MONTH) {
			// day
			msg = (diffTime) + "달 전";
		} else {
			msg = (diffTime) + "년 전";
		}

		return msg;
	}
	public String setOverTime(int time){
		String strTime = String.valueOf(time);
		if(time<10){
			strTime = "0"+strTime;
		}
		return strTime;
	}
	public String getDayAfeterNight(){
		Calendar cal = Calendar.getInstance();
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int month = cal.get(Calendar.MONTH)+1;

		String day = "";

		if(month==4 || month==3 || month==2 || month==1 || month==12 || month==11 || month==10){
			if(h>=6 && h<18 ){
				day = "AFTERNOON";
			}else{
				day = "NIGHT";
			}
		}else{
			if(h>=6 && h<=18 ){
				day = "AFTERNOON";
			}else{
				day = "NIGHT";
			}
		}

		return day;
	}
	

}
