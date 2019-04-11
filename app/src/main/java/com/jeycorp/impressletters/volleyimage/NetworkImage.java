package com.jeycorp.impressletters.volleyimage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkImage {
	private ArrayList<String> urlList;
	private ArrayList<Bitmap> bitmaps;
	private boolean downLoadException = false;
	public NetworkImage(ArrayList<String> urlList) {
		this.urlList = urlList;
		
		bitmaps = new ArrayList<Bitmap>();
		new getImageTask().execute();
	}
	
	
	class getImageTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			int position = 0;
			URL url = null;
			
			
			for(int i=0; i<urlList.size(); i++) {
				try {
					url = new URL(urlList.get(i));
					bitmaps.add(getNetworkImage(url));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(downLoadException == true) {
				Log.e("network image", "downLoad error");
			} else {
				onResponse(bitmaps);
			}
		}

	}
	
	public Bitmap getUrlBitmap(){
		return bitmaps.get(0);
	}
	
	
	private Bitmap getNetworkImage(URL url) {

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

			//int[] res = new int[1];
			//res[0] = color.transparent;
			//Bitmap bm = Bitmap.createBitmap(res, 1, 1, Bitmap.Config.ARGB_8888);
			
			return null;
		}
	}
	
	protected void onResponse(ArrayList<Bitmap> bitmaps) {
		
	}
}
