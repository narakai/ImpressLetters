package com.jeycorp.impressletters.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.jeycorp.impressletters.utill.MyWebChromeClient;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;

public class QuestionWebViewActivity extends BaseActivity {

    private WebView webViewGuide;
    private ImageView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_question_web_view);

            getWebView();

            findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    private void getWebView() {
        webViewGuide = (WebView) activity.findViewById(R.id.webViewGuide);
        webViewGuide.getSettings().setJavaScriptEnabled(true);
        webViewGuide.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webViewGuide.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webViewGuide.getSettings().setSupportZoom(false);
        webViewGuide.getSettings().setLoadWithOverviewMode(true);

        webViewGuide.setWebChromeClient(new MyWebChromeClient(activity, null));
        // webViewGuide.setWebViewClient(new MyWebViewClient(activity,webViewGuide,false));

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


                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.rotate);
                animation.setRepeatCount(Animation.INFINITE);
                progressBar = (ImageView) activity.findViewById(R.id.progressBars);
                progressBar.startAnimation(animation);
                /*
                progressBar = (ImageView) activity.findViewById(R.id.progressBars);
                progressBar.setBackgroundResource(R.drawable.loading);
                AnimationDrawable animation = (AnimationDrawable) progressBar.getBackground();
                animation.start();
                */
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                    activity.findViewById(R.id.imageViewGuideProgressBar).clearAnimation();
                activity.findViewById(R.id.progressBars).clearAnimation();
                activity.findViewById(R.id.layoutGuideProgressBar).setVisibility(View.GONE);
                webViewGuide.setVisibility(View.VISIBLE);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
//                    activity.findViewById(R.id.imageViewGuideProgressBar).clearAnimation();
                activity.findViewById(R.id.progressBars).clearAnimation();
                activity.findViewById(R.id.layoutGuideProgressBar).setVisibility(View.GONE);
                webViewGuide.setVisibility(View.GONE);
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
        });

        webViewGuide.loadUrl(UrlDefine.FAQ);
    }

}
