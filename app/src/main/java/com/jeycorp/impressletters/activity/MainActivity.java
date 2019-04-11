package com.jeycorp.impressletters.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.adapter.SectionsPagerAdapter;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.utill.GcmPreferenceManager;
import com.jeycorp.impressletters.utill.JAlertConfirm;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.view.Guide;
import com.jeycorp.impressletters.view.LeftMenuView;


public class MainActivity extends BaseActivity {
    private final static int REQ_FINISH = 2;
    public static final int initPage = 0;
    private PreferenceManagers pref;
    private boolean exit = false;
    private DrawerLayout drawerLayout;
    private LeftMenuView leftMenuView;
    private View leftView;
    private boolean isMenuActive = false;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    //InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_main);
            setInitView();
            setPageView();
            //getGuide();

        /*
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.full_banner_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build(); //새 광고요청
        mInterstitialAd.loadAd(adRequest); //요청한 광고를 load 합니다.
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                finish();
            }
        });
        */


        }
    }

    @Override
    public void onBackPressed() {
        if (isMenuActive) {
            drawerLayout.closeDrawer(leftView);
            return;
        } else {
            Intent intent = new Intent(this, FinishPopupActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, REQ_FINISH);
        }

    }

    private void onFinish() {
        pref.setAppExcute(false);
        this.finish();
        /*
        if(mInterstitialAd.isLoaded()){
            SettingPreferenceManager settingPreferenceManager = new SettingPreferenceManager(activity);
            if(settingPreferenceManager.getFinishAd().equals("ON")){
                mInterstitialAd.show();
            }else{
                this.finish();
            }
        }else{
            this.finish();
        }
        */

    }

    private void setInitView() {
        pref = new PreferenceManagers(this);
        pref.setAppExcute(true);

        getPushCheck();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        leftView = (View) findViewById(R.id.left_layout);

        drawerLayout.setDrawerListener(myDrawerListener);
        leftMenuView = new LeftMenuView(this);
        leftMenuView.setInit();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("type") != null) {
                if (bundle.getString("type").equals(ValueDefine.MESSAGE_NOTICE)) {
                    Intent intent = new Intent(activity, NoticeActivity.class);
                    startActivity(intent);
                }
            }

            if (bundle.getLong("goodsSeq") != 0) {
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                intent.putExtra("seq", bundle.getLong("goodsSeq"));
                startActivity(intent);
            }
        }
    }

    private void getPushCheck() {
        GcmPreferenceManager gcmPreferenceManager = new GcmPreferenceManager(activity);
        gcmPreferenceManager.setPushCount(0);

        Intent intents = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intents.putExtra("badge_count", 0);
        // 뱃지에 나타날 아이콘의 패키지명  ex) day.ex
        intents.putExtra("badge_count_package_name", "com.jeycorp.impressletters");
        // 뱃지에 나타날 아이콘의 클래스명 ex) day.ex.mainActivity
        // 클래스는 아무거나 설정해도 눈에 나타나는 차이는 없습니다.
        intents.putExtra("badge_count_class_name", "com.jeycorp.impressletters.activity.IntroActivity");
        sendBroadcast(intents);
    }

    private void setPageView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(initPage);
        setPageChange(initPage);

        findViewById(R.id.menuGoodWrite).setOnClickListener(pageClick);
        findViewById(R.id.menuGoodBest).setOnClickListener(pageClick);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPageChange(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
    }

    private void setPageChange(int position) {
        LinearLayout menuGoodWrite = (LinearLayout) findViewById(R.id.menuGoodWrite);
        LinearLayout menuGoodBest = (LinearLayout) findViewById(R.id.menuGoodBest);

        TextView txtGoodWrite = (TextView) findViewById(R.id.txtGoodWrite);
        TextView txtGoodBest = (TextView) findViewById(R.id.txtGoodBest);

        menuGoodWrite.setBackgroundColor(getResources().getColor(R.color.white));
        menuGoodBest.setBackgroundColor(getResources().getColor(R.color.white));
        txtGoodWrite.setTextColor(getResources().getColor(R.color.menu_gray));
        txtGoodBest.setTextColor(getResources().getColor(R.color.menu_gray));

        findViewById(R.id.lineGoodWrite).setVisibility(View.GONE);
        findViewById(R.id.lineGoodBest).setVisibility(View.GONE);

        if (position == 0) {
            //findViewById(R.id.lineGoodWrite).setVisibility(View.VISIBLE);
            menuGoodWrite.setBackgroundColor(getResources().getColor(R.color.board_bottom_color));
            txtGoodWrite.setTextColor(getResources().getColor(R.color.white));
        } else {
            //findViewById(R.id.lineGoodBest).setVisibility(View.VISIBLE);
            menuGoodBest.setBackgroundColor(getResources().getColor(R.color.board_bottom_color));
            txtGoodBest.setTextColor(getResources().getColor(R.color.white));
        }

        sectionsPagerAdapter.setMoveTop(position);
    }

    private void getGuide() {
        if (pref.isGuide() == false) {
            new Guide(activity).showFirst();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_FINISH) {
                if (intent != null) {
                    String state = intent.getStringExtra("STATE");
                    if (state.equals("FINISH")) {
                        onFinish();

                    } else if (state.equals("NONE")) {
                        new JAlertConfirm(this,
                                getString(R.string.main_finish), false) {
                            @Override
                            protected void onYes() {
                                super.onYes();
                                onFinish();
                            }

                            @Override
                            protected void onCancel() {

                                super.onCancel();
                            }

                            ;

                        };
                    } else {
                        //webView.loadUrl(state);
                    }

                }
            }
        }
    }

    DrawerLayout.DrawerListener myDrawerListener = new DrawerLayout.DrawerListener() {
        public void onDrawerClosed(View drawerView) {
            isMenuActive = false;
        }

        public void onDrawerOpened(View drawerView) {
            isMenuActive = true;
        }

        public void onDrawerSlide(View drawerView, float slideOffset) {
        }

        public void onDrawerStateChanged(int newState) {
            String state;
            switch (newState) {
                case DrawerLayout.STATE_IDLE:
                    state = "STATE_IDLE";
                    break;
                case DrawerLayout.STATE_DRAGGING:
                    state = "STATE_DRAGGING";
                    break;
                case DrawerLayout.STATE_SETTLING:
                    state = "STATE_SETTLING";
                    break;
                default:
                    state = "unknown!";
            }
        }
    };

    public void topClick(View v) {
        switch (v.getId()) {
            case R.id.btnMenu:
                drawerLayout.openDrawer(leftView);
                break;
            case R.id.btnSearch:
                Intent intent = new Intent(activity, GoodsSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    View.OnClickListener pageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menuGoodWrite:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.menuGoodBest:
                    viewPager.setCurrentItem(1);
                    break;

            }
        }
    };

}
