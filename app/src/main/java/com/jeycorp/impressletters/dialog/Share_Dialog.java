package com.jeycorp.impressletters.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.utill.KaKaoLink;

public class Share_Dialog extends Dialog {
	private String urls;
	private Activity activity;
	private Bundle bundle;
	private KaKaoLink kaKaoLink;

	private long seq;
	private String text;
	private String content;
	private String imageUrl;
	private String linkText;
	private String ButtonText;
	private String buttonUrl;
	private int width;
	private int height;

	
	public Share_Dialog(Context context,Activity activity, KaKaoLink kaKaoLink) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.activity = activity;
		this.kaKaoLink = kaKaoLink;
	}

	public void setInitData(long seq, String text, String content, String imageUrl, String linkText, String ButtonText, String buttonUrl, int width, int height){
		this.seq = seq;
		this.text = text;
		this.content = content;
		this.imageUrl = imageUrl;
		this.linkText = linkText;
		this.ButtonText = ButtonText;
		this.buttonUrl = buttonUrl;
		this.width = width;
		this.height = height;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bundle = savedInstanceState;
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.5f;
		getWindow().setAttributes(lpWindow);
		
		setContentView(R.layout.share_dialog);
		
		findViewById(R.id.btn_kakao_share).setOnClickListener(clickListener);
		findViewById(R.id.btn_kakao_story_share).setOnClickListener(clickListener);
		findViewById(R.id.btn_facebook_share).setOnClickListener(clickListener);
		findViewById(R.id.btn_instagram_share).setOnClickListener(clickListener);
		findViewById(R.id.btn_more_share).setOnClickListener(clickListener);
		

	}

	View.OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Message message = new Message();
			switch(v.getId()){
			case R.id.btn_kakao_share:
				startApp("com.kakao.talk");
				kaKaoLink.sendKakaoTalkLink(seq, text, imageUrl, linkText, ButtonText, buttonUrl, width, height);
				break;
			case R.id.btn_kakao_story_share:
				startApp("com.kakao.story");
				kaKaoLink.sendKakaoStory(seq, text,content, imageUrl, linkText, ButtonText, buttonUrl, width, height);
				break;
			case R.id.btn_facebook_share:
				startApp("com.facebook.katana");
				kaKaoLink.publishStory(bundle,urls,text,buttonUrl);
				break;
			case R.id.btn_instagram_share:
				Intent intent2 = activity.getPackageManager().getLaunchIntentForPackage(
						"com.nhn.android.band");
				if(intent2==null){
					intent2 = new Intent(Intent.ACTION_VIEW);
					intent2.setData(Uri.parse("market://details?id=" + "com.nhn.android.band"));
					activity.startActivity(intent2);
					return;
				}else{
					Intent bIntent = new Intent(Intent.ACTION_SEND);
					bIntent.setType("text/plain");
					bIntent.putExtra(Intent.EXTRA_SUBJECT, text);
					bIntent.putExtra(Intent.EXTRA_TEXT, buttonUrl);
					bIntent.setPackage("com.nhn.android.band");
					getContext().startActivity(bIntent);
				}

				break;
			case R.id.btn_more_share:
				//urls = imageUrl;
				//new imgUrlAsync().execute();
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, buttonUrl);

				Intent chooser = Intent.createChooser(intent, "공유");
				getContext().startActivity(chooser);
				break;
			}
			Share_Dialog.this.dismiss();
			
		}
	};
	
	private void startApp(String url) {
		Intent intent = activity.getPackageManager().getLaunchIntentForPackage(
				url);
		if(intent==null){
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=" + url));
			activity.startActivity(intent);
			return;
		}

		
	}	/*
	class imgUrlAsync extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			HttpURLConnection connection = null;
			try {
				URL url = new URL(urls);
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);


				SunUtill mSunUtill = new SunUtill(getContext());
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(mSunUtill.getPath()
							+ "/tempImage.jpg");
					myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				} catch (Exception e) {
					e.printStackTrace();

				} finally {

				}

			} catch (IOException e) {
				e.printStackTrace();

			} finally {
				if (connection != null)
					connection.disconnect();
			}

			return null;
		}


		@Override
		protected void onPostExecute(Void result) {
			SunUtill mSunUtill = new SunUtill(getContext());
			String imagePath = mSunUtill.getPath()+"/tempImage.jpg";

			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			//intent.setType("image/*");
			intent.setType("text/plain");
			//intent.putExtra(Intent.EXTRA_SUBJECT, text);
			intent.putExtra(Intent.EXTRA_TEXT, buttonUrl);
			//intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///"+imagePath));


			Intent chooser = Intent.createChooser(intent, "공유");
			getContext().startActivity(chooser);


		}


	}
	*/
}
