package com.jeycorp.impressletters.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.jeycorp.impressletters.utill.MyWebChromeClient;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.volley.VolleyDialog;

/**
 * Created by park-jw on 2017-02-17.
 */
public class Guide {
    private Activity activity;
    private WebView webViewGuide;
    private boolean loadComplete = false;
    private ImageView progressBar;
    public Guide(Activity activity) {
        this.activity = activity;
    }

    Handler timeoutHandler = new Handler() {
        public void handleMessage(Message msg) {
            try {
                if (loadComplete == false) {
                    if (webViewGuide != null) {
                        webViewGuide.setVisibility(View.GONE);
                        webViewGuide.destroy();
                        activity.findViewById(R.id.layoutGuideProgressBar).setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {

            }
        }
    };


    @SuppressLint("JavascriptInterface")
    public void showFirst() {
        timeoutHandler.sendEmptyMessageDelayed(0, 5000);


        try {

            webViewGuide = (WebView) activity.findViewById(R.id.webViewGuide);
            webViewGuide.getSettings().setJavaScriptEnabled(true);
            webViewGuide.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webViewGuide.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webViewGuide.getSettings().setSupportZoom(false);
            webViewGuide.getSettings().setLoadWithOverviewMode(true);

            webViewGuide.setWebChromeClient(new MyWebChromeClient(activity, null));
            webViewGuide.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);

//                    Animation animationRotate = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//                    animationRotate.setRepeatCount(Animation.INFINITE);
//                    animationRotate.setInterpolator(new DecelerateInterpolator());
//                    animationRotate.setDuration(1000);
//
                    activity.findViewById(R.id.layoutGuideProgressBar).setVisibility(View.VISIBLE);
//                    activity.findViewById(R.id.imageViewGuideProgressBar).startAnimation(animationRotate);

                    progressBar = (ImageView) activity.findViewById(R.id.progressBars);
                    progressBar.setBackgroundResource(R.drawable.loading);
                    AnimationDrawable animation = (AnimationDrawable) progressBar.getBackground();
                    animation.start();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
//                    activity.findViewById(R.id.imageViewGuideProgressBar).clearAnimation();
                    activity.findViewById(R.id.progressBars).clearAnimation();
                    activity.findViewById(R.id.layoutGuideProgressBar).setVisibility(View.GONE);
                    webViewGuide.setVisibility(View.VISIBLE);
                    loadComplete = true;
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    super.onReceivedError(view, errorCode, description, failingUrl);
//                    activity.findViewById(R.id.imageViewGuideProgressBar).clearAnimation();
                    activity.findViewById(R.id.progressBars).clearAnimation();
                    activity.findViewById(R.id.layoutGuideProgressBar).setVisibility(View.GONE);
                    webViewGuide.setVisibility(View.GONE);
                }
            });


            webViewGuide.addJavascriptInterface(new Object() {
                @JavascriptInterface
                public String toString() {
                    return "carTax";
                }

                @JavascriptInterface
                public void sendMessage(String text) {
                    if ("endGuide".equals(text)) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        webViewGuide.setVisibility(View.GONE);
                                        webViewGuide.destroy();
                                        webViewGuide = null;
                                        PreferenceManagers pref = new PreferenceManagers(activity);
                                        pref.setGuide(true);
                                    }
                                });
                            }
                        }).start();

                    }
                }
            }, "Goodthings");

            webViewGuide.loadUrl(UrlDefine.GUIDE);
            webViewGuide.setFocusable(true);
        } catch (Exception e) {

        }
    }


//    public void show() {
//        try {
//
//            webViewGuide = (WebView) activity.findViewById(R.id.webViewGuide);
//            webViewGuide.getSettings().setJavaScriptEnabled(true);
//            webViewGuide.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//            webViewGuide.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            webViewGuide.getSettings().setSupportZoom(false);
//            webViewGuide.getSettings().setLoadWithOverviewMode(true);
//
//            webViewGuide.setWebChromeClient(new MyWebChromeClient(activity, null));
//            webViewGuide.setVisibility(View.VISIBLE);
//
//
//
//            webViewGuide.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                    super.onPageStarted(view, url, favicon);
//                    VolleyDialog.showProgressDialog((FragmentActivity) activity, onProgressCancelListener);
//                }
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
//                    VolleyDialog.dismissProgressDialog();
//                }
//
//                @Override
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                    super.onReceivedError(view, errorCode, description, failingUrl);
//                    VolleyDialog.dismissProgressDialog();
//                }
//            });
//
//
//            webViewGuide.addJavascriptInterface(new Object() {
//                @JavascriptInterface
//                public String toString() {
//                    return "carTax";
//                }
//
//                @JavascriptInterface
//                public void sendMessage(String text) {
//                    if ("endGuide".equals(text)) {
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                activity.runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        activity.finish();
//                                    }
//                                });
//                            }
//                        }).start();
//
//                    }
//                }
//            }, "carTax");
//
//            webViewGuide.loadUrl(UrlDefine.GUIDE);
//            webViewGuide.setFocusable(true);
//        } catch (Exception e) {
//
//        }
//    }

    DialogInterface.OnCancelListener onProgressCancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            VolleyDialog.dismissProgressDialog();
        }
    };

    public void destory() {
        if(webViewGuide != null) {
            webViewGuide.destroy();
            webViewGuide = null;
        }
    }

}
