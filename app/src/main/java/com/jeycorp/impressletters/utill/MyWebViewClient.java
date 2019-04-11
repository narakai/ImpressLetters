package com.jeycorp.impressletters.utill;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.volley.VolleyDialog;


public class MyWebViewClient extends WebViewClient {
	private Activity activity;
	private ProgressDialog progressDialog;
	private WebView webView;
	private boolean startPageProgress;
	private boolean startPageFinished;
	
	public MyWebViewClient(Activity activity, WebView webView, boolean startPageProgress) {
		this.activity = activity;
		this.webView = webView;
		this.startPageProgress = startPageProgress;
		this.startPageFinished = false;

		
		progressDialog = new ProgressDialog(activity, ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setTitle(activity.getString(R.string.app_name));
		progressDialog.setMessage(activity.getString(R.string.please_wait));
		progressDialog.setCancelable(true);
	}
	
	public void startPageFinished() {
		
	}
		
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		
		if(startPageFinished == false) {
			startPageFinished = true;
			startPageFinished();
		}
		
		
		
		
		
		VolleyDialog.dismissProgressDialog();
		
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		
		if(startPageProgress == false) {
			startPageProgress = true;
		} else {
			/*
			VolleyDialog.dismissProgressDialog();
			VolleyDialog.showProgressDialog((FragmentActivity) activity, new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					
				}
			});
			*/
		}

	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		// view.loadUrl("file:///android_asset/error.html");
		
		
		//VolleyDialog.dismissProgressDialog();

		
		

		
//		ImageView connectionError = (ImageView) activity.findViewById(R.id.connectionError);
//		connectionError.setVisibility(View.VISIBLE);
//		webView.setVisibility(View.GONE);
//		connectionError.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				webView.reload();
//				webView.setVisibility(View.VISIBLE);
//				v.setVisibility(View.GONE);
//			}
//		});
		

		// webProgressBar.setVisibility(View.GONE);
		// Toast.makeText(WebViewActivity.this, "Received Error",
		// Toast.LENGTH_SHORT).show();
		// webView.reload();
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		try {
			activity.startActivity(intent);
			return true;
		} catch (ActivityNotFoundException e) {
			return false;
		}
	}

	

}

