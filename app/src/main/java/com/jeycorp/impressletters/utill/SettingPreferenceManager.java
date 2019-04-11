package com.jeycorp.impressletters.utill;


import android.content.Context;
import android.content.SharedPreferences;

public class SettingPreferenceManager {
    SharedPreferences Pref;
    SharedPreferences.Editor edit;
    Context mCon;

    public SettingPreferenceManager(Context context){
        mCon = context;
        Pref = mCon.getSharedPreferences("IMPRESSLETTERS", 0);
    }

    public void setIntroAd(String add){
        edit =Pref.edit();
        edit.putString("introAd",add);
        edit.commit();
    }
    public String getIntroAd(){
        String add = Pref.getString("introAd","ON");
        return add;
    }

    public void setFinishAd(String add){
        edit = Pref.edit();
        edit.putString("finishAd",add);
        edit.commit();
    }
    public String getFinishAd(){
        String add = Pref.getString("finishAd","ON");
        return add;
    }
    public void setPopFinishAd(String add){
        edit = Pref.edit();
        edit.putString("finishPopAd",add);
        edit.commit();
    }
    public String getPopFinishAd(){
        String add = Pref.getString("finishPopAd","ON");
        return add;
    }
    public void setPopFinishText(String text){
        edit = Pref.edit();
        edit.putString("finishPopText",text);
        edit.commit();
    }
    public String getPopFinishText(){
        String add = Pref.getString("finishPopText","ON");
        return add;
    }

    public void setViewAd(String add){
        edit = Pref.edit();
        edit.putString("viewAdd",add);
        edit.commit();
    }
    public String getViewAd(){
        String add = Pref.getString("viewAdd","ON");
        return add;
    }



}
