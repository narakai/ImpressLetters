package com.jeycorp.impressletters.utill;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagers {
	SharedPreferences Pref;
	SharedPreferences.Editor edit;
	Context mCon;

	public PreferenceManagers(Context context) {
		mCon = context;
		Pref = mCon.getSharedPreferences("IMPRESSLETTERS", 0);
	}

	//카테고리시퀀스


	// 아이디
	public void setUid(String uid) {
		edit = Pref.edit();
		edit.putString("uid", uid);
		edit.commit();
	}
	public String getUid() {
		String uid = Pref.getString("uid", "");
		return uid;
	}
	//닉네임
	public void setNickname(String name){
		edit = Pref.edit();
		edit.putString("nickname",name);
		edit.commit();
	}
	public String getNickname(){
		String name = Pref.getString("nickname","");
		return name;
	}

	//앱 링크
	public void setLinkUrl(String url){
		edit = Pref.edit();
		edit.putString("linkUrl", url);
		edit.commit();
	}
	public String getLinkUrl(){
		String url = Pref.getString("linkUrl", "");
		return url;
	}
	//앱 종료 유무
	public boolean isAppExcute(){
		boolean finish = Pref.getBoolean("appExcute", false);
		return finish;
	}
	public void setAppExcute(boolean finish){
		edit = Pref.edit();
		edit.putBoolean("appExcute", finish);
		edit.commit();
	}
	// 푸시 카운트
	public void setPushCount(int count){
		edit = Pref.edit();
		edit.putInt("pushCount", count);
		edit.commit();
	}
	public int getPushCount(){
		int count = Pref.getInt("pushCount", 0);
		return count;
	}
	/***************************************날씨******************************************/
	//날씨 ON,OFF
	public void setWeather(boolean is){
		edit = Pref.edit();
		edit.putBoolean("isWeather",is);
		edit.commit();
	}
	public boolean isWeather(){
		boolean is = Pref.getBoolean("isWeather",true);
		return is;
	}
	//위도 경도
	public void setLatitude(double latitude){
		edit = Pref.edit();
		edit.putLong("latitude",Double.doubleToLongBits(latitude));
		edit.commit();
	}
	public double getLatitude(){
		double latitude = Double.longBitsToDouble(Pref.getLong("latitude",0));
		return latitude;
	}

	public void setLongitude(double longiutde){
		edit = Pref.edit();
		edit.putLong("longitude",Double.doubleToLongBits(longiutde));
		edit.commit();
	}
	public double getLongitude(){
		double longitude = Double.longBitsToDouble(Pref.getLong("longitude",0));
		return longitude;
	}
	//지역 코드
	public void setLocationCode(String code){
		edit = Pref.edit();
		edit.putString("locationCode", code);
		edit.commit();
	}
	public String getLocationCode(){
		String code = Pref.getString("locationCode", "");
		return code;
	}
	//지역 명
	public void setLocationCurrent(String location){
		edit = Pref.edit();
		edit.putString("locationCurrent", location);
		edit.commit();
	}
	public String getLocationCurrent(){
		String location = Pref.getString("locationCurrent", "");
		return location;
	}
	//현제온도
	public void setCurrentTemp(String temp){
		edit = Pref.edit();
		edit.putString("currentTemp",temp);
		edit.commit();
	}
	public String getCurrentTemp(){
		String temp = Pref.getString("currentTemp","GPS를 켜주세요.");
		return temp;
	}
	//최대,최소온도
	public void setMaxMinTemp(String temp){
		edit = Pref.edit();
		edit.putString("maxminTemp",temp);
		edit.commit();
	}
	public String getMaxMinTemp(){
		String temp = Pref.getString("maxminTemp","");
		return temp;
	}
	public void setMaxTemp(String temp){
		edit = Pref.edit();
		edit.putString("maxTemp",temp);
		edit.commit();
	}
	public String getMaxTemp(){
		String temp = Pref.getString("maxTemp","");
		return temp;
	}

	public void setMinTemp(String temp){
		edit = Pref.edit();
		edit.putString("minTemp",temp);
		edit.commit();
	}
	public String getMinTemp(){
		String temp = Pref.getString("minTemp","");
		return temp;
	}
	//이미지 코드
	public void setWeatherCode(int code){
		edit = Pref.edit();
		edit.putInt("weatherCode",code);
		edit.commit();
	}
	public int getWeatherCode(){
		int code = Pref.getInt("weatherCode",0);
		return code;
	}
	//난수
	public void setRandom(int rand){
		edit = Pref.edit();
		edit.putInt("random",rand);
		edit.commit();
	}
	public int getRandom(){
		int rand = Pref.getInt("random",0);
		return rand;
	}

	/****************************************주간날씨***************************************/
	public void setWeatherCode01(int code){
		edit = Pref.edit();
		edit.putInt("weatherCode01",code);
		edit.commit();
	}
	public int getWeatherCode01(){
		int code = Pref.getInt("weatherCode01",0);
		return code;
	}
	public void setWeatherHighTemp01(String temp){
		edit = Pref.edit();
		edit.putString("weatherHighTemp01",temp);
		edit.commit();
	}
	public String getWeatherHighTemp01(){
		String temp = Pref.getString("weatherHighTemp01","");
		return temp;
	}
	public void setWeatherLowTemp01(String temp){
		edit = Pref.edit();
		edit.putString("weatherLowTemp01",temp);
		edit.commit();
	}
	public String getWeatherLowTemp01(){
		String temp = Pref.getString("weatherLowTemp01","");
		return temp;
	}


	public void setWeatherCode02(int code){
		edit = Pref.edit();
		edit.putInt("weatherCode02",code);
		edit.commit();
	}
	public int getWeatherCode02(){
		int code = Pref.getInt("weatherCode02",0);
		return code;
	}
	public void setWeatherHighTemp02(String temp){
		edit = Pref.edit();
		edit.putString("weatherHighTemp02",temp);
		edit.commit();
	}
	public String getWeatherHighTemp02(){
		String temp = Pref.getString("weatherHighTemp02","");
		return temp;
	}
	public void setWeatherLowTemp02(String temp){
		edit = Pref.edit();
		edit.putString("weatherLowTemp02",temp);
		edit.commit();
	}
	public String getWeatherLowTemp02(){
		String temp = Pref.getString("weatherLowTemp02","");
		return temp;
	}


	public void setWeatherCode03(int code){
		edit = Pref.edit();
		edit.putInt("weatherCode03",code);
		edit.commit();
	}
	public int getWeatherCode03(){
		int code = Pref.getInt("weatherCode03",0);
		return code;
	}
	public void setWeatherHighTemp03(String temp){
		edit = Pref.edit();
		edit.putString("weatherHighTemp03",temp);
		edit.commit();
	}
	public String getWeatherHighTemp03(){
		String temp = Pref.getString("weatherHighTemp03","");
		return temp;
	}
	public void setWeatherLowTemp03(String temp){
		edit = Pref.edit();
		edit.putString("weatherLowTemp03",temp);
		edit.commit();
	}
	public String getWeatherLowTemp03(){
		String temp = Pref.getString("weatherLowTemp03","");
		return temp;
	}
	/***************************************광고******************************************/

	public void setAdCount(int count){
		edit = Pref.edit();
		edit.putInt("adCount",count);
		edit.commit();
	}
	public int getAdCount(){
		int count = Pref.getInt("adCount",0);
		return count;
	}

	/***********************************가이드 팝업*****************************************/

	public void setGuide(boolean is){
		edit = Pref.edit();
		edit.putBoolean("guide",is);
		edit.commit();
	}

	public boolean isGuide(){
		boolean is = Pref.getBoolean("guide",false);
		return  is;
	}

	/************************************프로필 사진 선택***********************************/

	public void setProfileImagePosition(int number){
		edit = Pref.edit();
		edit.putInt("profileImagePosition",number);
		edit.commit();
	}
	public int getProfileImagePosition(){
		int position = Pref.getInt("profileImagePosition",100);
		return position;
	}

	
	
	
	

}
