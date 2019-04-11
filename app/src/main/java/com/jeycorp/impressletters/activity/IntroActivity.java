package com.jeycorp.impressletters.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jeycorp.impressletters.utill.JUtil;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.utill.SettingPreferenceManager;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.param.GetUserParam;
import com.jeycorp.impressletters.result.GetUserResult;
import com.jeycorp.impressletters.type.Device;
import com.jeycorp.impressletters.type.Setting;
import com.jeycorp.impressletters.type.User;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.GcmPreferenceManager;
import com.jeycorp.impressletters.utill.JAlertConfirm;
import com.jeycorp.impressletters.utill.RegisterStatic;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;
import com.jeycorp.impressletters.weather.FusedLocationHelper;
import com.jeycorp.impressletters.weather.MyAddress;
import com.jeycorp.impressletters.weather.WeatherLocation;

public class IntroActivity extends BaseActivity {
    private PreferenceManagers pref;
    private User user;
    //private FusedLocationHelper fusedLocationHelper;
    private WeatherLocation weatherLocation;
    private InterstitialAd mInterstitialAd;
    private Intent mIntent = null;
    private SettingPreferenceManager settingPreferenceManager;
    private boolean isInit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_intro);
            setInitView();
            setAnimationLogo();
            new Analytics(getApplication()).getOutputEvent("인트로 화면");
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        // fusedLocationHelper.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*
        if(fusedLocationHelper!=null){
            fusedLocationHelper.stop();
        }
        */

    }

    private void setMyLocationTemp() {
        //fusedLocationHelper = new FusedLocationHelper(this, fusedLocationListener);
    }

    private FusedLocationHelper.FusedLocationListener fusedLocationListener = new FusedLocationHelper.FusedLocationListener() {
        @Override
        public void onNotSupportedService() {
        }

        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onAddressChanged(MyAddress myAddress) {
            try {
                String address = myAddress.getSido() + "," + myAddress.getDongLee() + "," + myAddress.getGugun();
                weatherLocation = new WeatherLocation(IntroActivity.this);
                if (address.equals(pref.getLocationCurrent())) {
                    weatherLocation.finishTask("location", 1, "", pref.getLocationCode());
                } else {
                    weatherLocation.getWeather(address);
                }

                pref.setLatitude(myAddress.getLatitude());
                pref.setLongitude(myAddress.getLongitude());
                pref.setLocationCurrent(address);

                if (myAddress != null) {
                    onStop();
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onConnected(Location location) {
            // TODO Auto-generated method stub

        }
    };

    private void setInitGcm() {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();
    }

    private void setInitView() {
        setInitGcm();
        pref = new PreferenceManagers(this);
        settingPreferenceManager = new SettingPreferenceManager(this);

        int[] imgArray = getXml(R.array.intro_img_array);
        int rand = (int) (Math.random() * imgArray.length);

        /*
        RelativeLayout bgView = (RelativeLayout)findViewById(R.id.linearBg);
        bgView.setBackgroundResource(imgArray[rand]);
        */

    }

    private void getIntroUrl() {
        Device device = JUtil.getDevice(activity);
        GetUserParam getUserParam = new GetUserParam();
        getUserParam.setUid(pref.getUid());
        getUserParam.setDevice(device);

        VolleyJsonHelper<GetUserParam, GetUserResult> getIntroHelper = new VolleyJsonHelper<GetUserParam, GetUserResult>(this);
        getIntroHelper.request(UrlDefine.API_GET_INTRO, getUserParam, GetUserResult.class, getIntroHelperListener, false, true, true);
    }

    public void initAdView() {
        if (isInit) {
            isInit = false;
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.full_banner_ad_unit_id));

            AdRequest adRequest = new AdRequest.Builder().build(); //새 광고요청
            mInterstitialAd.loadAd(adRequest); //요청한 광고를 load 합니다.

            if (settingPreferenceManager.getIntroAd().equals("ON")) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        goMain();
                    }

                    @Override
                    public void onAdLoaded() {
                        mInterstitialAd.show();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        goMain();
                    }
                });
            } else {
                goMain();
            }
        }
    }

    private void setAnimationLogo() {
        /*
        ImageView imageView = (ImageView)findViewById(R.id.imgLogo);
        Animation slowAppear,slowDisappear;
        slowDisappear = AnimationUtils.loadAnimation(activity,R.anim.slowly_disappear);
        slowAppear = AnimationUtils.loadAnimation(activity,R.anim.slowly_appear);
        imageView.setAnimation(slowDisappear);
        imageView.setAnimation(slowAppear);
        */
        ImageView imageView = (ImageView) findViewById(R.id.imgIntro);
        imageView.setBackgroundResource(R.drawable.intro);
        AnimationDrawable animation = (AnimationDrawable) imageView.getBackground();
        animation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //initAdView();
                setMyLocationTemp();
                getIntroUrl();
            }
        }, 2500);

    }

    public void goMain() {

        // finish();
        Intent intent = null;
        intent = new Intent(this, MainActivity.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intent.putExtra("push", getIntent().getBooleanExtra("push", false));
            intent.putExtra("type", getIntent().getStringExtra("type"));
            intent.putExtra("goodsSeq", getIntent().getLongExtra("goodsSeq", 0));
        }
        Uri targetUri = this.getIntent().getData();
        if (targetUri != null) {
            String schemaData = targetUri.getQueryParameter("seq");
            if (schemaData != null) {
                intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra("seq", Long.parseLong(schemaData));
            }
        }
        // mIntent = intent;
        onStartActivity(intent);

    }

    private void setSetting(Setting setting) {
        settingPreferenceManager.setIntroAd(setting.getIntroAd());
        settingPreferenceManager.setFinishAd(setting.getFinishAd());
        settingPreferenceManager.setViewAd(setting.getViewAd());
        settingPreferenceManager.setPopFinishAd(setting.getFinishPopAd());
        settingPreferenceManager.setPopFinishText(setting.getFinishPopText());
    }

    private void goUpdate() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(RegisterStatic.getUpdateUrl(activity));
        intent.setData(uri);
        onStartActivity(intent);
        finish();
    }

    private void checkAppUpdate() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {

        }
        if (packageInfo.versionCode < Integer.parseInt(RegisterStatic.getUpdateVersion(getApplicationContext()))) {
            new JAlertConfirm(activity, getStr(R.string.intro_msg_new_update), false) {
                @Override
                protected void onYes() {
                    super.onYes();
                    goUpdate();
                    finishApplication();
                }

                @Override
                protected void onCancel() {
                    super.onCancel();
                    finishApplication();
                }
            };
        } else {
            if (packageInfo.versionCode < Integer.parseInt(RegisterStatic.getLooseUpdateVersion(getApplicationContext()))) {
                new JAlertConfirm(activity, getStr(R.string.intro_msg_new_update), false) {
                    @Override
                    protected void onYes() {
                        super.onYes();
                        goUpdate();
                    }

                    @Override
                    protected void onCancel() {
                        super.onCancel();
                        //fusedLocationHelper.start();
                        /*
                        if(getGPScheck()==false){
                            initAdView();
                        }
                        */
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                initAdView();
                            }
                        }, 1000);
                    }
                };
            } else {
                // fusedLocationHelper.start();
                /*
                if(getGPScheck()==false){
                    initAdView();
                }
                */
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initAdView();
                    }
                }, 100);
            }
        }

    }

    private VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, GetUserResult> getIntroHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, GetUserResult>() {
        @Override
        public void onSuccess(GetUserParam getUserParam, GetUserResult getUserResult) {
            RegisterStatic.putRegister(getApplicationContext(), getUserResult.getRegister());
            user = getUserResult.getUser();

            Device device = getUserResult.getDevice();
            if (device != null) {
                GcmPreferenceManager gcmPreferenceManager = new GcmPreferenceManager(activity);
                if (device.getIsPush().equals("Y")) {
                    gcmPreferenceManager.setPush(true);
                } else {
                    gcmPreferenceManager.setPush(false);
                }
            }
            setSetting(getUserResult.getSetting());
            checkAppUpdate();
        }

        @Override
        public void onMessage(GetUserParam getUserParam, GetUserResult getUserResult) {

        }

        @Override
        public void onError(GetUserParam getUserParam, VolleyError error) {
        }
    };
}

