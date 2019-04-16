package com.jeycorp.impressletters.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.adapter.SectionsPagerAdapter;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.fragment.GoodsWriteFragment;
import com.jeycorp.impressletters.fragment.MenuResultFragment;
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
    Context context;
    private String mTAG = "kng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_main);
            setInitView();
            setPageView();
            clickMenuItem();
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            //getGuide();
            context=this;

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

    public void clickMenuItem(){
        TextView txt_all =findViewById(R.id.txt_all);
        TextView txt_love =findViewById(R.id.txt_love);
        TextView txt_hope =findViewById(R.id.txt_hope);
        TextView txt_wish =findViewById(R.id.txt_wish);
        TextView txt_goodSaying =findViewById(R.id.txt_goodSaying);
        TextView txt_life =findViewById(R.id.txt_life);

        txt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // based on the current position you can then cast the page to the correct Fragment class and call some method inside that fragment to reload the data:
                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" +R.id.viewPager + ":" + viewPager.getCurrentItem());
                Log.e("현재 프래그먼트" ,""+viewPager.getCurrentItem() );
                Log.e("프래그먼트 상태" ,""+page );

                if (viewPager.getCurrentItem() == 0 && page != null) {
                    Log.e("타냐?","어");
                    GoodsWriteFragment goodsWriteFragment = new GoodsWriteFragment();
                    Bundle bundle = new Bundle();

                    bundle.putString("seq","");
                    Log.e("번들값",""+bundle);
                    Log.e(mTAG, "번들값 : " + bundle);

                    goodsWriteFragment.setArguments(bundle);
                    ((GoodsWriteFragment)page).setMoveTop();
                }
                viewPager.setVisibility(View.VISIBLE);

            }
        });

        txt_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("타냐?","어1");
                GoodsWriteFragment goodsWriteFragment = new GoodsWriteFragment();
                Bundle bundle = new Bundle();
                bundle.putString("seq","20");
                goodsWriteFragment.setArguments(bundle);
                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" +R.id.viewPager + ":" + viewPager.getCurrentItem());
                Log.e("현재 프래그먼트" ,""+viewPager.getCurrentItem() );
                Log.e("프래그먼트 상태" ,""+page );
                if (viewPager.getCurrentItem() == 0 && page != null) {
                    Log.e("타냐?","어2");

                    ((GoodsWriteFragment)page).setMoveTop();
                }

            }
        });

        txt_hope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("호프텍스트","1");
                Bundle bundle = new Bundle();
                bundle.putString("seq","38");
                GoodsWriteFragment goodsWriteFragment = new GoodsWriteFragment();
                goodsWriteFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.left_in, R.anim.right_out).add(R.id.contaienr, new MenuResultFragment(), "TodayFortuneSelectFragment").addToBackStack(null)
                        .commit();
                viewPager.setVisibility(View.GONE);

//                Intent intent = new Intent(MainActivity.this,Main3Activity.class);
//                startActivity(intent);



            }
        });

        txt_wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("seq","24");
                GoodsWriteFragment goodsWriteFragment = new GoodsWriteFragment();
                goodsWriteFragment.setArguments(bundle);
            }
        });

        txt_goodSaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("seq","36");
                GoodsWriteFragment goodsWriteFragment = new GoodsWriteFragment();
                goodsWriteFragment.setArguments(bundle);
            }
        });

        txt_life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("seq","39");
                GoodsWriteFragment goodsWriteFragment = new GoodsWriteFragment();
                goodsWriteFragment.setArguments(bundle);
            }
        });
    };

    public void menu_popup(){
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);

        LinearLayout container_menu = findViewById(R.id.container_menu);
        container_menu.setAnimation(animation);
    }

    @Override
    public void onBackPressed() {
        viewPager.setVisibility(View.VISIBLE);
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
