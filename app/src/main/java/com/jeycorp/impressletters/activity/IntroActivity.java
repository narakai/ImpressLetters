package com.jeycorp.impressletters.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import gradient.mylibrary.GradienTextView;
import gradient.mylibrary.Orientation;

public class IntroActivity extends BaseActivity {
    private PreferenceManagers pref;
    private User user;
    //private FusedLocationHelper fusedLocationHelper;
    private WeatherLocation weatherLocation;
    private InterstitialAd mInterstitialAd;
    private Intent mIntent = null;
    private SettingPreferenceManager settingPreferenceManager;
    private boolean isInit = true;
    int comment_index;
    int name_intdex;
    GradienTextView fading_txt,fading_txt2;
    int text_speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_intro);
            setInitView();
//            setAnimationLogo();
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
        final AlphaAnimation animation = new AlphaAnimation(0f, 1.0f);
        animation.setDuration(1000);




        final TextView txtname = findViewById(R.id.txtname);

//        txtname.setVisibility(View.INVISIBLE);




       fading_txt = findViewById(R.id.fading_txt);


       fading_txt2 = findViewById(R.id.fading_txt2);

        ArrayList<String> comments = new ArrayList<>();
        comments.add("다른사람의 좋은 습관을");
        comments.add("사람들과 쉽게 포옹하라.");
        comments.add("더이상 돈을 위해 일할 필요가");
        comments.add("책과 신문속에 부가있다.");
        comments.add("가난하게 태어난것은 당신의 실수가 아니다.");
        comments.add("빌려주지 않아서 잃는 친구보다");
        comments.add("만족할 줄 아는 사람은 부자고,");
        comments.add("버는 것 보다 적게 쓰는 법을 안다면");
        comments.add("돈은 흐르는 물과 같다.");
        comments.add("지금의 10억 달러는");
        comments.add("지식에 대한 투자는 최고의 수익률로 보답한다.");
        comments.add("행운의 여신은 집착한다고 오지 않는다.");
        comments.add("돈은 형편없는 주인이기도 하지만");
        comments.add("이상으로 시작에서 현실로 마무리를 지어라.");

        final ArrayList<String> comments2 = new ArrayList<>();
        comments2.add("내 습관으로 만들어라.");
        comments2.add("");
        comments2.add("없을 때 까지 일하라.");
        comments2.add("");
        comments2.add("그러나 죽을때도 가난한 것은 당신의 실수다.");
        comments2.add("빌려주어서 잃는 친구가 더 많다.");
        comments2.add("탐욕스러운 사람은 가난한 사람이다.");
        comments2.add("현자의 돌을 가진 것과 같다.");
        comments2.add("가둬 두면 썩고 만다.");
        comments2.add("예전의 그 10억 달러가 아니다.");
        comments2.add("");
        comments2.add("그렇지만 열심히 일하는 사람에게는 항상 미소를 짓는다.");
        comments2.add("훌륭한 하인도 될 수 있다.");
        comments2.add("");

        final ArrayList<String> names = new ArrayList<>();
        names.add("- 빌게이츠 -");
        names.add("- 오프라 윈프리 -");
        names.add("- 월트 디즈니 -");
        names.add("- 워렌 버핏 -");
        names.add("- 빌게이츠 -");
        names.add("- 쇼펜 하우어 -");
        names.add("- 솔론 -");
        names.add("- 벤자민 프랭클린 -");
        names.add("- 모하메드 알 마코툼 -");
        names.add("- 벙커 헌트 -");
        names.add("- 벤자민 프랭클린 -");
        names.add("- 비버브룩 경 -");
        names.add("- P.T. 넘 -");
        names.add("- 칼 알브레히트 -");









        SimpleDateFormat df = new SimpleDateFormat("D", Locale.KOREA);
        String str_date = df.format(new Date());

         comment_index = Integer.parseInt(str_date);
//        comment_index = 4;
        if(comment_index>13){
            comment_index = comment_index % 13;
        }



        fading_txt.setText(comments.get(comment_index));
        fading_txt.setTextSize(17);

        final int text_length = fading_txt.getText().toString().length();
        final int text_length2 = fading_txt.getText().toString().length();
       text_speed =140;
        fading_txt.start(Orientation.LEFT_TO_RIGHT_FORME_NONE,text_length*text_speed);
        fading_txt.setOrientation(Orientation.LEFT_TO_RIGHT_FORME_NONE);
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
        animator.setDuration(text_length*text_speed);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float)animation.getAnimatedValue()).floatValue();

                fading_txt.setCurrentProgress(value);
            }
        });

//        animator.start();
            fading_txt.setAnimation(animation);







        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fading_txt2.setText(comments2.get(comment_index));
                fading_txt2.start(Orientation.LEFT_TO_RIGHT_FORME_NONE,text_length2*text_speed);
                fading_txt2.setOrientation(Orientation.LEFT_TO_RIGHT_FORME_NONE);
                ValueAnimator animator2 = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
                animator2.setDuration(text_length2*text_speed);
                animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = ((Float)animation.getAnimatedValue()).floatValue();
                        fading_txt2.setCurrentProgress(value);

                    }
                });
//                animator2.start();
                fading_txt2.setAnimation(animation);
            }
        },  0);





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                txtname.setVisibility(View.VISIBLE);
                AlphaAnimation animation2 = new AlphaAnimation(0f, 1.0f);
                animation2.setDuration(1500);
                txtname.setText(names.get(comment_index));
                txtname.setAnimation(animation2);
            }
        },  text_speed*text_length-text_speed*8);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //initAdView();
                setMyLocationTemp();
                getIntroUrl();
            }
        }, text_speed*text_length);



        Date  date = new Date();
        date.getDate();


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

//    private void setAnimationLogo() {
//        /*
//        ImageView imageView = (ImageView)findViewById(R.id.imgLogo);
//        Animation slowAppear,slowDisappear;
//        slowDisappear = AnimationUtils.loadAnimation(activity,R.anim.slowly_disappear);
//        slowAppear = AnimationUtils.loadAnimation(activity,R.anim.slowly_appear);
//        imageView.setAnimation(slowDisappear);
//        imageView.setAnimation(slowAppear);
//        */
//        ImageView imageView = (ImageView) findViewById(R.id.imgIntro);
//        imageView.setBackgroundResource(R.drawable.intro);
//        AnimationDrawable animation = (AnimationDrawable) imageView.getBackground();
//        animation.start();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //initAdView();
//                setMyLocationTemp();
//                getIntroUrl();
//            }
//        }, 2500);
//
//    }

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



