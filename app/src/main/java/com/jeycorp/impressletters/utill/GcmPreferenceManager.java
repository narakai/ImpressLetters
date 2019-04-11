package com.jeycorp.impressletters.utill;


import android.content.Context;
import android.content.SharedPreferences;

public class GcmPreferenceManager {
    SharedPreferences Pref;
    SharedPreferences.Editor edit;
    Context mCon;

   public GcmPreferenceManager(Context context){
       mCon = context;
       Pref = mCon.getSharedPreferences("IMPRESSLETTERS", 0);
   }

    public void setGcmId(String id){
        edit =Pref.edit();
        edit.putString("GcmId",id);
        edit.commit();
    }
    public String getGcmId(){
        String id = Pref.getString("GcmId","");
        return id;
    }

    public void setPush(boolean is){
        edit = Pref.edit();
        edit.putBoolean("isPush",is);
        edit.commit();
    }
    public boolean isPush(){
        boolean is = Pref.getBoolean("isPush",true);
        return is;
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


}
