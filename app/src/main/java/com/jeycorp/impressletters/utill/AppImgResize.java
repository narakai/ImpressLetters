package com.jeycorp.impressletters.utill;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AppImgResize {
	//1200
	private final int DEFAULT_HEIGHT = 1920;
	private final int DEFAULT_WIDTH = 1080;
	private int mWidth, mHeight;
	private float disWidth, disHeight;
	
	public AppImgResize(Context context,int width, int height) {
		getSize(context,width,height);
	}

	public AppImgResize(Context context, int id) {
		getSize(context, context.getResources().getDrawable(id));
	}

	public AppImgResize(Context context, Drawable drawable) {
		getSize(context, drawable);
	}
	
	private void getSize(Context context, int width, int height){
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1)
			setDisplaySizeNew(context);
		else
			setDisplaySizeOld(context);
		
		float squ = disWidth / disWidth;
		//float squ = disHeight / DEFAULT_HEIGHT;

		// 이미지 사이즈
		mWidth = (int) (width * squ);
		mHeight = (int) (height * squ);
	}

	private void getSize(Context context, Drawable drawable) {
		// 단말기 해상도 가져오기
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1)
			setDisplaySizeNew(context);
		else
			setDisplaySizeOld(context);

		// 현재 단말기 해상도 / 기준 단말기 해상도
		float squ = disWidth / disWidth;

		// 이미지 사이즈
		mWidth = (int) (drawable.getIntrinsicWidth() * squ);
		mHeight = (int) (drawable.getIntrinsicHeight() * squ);
	}

	// API 13이상
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void setDisplaySizeNew(Context context) {
		Display dis = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point p = new Point();
		dis.getSize(p);
		disWidth = p.x;
		disHeight = p.y;
	}

	// API 13미만
	private void setDisplaySizeOld(Context context) {
		Display dis = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		disWidth = dis.getWidth();
		disHeight = dis.getHeight();
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public int getDisplayWidth() {
		return (int) disWidth;
	}

	public int getDisplayHeight() {
		return (int) disHeight;
	}

	public void resize(View v) {
		v.getLayoutParams().width = mWidth;
		v.getLayoutParams().height = mHeight;
	}

	public void resize(View v, float squ) {
		v.getLayoutParams().width = (int) (mWidth * squ);
		v.getLayoutParams().height = (int) (mHeight * squ);
	}

	public void resize(View v, float wSqu, float hSqu) {
		v.getLayoutParams().width = (int) (mWidth * wSqu);
		v.getLayoutParams().height = (int) (mHeight * hSqu);
	}

	public void resizeWidth(View v) {
		v.getLayoutParams().width = mWidth;
	}

	public void resizeWidth(View v, float squ) {
		v.getLayoutParams().width = (int) (mWidth * squ);
	}

	public void resizeHeight(View v) {
		v.getLayoutParams().height = mHeight;
	}

	public void resizeHeight(View v, float squ) {
		v.getLayoutParams().height = (int) (mHeight * squ);
	}

	public void resizeDividerHeight(ListView v) {
		v.setDividerHeight(mHeight);
	}

	public void setLinearLayoutParams(View v) {
		v.setLayoutParams(new LinearLayout.LayoutParams(mWidth, mHeight));
	}
}
