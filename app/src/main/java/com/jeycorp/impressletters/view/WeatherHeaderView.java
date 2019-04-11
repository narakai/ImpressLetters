package com.jeycorp.impressletters.view;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeycorp.impressletters.activity.WeatherActivity;
import com.jeycorp.impressletters.fragment.GoodsWriteFragment;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.utill.TimeUtil;
import com.jeycorp.impressletters.weather.WeatherInfo;
import com.jeycorp.impressletters.R;

public class WeatherHeaderView {
    private Activity activity;
    private GoodsWriteFragment fragment;
    private View view;
    int dustGrade;
    int dustValue;

    public WeatherHeaderView(Activity activity,GoodsWriteFragment fragment,View view,int dustGrade, int dustValue){
        this.activity = activity;
        this.fragment = fragment;
        this.view = view;
        this.dustGrade = dustGrade;
        this.dustValue = dustValue;
    }
    public void getView(String weatherWord){
        TextView txtCurrentTemp = (TextView)view.findViewById(R.id.txtCurrentTemp);
        TextView txtMaxMinTemp = (TextView)view.findViewById(R.id.txtMaxMinTemp);
        TextView txtWord = (TextView)view.findViewById(R.id.txtWord);
        ImageView imgWeather = (ImageView)view.findViewById(R.id.imgWeather);
        LinearLayout linearBg = (LinearLayout)view.findViewById(R.id.linearBg);

        PreferenceManagers preferenceManagers = new PreferenceManagers(activity);
        WeatherInfo wc = new WeatherInfo();
       // txtCurrentTemp.setText(wc.getWeatherTxt(preferenceManagers.getWeatherCode())+" "+preferenceManagers.getCurrentTemp());
        txtCurrentTemp.setText(preferenceManagers.getCurrentTemp());
        txtMaxMinTemp.setText(preferenceManagers.getMaxMinTemp());
        imgWeather.setBackgroundResource(wc.getWeatherWhiteImg(preferenceManagers.getWeatherCode()));

        TimeUtil timeUtil = new TimeUtil(activity);

        if(timeUtil.getDayAfeterNight().equals("AFTERNOON")){
            linearBg.setBackgroundResource(wc.getDayAfterNoonImg(preferenceManagers.getWeatherCode()));
        }else{
            linearBg.setBackgroundResource(wc.getDayNightImg(preferenceManagers.getWeatherCode()));

            int wordPosition = wc.getWeatherCode(preferenceManagers.getWeatherCode());
            if(wordPosition==0||wordPosition==1) {
                //txtCurrentTemp.setText(wc.getWeatherTxt(48)+" "+preferenceManagers.getCurrentTemp());\
                txtCurrentTemp.setText(preferenceManagers.getCurrentTemp());
                imgWeather.setBackgroundResource(wc.getWeatherWhiteImg(48));
            }
        }
//        int wordPosition = wc.getDayWord(preferenceManagers.getWeatherCode());
//        String wordArray[] = activity.getResources().getStringArray(wordPosition);
//        int word = (int) (Math.random() * wordArray.length);
//        txtWord.setText(activity.getResources().getStringArray(wordPosition)[word]);
        txtWord.setText("\" "+weatherWord+" \"");
        linearBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment instanceof GoodsWriteFragment){

                    Intent intent = new Intent(activity, WeatherActivity.class);
                    intent.putExtra("dustGrade",fragment.getDustGrade());
                    intent.putExtra("dustValue",fragment.getDustValue());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    activity.startActivity(intent);
                    //((GoodsWriteFragment) fragment).setMoveTop();
                }
            }
        });

        Log.e("codes","codes:"+preferenceManagers.getWeatherCode());
    }

}
