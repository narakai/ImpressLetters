package com.jeycorp.impressletters.weather;

import android.os.AsyncTask;
import android.util.Log;


import com.jeycorp.impressletters.activity.IntroActivity;
import com.jeycorp.impressletters.utill.PreferenceManagers;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;


public class WeatherLocation {
	private IntroActivity activity;
	public WeatherData wData;
	private PreferenceManagers preferenceManagers;
	private String currentTemp;
	private String maxminTemp;
	private int imageDrawable;
	private boolean isInit=true;

	private int code01,code02,code03;
	private int highTemp01,highTemp02,highTemp03;
	private int lowTemp01,lowTemp02,lowTemp03;

	
	public WeatherLocation(IntroActivity activity) {
		this.activity = activity;

		this.wData = new WeatherData();
		preferenceManagers = new PreferenceManagers(activity);
	}


	public void getWeather(String address) {
		startYahooTask(address);
		
	}
	public void startYahooTask(String address) {
		String[] addr = address.split(",");
		int len = addr.length - 1;

		String thisAddr = addr[len-2].trim()  + " " +  addr[len-1].trim() + " " + addr[len].trim();
	    String url = "select * from geo.places where text=\"" + thisAddr + "\"";

	    try {
		   url = URLEncoder.encode(new String(url.getBytes("UTF-8")));
		   url = "http://query.yahooapis.com/v1/public/yql?q=" + url + "&format=xml";
	    } catch (UnsupportedEncodingException e) {
		   e.printStackTrace();
	    }

	    new YahooTask(url, "location").execute();
	}
	
	 class YahooTask extends AsyncTask<Void, Void, Void> {
		 private String  url;
		 private int finishCode;
		 private String xml;
		 private String woeid;

