package com.jeycorp.impressletters.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyNativeAdInfoBuilder;
import com.fsn.cauly.CaulyNativeAdView;
import com.fsn.cauly.CaulyNativeAdViewListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;
import com.jeycorp.impressletters.utill.SettingPreferenceManager;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.type.Finish;
import com.jeycorp.impressletters.utill.RegisterStatic;

import java.util.List;

public class FinishPopupActivity extends BaseActivity implements CaulyNativeAdViewListener {
    private List<Finish> banners;
    private Context context = FinishPopupActivity.this;
    private ViewPager mViewPager;
    private LinearLayout bottomLayout;
    private LinearLayout mBtnConfirm, mBtnCancel, mBtnReview;
    private CountDownTimer timer;
    private int currentPosition;
    //ca-app-pub-3940256099942544/2247696110
    private static final String ADMOB_AD_UNIT_ID = "ca-app-pub-1410685208575107/5733209382";

    //카울리
    String APP_CODE = "9bjYc25v";// your app code which you are assigned.
    ViewGroup native_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            setContentView(R.layout.activity_finish_popup);
            native_container = (ViewGroup) findViewById(R.id.native_container);
            //mViewPager = (ViewPager) findViewById(R.id.pager);

            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null) {
                Intent intent = getIntent();
                intent.putExtra("STATE", "NONE");
                setResult(RESULT_OK, intent);
                finish();
            }
            //디비에 등록된 앱 번호
        /*
        PopupParam param = new PopupParam();
        param.setApp_seq(10);


        VolleyJsonHelper<PopupParam,GetFinishResult> getIntroHelper = new VolleyJsonHelper<PopupParam,GetFinishResult>(this);
        getIntroHelper.request(UrlDefine.GET_FINISH, param, GetFinishResult.class, getFinishHelperListener, false,true,true);
        */

            setInitView();

        }
    }

    private void setInitView() {
        bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);

        mBtnConfirm = (LinearLayout) findViewById(R.id.btn_confirm);
        mBtnCancel = (LinearLayout) findViewById(R.id.btn_cancel);
        mBtnReview = (LinearLayout) findViewById(R.id.btn_review);

        mBtnConfirm.setOnClickListener(clickListener);
        mBtnCancel.setOnClickListener(clickListener);
        mBtnReview.setOnClickListener(clickListener);

        setCaulyView();
        //setAdView();
    }

    private void setCaulyView() {
        SettingPreferenceManager settingPreferenceManager = new SettingPreferenceManager(activity);
        if (settingPreferenceManager.getPopFinishAd().equals("ON")) {
            findViewById(R.id.parent_view).setVisibility(View.VISIBLE);
            TextView txtPopText = (TextView) findViewById(R.id.txtPopText);
            txtPopText.setText(settingPreferenceManager.getPopFinishText());
        } else {
            Intent intent = getIntent();
            intent.putExtra("STATE", "NONE");
            setResult(RESULT_OK, intent);
            finish();
        }
        CaulyAdInfo adInfo = new CaulyNativeAdInfoBuilder(APP_CODE)
                .layoutID(R.layout.activity_cauly_view)
                .iconImageID(R.id.icon)
                .titleID(R.id.title)
                .subtitleID(R.id.subtitle)
                .sponsorPosition(R.id.sponsor, CaulyAdInfo.Direction.CENTER)
                .build();
        CaulyNativeAdView nativeAd = new CaulyNativeAdView(this);
        nativeAd.setAdInfo(adInfo);
        nativeAd.setAdViewListener(this);
        nativeAd.request();
    }

    // 네이티브애드가 없거나, 네트웍상의 이유로 정상적인 수신이 불가능 할 경우 호출이 된다.
    public void onFailedToReceiveNativeAd(CaulyNativeAdView adView, int errorCode, String errorMsg) {
    }

    // 네이티브애드가 정상적으로 수신되었을 떄, 호출된다.
    public void onReceiveNativeAd(CaulyNativeAdView adView, boolean isChargeableAd) {
        adView.attachToView(native_container);  //지정된 위치에 adView를 붙인다.
    }

    private void setAdView() {
        SettingPreferenceManager settingPreferenceManager = new SettingPreferenceManager(activity);
        if (settingPreferenceManager.getPopFinishAd().equals("ON")) {
            findViewById(R.id.parent_view).setVisibility(View.VISIBLE);
        } else {
            Intent intent = getIntent();
            intent.putExtra("STATE", "NONE");
            setResult(RESULT_OK, intent);
            finish();
        }


        AdLoader.Builder builder = new AdLoader.Builder(this, ADMOB_AD_UNIT_ID);
        builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
            @Override
            public void onContentAdLoaded(NativeContentAd ad) {
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
                NativeContentAdView adView = (NativeContentAdView) getLayoutInflater()
                        .inflate(R.layout.ad_content, null);
                populateContentAdView(ad, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });
        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       NativeContentAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.contentad_headline));
        adView.setImageView(adView.findViewById(R.id.contentad_image));
        adView.setBodyView(adView.findViewById(R.id.contentad_body));
        adView.setCallToActionView(adView.findViewById(R.id.contentad_call_to_action));
        adView.setLogoView(adView.findViewById(R.id.contentad_logo));
        adView.setAdvertiserView(adView.findViewById(R.id.contentad_advertiser));

        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }

    private void setBottomLayout(int position) {
        currentPosition = position;
        timer.cancel();
        timer.start();
        bottomLayout.removeAllViews();
        for (int i = 0; i < banners.size(); i++) {
            ImageView img = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            img.setLayoutParams(params);
            if (position == i) {
                img.setBackgroundResource(R.drawable.pop_03);
            } else {
                img.setBackgroundResource(R.drawable.pop_02);
            }
            bottomLayout.addView(img);
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_confirm:
                    Intent intent = getIntent();
                    intent.putExtra("STATE", "FINISH");
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case R.id.btn_cancel:
                    finish();
                    break;

                case R.id.btn_review:
                    Intent intent02 = new Intent(Intent.ACTION_VIEW, Uri.parse(RegisterStatic.getUpdateUrl(activity)));
                    startActivity(intent02);
                    finish();
                    break;

            }

        }
    };
/*
    private VolleyJsonHelper.VolleyJsonHelperListener<PopupParam,GetFinishResult> getFinishHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<PopupParam, GetFinishResult>() {
        @Override
        public void onSuccess(PopupParam getUserParam, GetFinishResult getBoardResult) {
            banners = getBoardResult.getBanners();
            if (banners != null) {
                findViewById(R.id.parent_view).setVisibility(View.VISIBLE);
                ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(
                        context, banners, FinishPopupActivity.this);
                mViewPager.setAdapter(imagePagerAdapter);

                timer = new CountDownTimer(2 * 1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (currentPosition == banners.size() - 1)
                            mViewPager.setCurrentItem(0);
                        else
                            mViewPager.setCurrentItem(currentPosition + 1);

                    }
                };

                setBottomLayout(0);

                mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageSelected(int arg0) {
                        setBottomLayout(arg0);
                    }
                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }
                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }
                });

            } else {
                Intent intent = getIntent();
                intent.putExtra("STATE", "NONE");
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        @Override
        public void onMessage(PopupParam getUserParam, GetFinishResult getBoardResult) {

        }

        @Override
        public void onError(PopupParam getUserParam, VolleyError error) {
        }
    };

*/
}
