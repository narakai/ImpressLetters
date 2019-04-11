package com.jeycorp.impressletters.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.gun0912.tedpermission.TedPermission;
import com.jeycorp.impressletters.weather.FusedLocationHelper;
import com.jeycorp.impressletters.R;

public class BaseActivity extends FragmentActivity {
	protected Activity activity;
	private static final String ACTION_FINISH = "finish";
	public boolean isPermission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isPermission = TedPermission.isGranted(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		activity = this;
		LocalBroadcastManager.getInstance(activity).registerReceiver(finishBroadcastReceiver, new IntentFilter(ACTION_FINISH));
		//new Analytics(getApplication()).getOutputEvent("퀴즈슬라이드/락스크린 화면");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(activity).unregisterReceiver(finishBroadcastReceiver);
	}
	public void requestPermissions() {
		Intent intent = new Intent(BaseActivity.this, PermissionActivity.class);
		startActivity(intent);
		finish();
	}
	protected BroadcastReceiver finishBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			activity.finish();
		}
	};
	public void finishApplication() {
		LocalBroadcastManager.getInstance(activity).sendBroadcast(
				new Intent(ACTION_FINISH));
	}
	public DisplayMetrics getDisplay(){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}

	public int[] getXml(int id) {

		TypedArray ar = getResources().obtainTypedArray(id);
		int len = ar.length();//

		int[] image = new int[len];
		for (int i = 0; i < len; i++)

			image[i] = ar.getResourceId(i, 0);

		ar.recycle();

		return image;
	}

	public String getStr(int resId) {
		return getResources().getString(resId);
	}
	public Bitmap getBitmap(String imagePath, int size){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = size;
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);

		return bitmap;
	}
	public Bitmap getBitmapResource(int imageResource){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResource, options);

		return bitmap;
	}
	public int exifOrientationToDegrees(int exifOrientation) {

		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}
	public Bitmap rotate(Bitmap bitmap, int degrees) {
		if (degrees != 0 && bitmap != null) {

			Matrix m = new Matrix();
			m.setRotate(degrees, (float) bitmap.getWidth() / 2,
					(float) bitmap.getHeight() / 2);

			try {
				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), m, true);
				// Log.d("#######################",
				// "bitmap.getWidth() & bitmap.getHeight() = "+bitmap.getWidth()
				// +"   " + bitmap.getHeight());
				if (bitmap != converted) {
					bitmap.recycle();
					bitmap = converted;
				}
			} catch (OutOfMemoryError ex) {
				// 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
			}
		}
		return bitmap;
	}
	public boolean getGPScheck(){
		if(FusedLocationHelper.isLocationAvailable(this) == false) {
			Toast.makeText(activity,getStr(R.string.please_on_location_service),Toast.LENGTH_SHORT).show();
//			new JAlertConfirm(this,this.getString(R.string.please_on_location_service), false) {
//				@Override
//				protected void onYes() {
//					super.onYes();
//					Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//					intent.addCategory(Intent.CATEGORY_DEFAULT);
//					startActivity(intent);
//				}
//			};

			return false;

		}
		return true;
	}
	public void onStartActivity(Intent intent){
		if(intent!=null){
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}





}
