package com.jeycorp.impressletters.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeycorp.impressletters.utill.GeoTrans;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.utill.GeoPoint;
import com.jeycorp.impressletters.utill.TimeUtil;
import com.jeycorp.impressletters.weather.WeatherInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.net.URLEncoder;

public class WeatherActivity extends BaseActivity {

    private String strLocationName = "";
    private int dustGrade = 0;
    private int dustValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_weather);
            getCurrentDate();
            getCurrentWeather();
            getWeekWeather();
            getDustInfo();
        }
    }

    private void setInitView() {
    }

    private void getCurrentDate() {
        TextView txtDate = (TextView) findViewById(R.id.txt_date);

        TimeUtil timeUtil = new TimeUtil(activity);
        String[] strDate = timeUtil.getToDate().split("-");
        String strToday = strDate[1] + "월 " + strDate[2] + "일";
        txtDate.setText(strToday + "(" + timeUtil.getTodaytWeek() + ")");
    }

    private void getCurrentWeather() {
        PreferenceManagers preferenceManagers = new PreferenceManagers(activity);

        TextView txtCurrentTemp = (TextView) findViewById(R.id.txt_current_temp);
        TextView txtCurrentHighTemp = (TextView) findViewById(R.id.txt_current_high_temp);
        TextView txtCurrentLowTemp = (TextView) findViewById(R.id.txt_current_low_temp);
        TextView txtCurrentStateTemp = (TextView) findViewById(R.id.txt_current_temp_state);

        ImageView imgWeather = (ImageView) findViewById(R.id.img_current_weather);

        ImageView imgBg = (ImageView) findViewById(R.id.img_bg);

        WeatherInfo wc = new WeatherInfo();

        txtCurrentTemp.setText(preferenceManagers.getCurrentTemp().replaceAll("º", ""));
        txtCurrentStateTemp.setText(wc.getWeatherTxt(preferenceManagers.getWeatherCode()));
        txtCurrentHighTemp.setText(preferenceManagers.getMaxTemp() + "º");
        txtCurrentLowTemp.setText(preferenceManagers.getMinTemp() + "º");
        imgWeather.setBackgroundResource(wc.getWeatherWhiteImg(preferenceManagers.getWeatherCode()));

        TimeUtil timeUtil = new TimeUtil(activity);

        if (timeUtil.getDayAfeterNight().equals("AFTERNOON")) {
            imgBg.setBackgroundResource(wc.getWeatherAfterNoonImg(preferenceManagers.getWeatherCode()));
        } else {
            imgBg.setBackgroundResource(wc.getWeatherNightImg(preferenceManagers.getWeatherCode()));

            int wordPosition = wc.getWeatherCode(preferenceManagers.getWeatherCode());
            if (wordPosition == 0 || wordPosition == 1) {
                txtCurrentTemp.setText(preferenceManagers.getCurrentTemp());
                imgWeather.setBackgroundResource(wc.getWeatherWhiteImg(48));
            }
        }
    }

    private void getWeekWeather() {
        PreferenceManagers preferenceManagers = new PreferenceManagers(activity);

        TextView txtWeekDay01 = (TextView) findViewById(R.id.txt_week_day01);
        TextView txtWeekDay02 = (TextView) findViewById(R.id.txt_week_day02);
        TextView txtWeekDay03 = (TextView) findViewById(R.id.txt_week_day03);

        TextView txtWeekHighTemp01 = (TextView) findViewById(R.id.txt_week_max_tempo01);
        TextView txtWeekHighTemp02 = (TextView) findViewById(R.id.txt_week_max_tempo02);
        TextView txtWeekHighTemp03 = (TextView) findViewById(R.id.txt_week_max_tempo03);

        TextView txtWeekLowTemp01 = (TextView) findViewById(R.id.txt_week_min_tempo01);
        TextView txtWeekLowTemp02 = (TextView) findViewById(R.id.txt_week_min_tempo02);
        TextView txtWeekLowTemp03 = (TextView) findViewById(R.id.txt_week_min_tempo03);

        ImageView imgWeekDay01 = (ImageView) findViewById(R.id.img_week_day01);
        ImageView imgWeekDay02 = (ImageView) findViewById(R.id.img_week_day02);
        ImageView imgWeekDay03 = (ImageView) findViewById(R.id.img_week_day03);


        TimeUtil timeUtil = new TimeUtil(activity);
        txtWeekDay01.setText(timeUtil.getNextDate(1));
        txtWeekDay02.setText(timeUtil.getNextDate(2));
        txtWeekDay03.setText(timeUtil.getNextDate(3));

        txtWeekHighTemp01.setText(preferenceManagers.getWeatherHighTemp01() + "º");
        txtWeekHighTemp02.setText(preferenceManagers.getWeatherHighTemp02() + "º");
        txtWeekHighTemp03.setText(preferenceManagers.getWeatherHighTemp03() + "º");

        txtWeekLowTemp01.setText(preferenceManagers.getWeatherLowTemp01() + "º");
        txtWeekLowTemp02.setText(preferenceManagers.getWeatherLowTemp02() + "º");
        txtWeekLowTemp03.setText(preferenceManagers.getWeatherLowTemp03() + "º");

        WeatherInfo wc = new WeatherInfo();
        imgWeekDay01.setBackgroundResource(wc.getWeatherWhiteImg(preferenceManagers.getWeatherCode01()));
        imgWeekDay02.setBackgroundResource(wc.getWeatherWhiteImg(preferenceManagers.getWeatherCode02()));
        imgWeekDay03.setBackgroundResource(wc.getWeatherWhiteImg(preferenceManagers.getWeatherCode03()));


    }

    private void getDustInfo() {
        PreferenceManagers preferenceManagers = new PreferenceManagers(activity);


//37.5545613, 126.9906973

        GeoPoint in_pt = new GeoPoint(preferenceManagers.getLongitude(), preferenceManagers.getLatitude());
        GeoPoint tm_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, in_pt);
        double tm_x = tm_pt.getX();
        double tm_y = tm_pt.getY();

