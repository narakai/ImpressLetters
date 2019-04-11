package com.jeycorp.impressletters.volley;

import android.content.Context;
import android.widget.Toast;

import com.jeycorp.impressletters.define.DebugDefine;


public class VolleyToast {

	public static void show(Context context, String text) {
		if(DebugDefine.DEBUG_MODE) {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();	
		}		
	}
}
