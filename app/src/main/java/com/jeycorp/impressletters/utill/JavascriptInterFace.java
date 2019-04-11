package com.jeycorp.impressletters.utill;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.FragmentActivity;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.jeycorp.impressletters.volley.VolleyDialog;


public class JavascriptInterFace {
	private final static int REQ_BOARD_WRITE = 2;
	private StringBuffer webViewBuffer;
	private StringBuffer url;
	private Activity activity;

	public JavascriptInterFace(Activity activity) {
		this.activity = activity;
	}

	@JavascriptInterface
	public void toast(String text) {
		Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
	}
	



//	@JavascriptInterface
//	public void getUserBlog(String id) {
//		Log.e("오랑로알","ㅇ로아로아:");
//		Intent intent = new Intent(activity.getApplicationContext(),ArtistHomeActivity.class);
//		intent.putExtra("uid", id);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		activity.startActivity(intent);
//	}

	// -------------------------------------------------------------------------
	// 무한스크롤시 리스트 정보 담기(브라우저 hash 로 처리 가능하지만 메모리 많이 먹음)
	// 일단은 hash 로 처리함.
	private void initBuffer() {
		if (webViewBuffer == null) {
			webViewBuffer = new StringBuffer();
		}

		if (url == null) {
			url = new StringBuffer();
		}
	}

	@JavascriptInterface
	public void setBuffer(String url, String text) {
		this.initBuffer();

		if (this.url.toString().equals(url) == false) {
			this.url.delete(0, this.url.length());
			this.url.append(url);
		}

		webViewBuffer.delete(0, webViewBuffer.length());
		webViewBuffer.append(text);
	}

	@JavascriptInterface
	public String getBuffer(String url) {
		this.initBuffer();

		if (this.url.toString().equals(url)) {
			return webViewBuffer.toString().trim();
		} else {
			webViewBuffer.delete(0, webViewBuffer.length());
			return "";
		}
	}




	@JavascriptInterface
	public void outEvent() {
		activity.finish();
	}


	
	@JavascriptInterface
	public void showProgressDialog() {
		VolleyDialog.showProgressDialog((FragmentActivity) activity, new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				
			}
		});
	}
	
	@JavascriptInterface
	public void dismissProgressDialog() {
		VolleyDialog.dismissProgressDialog();
	}
}
