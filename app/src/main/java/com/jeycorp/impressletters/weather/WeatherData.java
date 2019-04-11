package com.jeycorp.impressletters.weather;

public class WeatherData {
	private int nowTemp;
	private int nowCode;
	private String nowText;
	
	private int todayLow;
	private int todayHigh;
	private int todayCode;
	private String todayText;
	private StringBuffer locationView;
	
	public WeatherData() {
		this.locationView = new StringBuffer();
	}
	
	public void setLocationView(String addr) {
		this.locationView.append(addr);
	}
	
	public String getLocationView() {
		return this.locationView.toString();
	}
	
	public void setNowTemp(String temp) {
		this.nowTemp = Integer.parseInt(temp);
		return;
	}
	
	public int getNowTemp() {
		return this.nowTemp;
	}
	
	public void setNowCode(String code) {
		this.nowCode = Integer.parseInt(code);
		return;
	}
	
	public int getNowCode() {
		return this.nowCode;
	}
	
	public void setNowText(String text) {
		this.nowText = text;
		return;
	}
	
	public String getNowText() {
		return this.nowText;
	}
	
	
	
	
	public void setTodayLow(String low) {
		this.todayLow = Integer.parseInt(low);
		return;
	}
	
	public int getTodayLow() {
		return this.todayLow;
	}
	
	public void setTodayHigh(String high) {
		this.todayHigh = Integer.parseInt(high);
		return;
	}
	
	public int getTodayHigh() {
		return this.todayHigh;
	}
	
	
	public void setTodayCode(String code) {
		this.todayCode = Integer.parseInt(code);
		return;
	}
	
	public int getTodayCode() {
		return this.todayCode;
	}
	
	public void setTodayText(String text) {
		this.todayText = text;
		return;
	}
	
	public String getTodayText() {
		return this.todayText;
	}
	
	public int getTempMP(int temp) {
		if(temp < 0) {
			return 0;
		} else if(temp == 0) {
			return 1;
		} else {
			return 2;
		}
	}
}
