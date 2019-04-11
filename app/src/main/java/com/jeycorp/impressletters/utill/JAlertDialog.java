package com.jeycorp.impressletters.utill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.activity.BaseActivity;


public class JAlertDialog {
	
	@SuppressLint("NewApi")
	public static void showMessage(Activity activity, String message) {
		if(activity.isFinishing() == false) {
			AlertDialog.Builder b = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT);
			b.setTitle(activity.getString(R.string.app_name));
	        b.setMessage(message);
	        b.setPositiveButton(R.string.close, null);
	        b.setCancelable(false).create().show();	
		}			
	}
	
	@SuppressLint("NewApi")
	public static void showMessageAndClose(final Activity activity, String message) {
		if(activity.isFinishing() == false) {
			AlertDialog.Builder b = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT);
			b.setTitle(activity.getString(R.string.app_name));
	        b.setMessage(message);
	        b.setPositiveButton(R.string.close, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
//					activity.finish();
					finishApplication(activity);
				}
			});
	        b.setCancelable(true);
	        b.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
//					activity.finish();
					
				}
			});
	        b.setCancelable(false).create().show();	
		}			
	}
	
	private static void finishApplication(Activity activity) {
		if(activity instanceof BaseActivity) {
			BaseActivity baseActivity = (BaseActivity) activity;
			baseActivity.finishApplication();
		} else {
			throw new ClassCastException(activity.toString() + "not BaseActivity"); 
		}
	}

//	public static void showMessage(Activity activity, int resId) {
//	if (activity.isFinishing() == false) {
//		AlertDialog.Builder b = new AlertDialog.Builder(activity);
//        b.setMessage(activity.getString(resId));
//        b.show();	
//	}		
//}
	
//	public static void showMessage(FragmentManager fm, String message) {
//		AlertDialogFragment.newInstance(AlertDialogFragment.DIALOG_OK_MESSAGE, message, 0).show(fm, "AlertDialogFragment");
//	}
	
//	public static void showMessage(FragmentManager fm, String message, int request) {
//		AlertDialogFragment.newInstance(AlertDialogFragment.DIALOG_OK_MESSAGE, message, request).show(fm, "AlertDialogFragment");
//	}

	
}
