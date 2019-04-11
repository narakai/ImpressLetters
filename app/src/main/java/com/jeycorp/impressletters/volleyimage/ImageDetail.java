package com.jeycorp.impressletters.volleyimage;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.jeycorp.impressletters.R;

public class ImageDetail extends Activity {
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_image_detail);

		setInitView();
	}

	private void setInitView() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String url = bundle.getString("url");

			mWebView = (WebView) findViewById(R.id.webView);
			mWebView.getSettings().setSupportZoom(true);
			mWebView.getSettings().setBuiltInZoomControls(true);
			mWebView.getSettings().setDisplayZoomControls(false);
			mWebView.setVerticalScrollBarEnabled(false);
			mWebView.setVerticalScrollbarOverlay(false);
			mWebView.setHorizontalScrollBarEnabled(false);
			mWebView.setHorizontalScrollbarOverlay(false);
			mWebView.setInitialScale(100);
			mWebView.loadDataWithBaseURL(null, htmlBody(url), "text/html",
					"utf-8", null);
		}

	}


	private String htmlBody(String url) {
		StringBuffer sb = new StringBuffer("<HTML>");
		sb.append("<HEAD>");
		sb.append("</HEAD>");
		sb.append("<BODY style='margin:0; padding:0; text-align:center;background-color:#000000'>");
		sb.append("<table width='100%' height='100%'><tr><td>");
		sb.append("<img width='100%' src=\"" + url + "\" style='margin:auto;'>");
		sb.append("</td></tr></table>");
		sb.append("</BODY>");
		sb.append("</HTML>");

		return sb.toString();

	}

}
