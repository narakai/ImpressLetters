package com.jeycorp.impressletters.utill;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.jeycorp.impressletters.R;

public class MyWebChromeClient extends WebChromeClient{
	public final static int FILECHOOSER_RESULTCODE = 8282;
	private Activity activity;
	private WebView childView = null;
    private WebView childViewOld = null;
    private int webViewPopupCount = 0;
    private WebView webViewPopup = null;
    private ValueCallback<Uri> uploadMessage;

    
   // private LinearLayout popupContainer;
    

	public MyWebChromeClient(Context context) {
		this.activity = (Activity) context;
		//popupContainer = (LinearLayout) activity.findViewById(R.id.popupContainer);
	}
	
	public MyWebChromeClient(Context context, LinearLayout popupContainer) {
		this.activity = (Activity) context;
		//this.popupContainer = popupContainer;
	}
	
	
	/*@Override
	public void onProgressChanged(WebView view, int newProgress) {
		if(progressDialog != null) {
			if(newProgress == 100) {
				progressDialog.setVisibility(View.GONE);
			} else {
				if(progressBar.getVisibility() == View.GONE) {
					progressBar.setVisibility(View.VISIBLE);
				}
				progressBar.setProgress(newProgress);
			}
		}
	}*/
	
	
	
	public void onCloseWindow(WebView paramWebView)
    {
		Log.d("test","onCloseWindow");
		//popupContainer.setVisibility(View.GONE);
    	webViewPopupCount -= 1;
    	if (childView != null) {
    		//popupContainer.removeView(childView);
    	}
      
    	webViewPopup = childViewOld;
    	childViewOld = null;
    	childView = null;
    	
      
    	super.onCloseWindow(paramWebView);
    }
    
	
    public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg)
    {
    	webViewPopupCount += 1;
    	
    	Log.d("test", "onCreateWindow");
    	
    	childView = new WebView(activity);
    //	popupContainer.setVisibility(View.VISIBLE);
    	
    	WebSettings webSet = childView.getSettings();
    	
    	webSet.setJavaScriptEnabled(true);
    	webSet.setJavaScriptCanOpenWindowsAutomatically(true);
    	webSet.setSupportMultipleWindows(true);
    	webSet.setAllowFileAccess(true);
    	String userAgent = webSet.getUserAgentString();
		
		childView.setWebChromeClient(this);
		childView.setWebViewClient(new MyWebViewClient(activity, childView, true));
		
		
		//Top Margin
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

		
		//popupContainer.addView(childView, layoutParams);
		WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
		transport.setWebView(childView);
		resultMsg.sendToTarget();
		
		//this.childView.requestFocus();
		//this.childView.requestFocusFromTouch();
		this.childViewOld = webViewPopup;
		webViewPopup = this.childView;
		webViewPopup.addJavascriptInterface(new JavascriptInterFace(activity), "AppInterface");
		
		return true;
    }

    

	@SuppressLint("NewApi")
	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			final android.webkit.JsResult result) {

		AlertDialog.Builder b;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			b = new AlertDialog.Builder(view.getContext(),
					AlertDialog.THEME_HOLO_LIGHT);
		} else {
			b = new AlertDialog.Builder(view.getContext());
		}

		b.setTitle(activity.getString(R.string.app_name))
				.setMessage(message)
				.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						}).setCancelable(false).create().show();

		return true;
	}
	   
	@SuppressLint("NewApi")
	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			final android.webkit.JsResult result) {

		AlertDialog.Builder b;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			b = new AlertDialog.Builder(view.getContext(),
					AlertDialog.THEME_HOLO_LIGHT);
		} else {
			b = new AlertDialog.Builder(view.getContext());
		}

		b.setTitle(activity.getString(R.string.app_name))
				.setMessage(message)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						})
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								result.cancel();
							}
						}).setCancelable(false).create().show();

		return true;
	}
	   
	   
    public boolean canGoBack() {
    	boolean canGoBack = false;
    	if(webViewPopupCount > 0)
	    {
			if(webViewPopup != null)
			{
				canGoBack = true;
	        } 
		}
    	
    	return canGoBack;
    }
    
    public void goBack() {
    	if(webViewPopupCount > 0)
	    {
			if(webViewPopup != null)
			{
				if(webViewPopup.canGoBack()) {
					webViewPopup.goBack();
				} else {
					webViewPopup.loadUrl("javascript:window.close()");
				}
	        } 
		}
    }

    public void destroy() {
    	if(webViewPopup != null) {
    		webViewPopup.clearHistory();
    		webViewPopup.clearFormData();
    		webViewPopup.clearCache(false);
    		webViewPopup.destroy();
    		webViewPopup = null;
    	}
    }
    
    public int getPopupCount() {
    	return webViewPopupCount;
    }

    public void close() {
    	webViewPopup.loadUrl("javascript:window.close()");
    }
    
    public void setUploadMessage(ValueCallback<Uri> uploadMessage) {
    	this.uploadMessage = uploadMessage;
    }
    
    public ValueCallback<Uri> getUploadMessage() {
    	return this.uploadMessage;
    }

    public void removeUploadMessage() {
    	this.uploadMessage = null;
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
    	openFileChooser(uploadMsg, "");
    }
    // For Android 3.0+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
    	setUploadMessage(uploadMsg);
    	Intent i = new Intent(Intent.ACTION_GET_CONTENT);
    	i.addCategory(Intent.CATEGORY_OPENABLE);
    	i.setType("image/*");
    	activity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    // For Android 4.1+
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
    	openFileChooser(uploadMsg, "");
    }

}