		 public YahooTask(String url, String xml) {
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

					if(xml == "location") {
						while (eventType != XmlPullParser.END_DOCUMENT) {
							if(parser.getName() != null) {
								if(eventType == XmlPullParser.START_TAG) {
									if(parser.getName().equals("woeid")) {
										this.woeid = parser.nextText().trim();
										this.finishCode = 1;
										break;
									}
								}
							}
							eventType = parser.next();
						}
					} else {

						while (eventType != XmlPullParser.END_DOCUMENT) {
										
							if(parser.getName() != null) {
								if(eventType == XmlPullParser.START_TAG) {
									if(parser.getName().equals("yweather:condition")) {		//Now
										
										//yweather:atmosphere humidity="49" ����
										
										wData.setNowTemp(parser.getAttributeValue(null, "temp"));
										wData.setNowCode(parser.getAttributeValue(null, "code"));
										wData.setNowText(parser.getAttributeValue(null, "text"));
										
										Log.d("test", "" + wData.getNowText());
									}
									
									if(parser.getName().equals("yweather:forecast")) {			//Today
										count++;
										Log.e("fsefsefsefsfs","sfwefsefsef:"+count);
										if(count==1){
											wData.setTodayLow(parser.getAttributeValue(null, "low"));

											//���� ����� ���� ���� ��� ���� ������ ���� ����� ���� ������� �ٲ���. (���� ����  ����);
											if(wData.getNowTemp() < wData.getTodayLow()) {
												wData.setTodayLow(String.valueOf(wData.getNowTemp()));
											}

											wData.setTodayHigh(parser.getAttributeValue(null, "high"));
											wData.setTodayCode(parser.getAttributeValue(null, "code"));
											wData.setTodayText(parser.getAttributeValue(null, "text"));
										}
										else if(count==2){
											code01 = Integer.parseInt(parser.getAttributeValue(null, "code"));
											highTemp01 = Integer.parseInt(parser.getAttributeValue(null, "high"));
											lowTemp01 = Integer.parseInt(parser.getAttributeValue(null, "low"));
										}
										else if(count==3){
											code02 = Integer.parseInt(parser.getAttributeValue(null, "code"));
											highTemp02 = Integer.parseInt(parser.getAttributeValue(null, "high"));
											lowTemp02 = Integer.parseInt(parser.getAttributeValue(null, "low"));
										}
										else if(count==4){
											code03 = Integer.parseInt(parser.getAttributeValue(null, "code"));
											highTemp03 = Integer.parseInt(parser.getAttributeValue(null, "high"));
											lowTemp03 = Integer.parseInt(parser.getAttributeValue(null, "low"));

											this.finishCode = 1;
											break;
										}

									}
								}
							}
							eventType = parser.next();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(this.finishCode != 1) {
					this.finishCode = 0;	//����
				}
				return null;
			} 
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
				if(this.xml.equals("weather") && this.finishCode == 1) {

					//Log.d("test", this.xml+"/"+this.finishCode);
					Log.e("또온다","또온다2");
					setWeather();
				}

				finishTask(this.xml, this.finishCode, this.url, this.woeid);
			
		}


	 }
	 public void finishTask(String xml, int finishCode, String url, String woeid) {

			if(xml.equals("location")) {
				if(finishCode == 1) {
					//http://weather.yahooapis.com/forecastrss?w=28806221&u=c
					//http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid=28806221&format=xml
					//http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=%EC%9D%B4%EA%B3%A1%EB%8F%99&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=Fp8ksAers5TQmkPhEg8Xfu3J%2Fe6w0qnX6OtN5mKJ70hKbrq25UgexELp6NRm8w6A2%2BwEapkvgSrV8NA2a495gw%3D%3D

					String query = "select * from weather.forecast where woeid=\"" + woeid + "\"";
					try{
						query = URLEncoder.encode(new String(query.getBytes("UTF-8")));
					}catch(Exception e){

					}

					String httpUrl = "http://query.yahooapis.com/v1/public/yql?q="+query+"&format=xml";
					new YahooTask(httpUrl, "weather").execute();
					preferenceManagers.setLocationCode(woeid);
				}
			}
	}
	 private void setWeather(){

		 int nowTemp = wData.getNowTemp();
		 int lowTemp = wData.getTodayLow();
		 int highTemp = wData.getTodayHigh();

//		 TextView txtHighLowTemp = (TextView)activity.findViewById(R.id.txt_highlow_temp);
//		 TextView txtCurrentTemp = (TextView)activity.findViewById(R.id.txt_current_temp);
//		 ImageView imgWeather = (ImageView)activity.findViewById(R.id.img_weather);
		// txtHighLowTemp.setText(lowTemp+"/"+highTemp+" 현재 ");
		// txtCurrentTemp.setText(nowTemp+"º");
		 String addr = preferenceManagers.getLocationCurrent();
		 String addrArray[]=null;
		 String sido = "";
		 if(addr!=null && !addr.equals("")){
			 addrArray = addr.split(",");
			 sido = addrArray[0];
		 }
		 currentTemp = String.valueOf((int)((nowTemp - 32)/1.8))+"";
		 maxminTemp = sido+"\n최저온도 "+String.valueOf((int)((lowTemp - 32)/1.8))+" / "+"최고 "+String.valueOf((int)((highTemp - 32)/1.8));
		 WeatherInfo wc = new WeatherInfo();
		 int code = wData.getNowCode();
		// int code = wData.getTodayCode();
		 if(code >= 0 && code <= 47) {
			 imageDrawable = wc.getWeatherWhiteImg(code);
			 Log.e("코드","코드:"+code);
			 //imgWeather.setImageResource(wc.getWeatherWhiteImg(code));
		 }
		 int wordPosition = wc.getDayWord(code);
		 String wordArray[] = activity.getResources().getStringArray(wordPosition);
		 int randWord = (int) (Math.random() * wordArray.length);

		 preferenceManagers.setCurrentTemp(currentTemp);
		 preferenceManagers.setMaxMinTemp(maxminTemp);
		 preferenceManagers.setMaxTemp(String.valueOf((int)((highTemp - 32)/1.8)));
		 preferenceManagers.setMinTemp(String.valueOf((int)((lowTemp - 32)/1.8)));
		 preferenceManagers.setWeatherCode(code);
		 preferenceManagers.setRandom(randWord);

		 preferenceManagers.setWeatherCode01(code01);
		 preferenceManagers.setWeatherCode02(code02);
		 preferenceManagers.setWeatherCode03(code03);

		 preferenceManagers.setWeatherHighTemp01(String.valueOf((int)((highTemp01 - 32)/1.8)));
		 preferenceManagers.setWeatherHighTemp02(String.valueOf((int)((highTemp02 - 32)/1.8)));
		 preferenceManagers.setWeatherHighTemp03(String.valueOf((int)((highTemp03 - 32)/1.8)));

		 preferenceManagers.setWeatherLowTemp01(String.valueOf((int)((lowTemp01 - 32)/1.8)));
		 preferenceManagers.setWeatherLowTemp02(String.valueOf((int)((lowTemp02 - 32)/1.8)));
		 preferenceManagers.setWeatherLowTemp03(String.valueOf((int)((lowTemp03 - 32)/1.8)));


		 Log.e("sfsfsefiosjrsr","soifjosijhefoseihjtstt");
		 activity.initAdView();

	 }

	public String getCurrentTemp(){
		return currentTemp;
	}
	public String getMaxMinTemp(){
		return maxminTemp;
	}
	public int getTempImage(){
		return imageDrawable;
	}

}
