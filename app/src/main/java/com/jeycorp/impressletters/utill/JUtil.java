package com.jeycorp.impressletters.utill;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.provider.Settings.Secure;

import com.jeycorp.impressletters.type.Device;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class JUtil {

	public static Device getDevice(Context context) {
		GcmPreferenceManager gcmPreferenceManager = new GcmPreferenceManager(context);
		String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		String registrationId = gcmPreferenceManager.getGcmId();
		//String registrationId = GcmRegisterManager.getRegistrationId(context);

		String osType = "Android";
		String osVersion = Build.VERSION.RELEASE;
		
		PackageInfo packageInfo = null;
		
		try {
			packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String versionName = packageInfo.versionName;
		
		Locale systemLocale = context.getResources().getConfiguration().locale;
		String country = systemLocale.getCountry();
		String language = systemLocale.getLanguage();
		String model = Build.MODEL.toString().trim();

		Device device = new Device();

		device.setDeviceId(deviceId);
		device.setPushId(registrationId);
		device.setOsType(osType);
		device.setOsVersion(osVersion);
		device.setVersionName(versionName);
		device.setCountry(country);
		device.setLanguage(language);
		device.setModel(model);

		return device;
	}
	
	public static int getDpiToPixel(Context context, int dpi) {
		float density = context.getResources().getDisplayMetrics().density;
		return (int) (density * dpi);
	}
	
	public static boolean hasSpecialCharacter(String text) {
		boolean finded = false;
		String pattern = "([^\\p{L}0-9])|(\\s)";
		Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = compiledPattern.matcher(text);
		while(matcher.find()) {
			finded = true;
		}
		
		return finded;
	}
	
	public static boolean hasSpaceCharacter(String text) {
		boolean finded = false;
		String pattern = "(\\s)";
		Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = compiledPattern.matcher(text);
		while(matcher.find()) {
			finded = true;
		}
		
		return finded;
	}
}
