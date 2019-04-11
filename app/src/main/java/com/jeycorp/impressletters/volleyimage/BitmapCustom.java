package com.jeycorp.impressletters.volleyimage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import com.jeycorp.impressletters.R;


public class BitmapCustom {
	private Context context;
	
	public BitmapCustom(Context context) {
		this.context = context;
	}
	public Bitmap getRoundedBg(Bitmap bitmap){
		Canvas canvas = new Canvas();
		
		Bitmap tmpMask = BitmapFactory.decodeResource(context.getResources(), R.drawable.mask_profile_back);
		Bitmap mask = Bitmap.createScaledBitmap(tmpMask, bitmap.getWidth(), bitmap.getHeight(), true);
//		tmpMask.recycle();
//		tmpMask = null;
		Bitmap result = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);

		canvas.setBitmap(result);
		Paint paint = new Paint();
		paint.setFilterBitmap(false);

		canvas.drawBitmap(bitmap, 0, 0, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// DST_IN,  DST_OUT(mask reverse)
		canvas.drawBitmap(mask, 0, 0, paint);
		paint.setXfermode(null);
		
		//mainImage.recycle();
		mask.recycle();
		tmpMask.recycle();
		
		//mainImage = null;
		mask = null;
		tmpMask = null;
		return result;
		
	}
	//비트맵 라운드
	public Bitmap getRoundedBitmap(Bitmap bitmap) {
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    if(bitmap.getWidth()>bitmap.getHeight()){
	        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
		            bitmap.getHeight() / 2, paint);
	    }else{
	        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
		            bitmap.getWidth() / 2, paint);
	    }
	
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
	    //return _bmp;
	    return output;
	  }
	public Bitmap getNetworkImage(URL url) {

		HttpURLConnection conn;

		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			Bitmap bm = BitmapFactory.decodeStream(is, null, options);
			is.close();
			return bm;

		} catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
}
