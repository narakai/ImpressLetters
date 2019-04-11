package com.jeycorp.impressletters.utill;

import java.lang.reflect.Type;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class RegisterStatic {
	private static Map<String, String> register;
	public final static String KEY_REGISTER_STATIC = "RegisterStatic";
	public final static String CLASS_NAME = RegisterStatic.class.getName();

	public static void putRegister(Context context, Map<String, String> register) {
		RegisterStatic.register = register;
		
		if(register != null) {
			Gson gson = new Gson();
			String resultJson = gson.toJson(register);
			
			SharedPreferences sharedPreferences = context.getSharedPreferences(CLASS_NAME, Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit(); 
			editor.putString(KEY_REGISTER_STATIC, resultJson);
			editor.commit();	
		}
		
	}
	public static String get(Context context, String name) {
		
		if(register == null) {
			Gson gson = new Gson();
			SharedPreferences sharedPreferences = context.getSharedPreferences(CLASS_NAME, Context.MODE_PRIVATE);
			String jsonString = sharedPreferences.getString(KEY_REGISTER_STATIC, null);
			
			Type type = new TypeToken<Map<String, String>>(){}.getType();
			Map<String, String> registerPreferences = gson.fromJson(jsonString, type);
			RegisterStatic.register = registerPreferences;

		}
		
		if(register != null) {
			return register.get(name);
		}
		
		return "";
	}

	public static String getUpdateUrl(Context context) {
		return get(context, "updateUrl");
	}
	public static String getversionName(Context context) {
		return get(context, "versionName");
	}
	public static String getUpdateVersion(Context context) {
		return get(context, "updateVersion");
	}
	public static String getLooseUpdateVersion(Context context) {return get(context, "looseUpdateVersion");}
	public static String getIosAppUrl(Context context){
		return get(context,"iosAppUrl");
	}
	public static String getIosAppVersion(Context context){
		return get(context,"iosAppVersion");
	}
	public static String getIosVersionType(Context context){
		return get(context,"iosVersionType");
	}

}
