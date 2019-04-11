package com.jeycorp.impressletters.utill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Build;

import com.jeycorp.impressletters.R;


public class JAlertConfirmTitle {
	@SuppressLint("NewApi")
	public JAlertConfirmTitle(Activity activity, String message, String title, boolean hideCancel) {
		Builder builder = null;
		if (Build.VERSION.SDK_INT > 14) {
			builder = new Builder(activity, AlertDialog.THEME_HOLO_LIGHT);
		} else {
			builder = new Builder(activity);
		}

		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						onYes();
					}
				});

		if (hideCancel == false) {
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							onCancel();
						}
					});
		}

		builder.setCancelable(true).create().show();
	}

	protected void onYes() {

	}

	protected void onCancel() {

	}

}