//        String sdx = Double.toString(tm_x);
//        String sdy = Double.toString(tm_y);


        TextView txtDustInfo01 = (TextView) findViewById(R.id.txt_dust_info01);
        TextView txtDustInfo02 = (TextView) findViewById(R.id.txt_dust_info02);

        final SpannableStringBuilder sp01 = new SpannableStringBuilder(activity.getResources().getString(R.string.weather_dust_info01));
        sp01.setSpan(new ForegroundColorSpan(Color.WHITE), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtDustInfo01.append(sp01);

        final SpannableStringBuilder sp02 = new SpannableStringBuilder(activity.getResources().getString(R.string.weather_dust_info02));
        sp02.setSpan(new ForegroundColorSpan(Color.WHITE), 10, 37, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtDustInfo02.append(sp02);
        Linkify.addLinks(txtDustInfo02, Linkify.WEB_URLS);

        responseDust();
//        String url = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?tmX="+tm_x+"&tmY="+tm_y+"&pageNo=1&numOfRows=10&ServiceKey=Fp8ksAers5TQmkPhEg8Xfu3J%2Fe6w0qnX6OtN5mKJ70hKbrq25UgexELp6NRm8w6A2%2BwEapkvgSrV8NA2a495gw%3D%3D";
//        new LocationTask(url, "location").execute();


    }

    public void menuClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }

    private void responseLocationName() {
        String name = "";
        try {
            name = URLEncoder.encode(strLocationName, "utf-8");
        } catch (Exception e) {

        }

        String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=" + name + "&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=Fp8ksAers5TQmkPhEg8Xfu3J%2Fe6w0qnX6OtN5mKJ70hKbrq25UgexELp6NRm8w6A2%2BwEapkvgSrV8NA2a495gw%3D%3D";
        new DustTask(url, "location").execute();
    }

    private void responseDust() {
        dustGrade = getIntent().getExtras().getInt("dustGrade");
        dustValue = getIntent().getExtras().getInt("dustValue");
        Log.e("dustValue", "dustValue:" + dustValue);
        String str = "좋음";
        TextView txtCurrentDust = (TextView) findViewById(R.id.txt_current_dust);
        ImageView imgDustProgress = (ImageView) findViewById(R.id.img_dust_progress);
        TextView txtCurrentDustNumber = (TextView) findViewById(R.id.txt_dust_number);

        txtCurrentDust.setVisibility(View.VISIBLE);
        imgDustProgress.setVisibility(View.VISIBLE);
        findViewById(R.id.txt_title_dust).setVisibility(View.VISIBLE);

        switch (dustGrade) {
            case 1:
                str = "좋음";
                txtCurrentDust.setTextColor(Color.parseColor("#ffffff"));
                imgDustProgress.setBackgroundResource(R.drawable.dust_bar_good);
                break;
            case 2:
                str = "보통";
                txtCurrentDust.setTextColor(Color.parseColor("#ffffff"));
                imgDustProgress.setBackgroundResource(R.drawable.dust_bar_normal);
                break;
            case 3:
                str = "나쁨";
                txtCurrentDust.setTextColor(Color.parseColor("#d84315"));

                imgDustProgress.setBackgroundResource(R.drawable.dust_bar_bad);
                break;
            case 4:
                str = "매우나쁨";
                txtCurrentDust.setTextColor(Color.parseColor("#d84315"));

                imgDustProgress.setBackgroundResource(R.drawable.dust_bar_very_bed);
                break;
            default:
                break;
        }

        txtCurrentDust.setText(str + " (" + String.valueOf(dustValue) + "ug)");


    }

    class LocationTask extends AsyncTask<Void, Void, Void> {
        private String url;
        private int finishCode;
        private String xml;
        private String woeid;

        public LocationTask(String url, String xml) {
            this.url = url;
            this.xml = xml;
            finishCode = 0;

        }

        @Override
        protected Void doInBackground(Void... params) {
            int count = 0;
            try {
                URL httpUrl = new URL(this.url);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(httpUrl.openStream(), "UTF-8");

                int eventType = parser.getEventType();


                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (parser.getName() != null) {
                        if (eventType == XmlPullParser.START_TAG) {

                            if (parser.getName().equals("stationName")) {

                                strLocationName = parser.nextText();


                                break;

                            }

                        }
                    }
                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (strLocationName.equals("") == false) {
                responseLocationName();
            }

        }

    }

    class DustTask extends AsyncTask<Void, Void, Void> {
        private String url;
        private int finishCode;
        private String xml;
        private String woeid;

        public DustTask(String url, String xml) {
            this.url = url;
            this.xml = xml;
            finishCode = 0;
        }

        @Override
        protected Void doInBackground(Void... params) {
            int count = 0;
            try {
                URL httpUrl = new URL(this.url);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(httpUrl.openStream(), "UTF-8");

                int eventType = parser.getEventType();


                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (parser.getName() != null) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("pm10Value")) {
                                dustValue = Integer.parseInt(parser.nextText());
                            }
                            if (parser.getName().equals("pm10Grade")) {

                                dustGrade = Integer.parseInt(parser.nextText());


                                break;

                            }

                        }
                    }
                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (dustGrade != 0) {
                responseDust();
            }

        }

    }

}
