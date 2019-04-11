package com.jeycorp.impressletters.volleyimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;


import com.jeycorp.impressletters.volley.VolleyDialog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ImageSync {
	private Activity activity;
	private ImageView imgView;
	private boolean isRound=false;
	
	public ImageSync(Activity activity,ImageView imgView, boolean isRound) {
		this.activity = activity;
		this.imgView = imgView;
		this.isRound = isRound;
	}
	
	public void excute(String imgUrl){
		new ImageTask().execute(imgUrl);
	}
	
	
	public class ImageTask extends AsyncTask<String, Integer, Bitmap> {
		@Override
		protected void onPreExecute() {
//			VolleyDialog.dismissProgressDialog();
//			VolleyDialog.showProgressDialog((FragmentActivity) activity, new OnCancelListener() {
//				@Override
//				public void onCancel(DialogInterface dialog) {
//
//				}
//			});
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bm = null;
			try {
				URL url = new URL(params[0]);

				URLConnection conn = url.openConnection();
				conn.connect();
				BufferedInputStream bis = new

				BufferedInputStream(conn.getInputStream());

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				bm = BitmapFactory.decodeStream(bis, null, options);
				bis.close();

			} catch (IOException e) {

			}
			return bm;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if(result!=null){
				imgView.setVisibility(View.VISIBLE);

				//imgView.setImageBitmap(result);
				if(isRound){
					BitmapCustom bitmapCustom = new BitmapCustom(activity);
					//Bitmap b =result;
					Bitmap b = bitmapCustom.getRoundedBitmap(result);
					imgView.setImageBitmap(b);
				}else{
					imgView.setImageBitmap(result);
				}
			}


			VolleyDialog.dismissProgressDialog();

		}

	}

}
