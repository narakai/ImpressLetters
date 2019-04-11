package com.jeycorp.impressletters.utill;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

public class PushWakeLock {
	private static PowerManager.WakeLock sCpuWakeLock;    
    private static KeyguardManager.KeyguardLock mKeyguardLock;    
    private static boolean isScreenLock;

    
    public static boolean screenOn(Context context) {
    	PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    	if(pm.isScreenOn() == true) {
    		return true;
    	} else {
    		return false;
    	}
    }

     
    public static void acquireCpuWakeLock(Context context) {        
        //Log.e("PushWakeLock", "Acquiring cpu wake lock");        
        //Log.e("PushWakeLock", "wake sCpuWakeLock = " + sCpuWakeLock);        
         
        if (sCpuWakeLock != null) {            
            return;        
        }         
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sCpuWakeLock = pm.newWakeLock(                
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |                
                PowerManager.ACQUIRE_CAUSES_WAKEUP |                
                PowerManager.ON_AFTER_RELEASE, "hello");        
         
        sCpuWakeLock.acquire();
        
    }
     
    public static void releaseCpuLock() {        
        //Log.e("PushWakeLock", "Releasing cpu wake lock");
        //Log.e("PushWakeLock", "relase sCpuWakeLock = " + sCpuWakeLock);
         
        if (sCpuWakeLock != null) {            
            sCpuWakeLock.release();            
            sCpuWakeLock = null;        
        }    
    }
}

