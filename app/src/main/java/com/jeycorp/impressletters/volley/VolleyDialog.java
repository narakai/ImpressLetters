package com.jeycorp.impressletters.volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

public class VolleyDialog {

	//private static ProgressDialog progressDialog;
	private static AlertDialog alertDialog;
	private static ProgressDialogFragment progressDialogFragment;
	
	public static synchronized void showProgressDialog(FragmentActivity activity, DialogInterface.OnCancelListener onCancelListener) {
		dismissProgressDialog();		
		//progressDialog = ProgressDialog.show(activity, null, "please wait...", true, true, onCancelListener);
		progressDialogFragment = new ProgressDialogFragment();
		
		if(onCancelListener != null) {
			progressDialogFragment.setOnCancelListener(onCancelListener);
		}

		if(activity.isFinishing() == false) {
			progressDialogFragment.show(activity.getSupportFragmentManager(), "progressDialogFragment");
		}
	}

	public static synchronized void dismissProgressDialog() {
//		if(progressDialog != null) {
//			progressDialog.dismiss();
//			progressDialog = null;
//		}
		if(progressDialogFragment != null) {
//			progressDialogFragment.dismiss();
			progressDialogFragment.dismissAllowingStateLoss();
			progressDialogFragment = null;
		}
	}
	
//	public static synchronized void cancelProgressDialog() {
//		if(progressDialogFragment != null) {
//			progressDialogFragment.getDialog().cancel();
//			progressDialogFragment = null;
//		}
//	}

	public static synchronized void showMessageDialog(Activity activity, String message) {
		if(activity.isFinishing() == false) {
			AlertDialog.Builder builder;
        	if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
        		builder = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT);
        	} else {
        		builder = new AlertDialog.Builder(activity);
        	}
			builder.setMessage(message);
			builder.setPositiveButton("OK", null);
			alertDialog = builder.create();
			alertDialog.show();	
		}
	}

}
