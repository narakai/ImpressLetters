package com.jeycorp.impressletters.weather;

import com.jeycorp.impressletters.R;


public class WeatherInfo {
	private int[] weatherCode;
	private int[] weatherWhiteImg;
	private String[] weatherTxt;
	private int[] dayAfterNoonImg;
	private int[] dayNightImg;
	private int[] dayWord;

	private int[] weatherDayAfterNoonImg;
	private int[] weatherDayNightImg;
	
	public WeatherInfo() {
	this.weatherWhiteImg = new int[] {R.drawable.main_weather_sun, R.drawable.main_weather_cloud, R.drawable.main_weather_rain, R.drawable.main_weather_snow,R.drawable.main_weather_moon};
		//this.weatherTxt = new String[] {"맑음(0)","구름(1)","구름(2)","비(3)","맑음(4)","비(5)","눈(6)","구름(7)","밤(8)"};
		this.weatherTxt = new String[] {"맑음","구름","비","눈","밤"};

		this.dayAfterNoonImg = new int[] {R.drawable.main_weather_sun_bg,R.drawable.main_weather_cloud_bg,R.drawable.main_weather_rain_bg,R.drawable.main_weather_snow_bg,R.drawable.main_weather_cloud_night_bg};
		this.dayNightImg = new int[] {R.drawable.main_weather_sun_night_bg,R.drawable.main_weather_cloud_night_bg,R.drawable.main_weather_rain_night_bg,R.drawable.main_weather_snow_night_bg,R.drawable.main_weather_cloud_night_bg};
		this.weatherDayAfterNoonImg = new int[]{R.drawable.weather_sun,R.drawable.weather_cloud,R.drawable.weather_rain,R.drawable.weather_snow,R.drawable.weather_night};
		this.weatherDayNightImg = new int[]{R.drawable.weather_night,R.drawable.weather_night,R.drawable.weather_rain,R.drawable.weather_snow,R.drawable.weather_night};

		this.dayWord = new int[]{R.array.sun_array,R.array.cloud_array,R.array.rain_array,R.array.snow_array,R.array.cloud_array};
		weatherCode = new int[49];
		weatherCode[0] = 1;		//0	tornado							����̵�		(��)
		weatherCode[1] = 2;		//1	tropical storm					��ǳ��		(��)
		weatherCode[2] = 2;		//2	hurricane						�㸮����		(��)
		weatherCode[3] = 1;		//3	severe thunderstorms		���� ����		(��)
		weatherCode[4] = 1;		//4	thunderstorms					����			(��)
		weatherCode[5] = 3;		//5	mixed rain and snow			��&��		(��)
		weatherCode[6] = 2;		//6	mixed rain and sleet			��&����	(��)
		weatherCode[7] = 3;		//7	mixed snow and sleet		��&����	(��)
		weatherCode[8] = 3;		//8	freezing drizzle					��� ����	(��)
		weatherCode[9] = 2;		//9	drizzle							�̽���		(�غ�)
		weatherCode[10] = 2;		//10	freezing rain					���ٴ� ��	(��)
		weatherCode[11] = 2;		//11	showers							�ҳ���		(�غ�)
		weatherCode[12] = 2;		//12	showers							�ҳ���		(�غ�)
		weatherCode[13] = 3;		//13	snow flurries					������ ���� ��	(��)
		weatherCode[14] = 3;		//14	light snow showers			�ѽ��� ��			(��)
		weatherCode[15] = 3;		//15	blowing snow					���� ��			(��)
		weatherCode[16] = 3;		//16	snow								��				(��)
		weatherCode[17] = 2;		//17	hail								���				(��)
		weatherCode[18] = 3;		//18	sleet								����			(��)
		weatherCode[19] = 1;		//19	dust								����				(�ع���)
		weatherCode[20] = 1;		//20	foggy								£�� �Ȱ�				(�ع���)
		weatherCode[21] = 1;		//21	haze								���� ����				(�ع���)
		weatherCode[22] = 1;		//22	smoky							����				(�ع���)
		weatherCode[23] = 1;		//23	blustery							�ٶ��� �ż�		(�ر���)
		weatherCode[24] = 1;		//24	windy								�ٶ�				(�ر���)
		weatherCode[25] = 1;		//25	cold								�߿�				(�ر���)
		weatherCode[26] = 1;		//26	cloudy							�帲				(����)
		weatherCode[27] = 1;		//27	mostly cloudy (night)		��ü�� �帲		(����)
		weatherCode[28] = 1;		//28	mostly cloudy (day)			��ü�� �帲		(����)
		weatherCode[29] = 1;		//29	partly cloudy (night)			�κ��� �帲		(�ر���)
		weatherCode[30] = 1;		//30	partly cloudy (day)			�κ��� �帲		(�ر���)
		weatherCode[31] = 1;		//31	clear (night)					�帰 �� ��				(�ر���)
		weatherCode[32] = 0;		//32	sunny								����				(��)
		weatherCode[33] = 1;		//33	fair (night)						����		(��)
		weatherCode[34] = 0;		//34	fair (day)						����		(��)
		weatherCode[35] = 2;		//35	mixed rain and hail			��+���			(��)
		weatherCode[36] = 0;		//36	hot								����				(�ر���)
		weatherCode[37] = 1;		//37	isolated thunderstorms		����				(��)
		weatherCode[38] = 1;		//38	scattered thunderstorms	����				(��)
		weatherCode[39] = 1;		//39	scattered thunderstorms	����				(��)
		weatherCode[40] = 2;		//40	scattered showers			�ҳ���			(�غ�)
		weatherCode[41] = 3;		//41	heavy snow						��				(��)
		weatherCode[42] = 2;		//42	scattered snow showers	�ҳ���			(�غ�)
		weatherCode[43] = 3;		//43	heavy snow						��				(��)
		weatherCode[44] = 0;		//44	partly cloudy					�帲				(����)
		weatherCode[45] = 1;		//45	thundershowers				��+�ҳ���		(��)
		weatherCode[46] = 3;		//46	snow showers					��				(��)
		weatherCode[47] = 1;		//47	isolated thundershowers	��+�ҳ���		(��)
		weatherCode[48] = 4;
	}
	
	
	public int getWeatherWhiteImg(int code) {
		return this.weatherWhiteImg[weatherCode[code]];
	}
	public String getWeatherTxt(int code){
		return this.weatherTxt[weatherCode[code]];
	}
	public int getDayAfterNoonImg(int code){
		return this.dayAfterNoonImg[weatherCode[code]];
	}
	public int getDayNightImg(int code){
		return this.dayNightImg[weatherCode[code]];
	}
	public int getDayWord(int code){
		return this.dayWord[weatherCode[code]];
	}

	public int getWeatherAfterNoonImg(int code){return this.weatherDayAfterNoonImg[weatherCode[code]];}
	public int getWeatherNightImg(int code){return this.weatherDayNightImg[weatherCode[code]];}


	public int getWeatherCode(int code){
		return this.weatherCode[code];
	}

}



	






