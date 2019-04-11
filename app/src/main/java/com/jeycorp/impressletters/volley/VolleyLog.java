package com.jeycorp.impressletters.volley;


import com.jeycorp.impressletters.define.DebugDefine;

public class VolleyLog {
	
	private static final boolean DEBUG_MODE = DebugDefine.DEBUG_MODE;
	
    public static int LEVEL = android.util.Log.DEBUG;

	public static void d(String tag, String msg)
	{
	    if (LEVEL <= android.util.Log.DEBUG)
	    {
	    	if(DEBUG_MODE) {
	    		android.util.Log.d(tag, msg);
	    	}
	    }
	}
	
	public static void d(String tag, String msg, Throwable tr)
	{
	    if (LEVEL <= android.util.Log.DEBUG)
	    {
	    	if(DEBUG_MODE) {
	    		android.util.Log.d(tag, msg, tr);
	    	}
	    }
	}

	public static void v(String tag, String msg)
	{
	    if (LEVEL <= android.util.Log.VERBOSE)
	    {
	    	if(DEBUG_MODE) {
	    		android.util.Log.v(tag, msg);
	    	}
	    }
	}
	
	public static void i(String tag, String msg)
	{
	    if (LEVEL <= android.util.Log.INFO)
	    {
	    	if(DEBUG_MODE) {
	    		android.util.Log.i(tag, msg);
	    	}
	    }
	}
	
	public static void w(String tag, String msg)
	{
	    if (LEVEL <= android.util.Log.WARN)
	    {
	    	if(DEBUG_MODE) {
	    		android.util.Log.w(tag, msg);
	    	}
	    }
	}	
	
	public static void e(String tag, String msg)
	{
	    if (LEVEL <= android.util.Log.ERROR)
	    {
	    	if(DEBUG_MODE) {
	    		android.util.Log.e(tag, msg);
	    	}
	    }
	}
	
//	public static void e(String tag, String msg, Throwable tr)
//	{
//	    if (LEVEL <= android.util.Log.ERROR)
//	    {
//	    	if(DEBUG_MODE) {
//	    		android.util.Log.e(tag, msg, tr);
//	    	}
//	    }
//	}
	
}
