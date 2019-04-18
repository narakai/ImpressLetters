package com.jeycorp.impressletters.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.jeycorp.impressletters.adapter.GoodsImagePagerAdapter;
import com.jeycorp.impressletters.define.DeveloperKey;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.param.GetBoardParam;
import com.jeycorp.impressletters.param.GetCommentParam;
import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.result.GetGoodsBoardResult;
import com.jeycorp.impressletters.type.GoodsComment;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.AppImgResize;
import com.jeycorp.impressletters.utill.ImageSelect;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.utill.SettingPreferenceManager;
import com.jeycorp.impressletters.utill.TimeUtil;
import com.jeycorp.impressletters.volleyimage.NetworkGifImageView;
import com.jeycorp.impressletters.volleyimage.NetworkImageViewFull;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.adapter.GoodsCommentAdapter;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.type.GoodsBoard;
import com.jeycorp.impressletters.utill.JAlertConfirm;
import com.jeycorp.impressletters.view.GoodsBottomView;
import com.jeycorp.impressletters.volley.VolleyJsonActivityHelper;
import com.jeycorp.impressletters.volley.VolleyQueue;
import com.jeycorp.impressletters.volleyimage.ImageSyncSize;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GoodsDetailActivity extends YouTubeFailureRecoveryActivity {
    private PreferenceManagers pref;
    private List<GoodsComment> goodsCommentList;
    private List<GoodsBoard> goodsBoardList;
    private View header,footer;
    private ListView listView;
    private GoodsCommentAdapter adapter;
    private GoodsBoard goodsBoard;
    private GoodsBottomView goodsBottomView;
    private GoodsComment goodsComment;
    private int startRow;
    private boolean isInit = true;
    private long seq;
    private String movieUrl="";
    InterstitialAd mInterstitialAd;

    private ViewPager mViewPager;
    private CountDownTimer timer;
    private int currentPosition;
    private LinearLayout bottomLayout;
    private RequestManager mGlideRequestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        setInitView();
        getGoodsUrl();


        new Analytics(getApplication()).getOutputEvent("부자되는 글   상세화면");
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(movieUrl);
        }
    }
    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) header.findViewById(R.id.movieView);
    }

    private void setInitView(){
        pref = new PreferenceManagers(this);

        listView = (ListView)findViewById(R.id.listView);
        goodsBottomView = new GoodsBottomView(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        header = inflater.inflate(R.layout.header_goods_board,null,false);
        footer = inflater.inflate(R.layout.footer_goods_board,null,false);


        listView.addHeaderView(header);
        listView.addFooterView(footer);

        goodsCommentList = new ArrayList<GoodsComment>();
        adapter = new GoodsCommentAdapter(this,goodsCommentList, ValueDefine.ADAPTER_DETAIL);
        adapter.setActivity(this);
        listView.setAdapter(adapter);

        pref.setAdCount(pref.getAdCount()+1);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.full_banner_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder().build(); //새 광고요청
        mInterstitialAd.loadAd(adRequest); //요청한 광고를 load 합니다.
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                SettingPreferenceManager settingPreferenceManager = new SettingPreferenceManager(GoodsDetailActivity.this);
                if(pref.getAdCount()>=5){
                    if(settingPreferenceManager.getViewAd().equals("ON")){
                        mInterstitialAd.show();
                    }
                    pref.setAdCount(0);
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            seq = bundle.getLong("seq",0);
            boolean isComment = bundle.getBoolean("comment",false);
            if(isComment){
                goodsBottomView.setVisibleKeyBoard();
            }
        }

        findViewById(R.id.btnCommentView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GoodsDetailActivity.this,CommentListActivity.class);
                intent.putExtra("seq",seq);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        mGlideRequestManager = Glide.with(this);
    }
    private void setInitList(){
        startRow = 0;
        goodsCommentList.clear();
        adapter.notifyDataSetChanged();
    }
    public void viewProgressLoading(int v){
        findViewById(R.id.progressLoading).setVisibility(v);
    }

    private void getHeaderView(final GoodsBoard goodsBoard){
        final NetworkImageViewFull imgPicture = (NetworkImageViewFull)header.findViewById(R.id.imgPicture);
        NetworkGifImageView imgPictureGif = (NetworkGifImageView)header.findViewById(R.id.imgPictureGif);
        YouTubePlayerView youTubeView = (YouTubePlayerView) header.findViewById(R.id.movieView);

        ImageLoader imageLoader = VolleyQueue.getImageLoader();

        TextView txtTitle = (TextView)header.findViewById(R.id.txtTitle);
        TextView txtDate = (TextView)header.findViewById(R.id.txtDate);
        TextView txtReadCount = (TextView)header.findViewById(R.id.txtReadCount);
        WebView webContents = (WebView)header.findViewById(R.id.webContent);
        webContents.getSettings().setJavaScriptEnabled(true);

        webContents.loadDataWithBaseURL(null, goodsBoard.getContents(), "text/html", "UTF-8", null);
        youTubeView.setVisibility(View.GONE);



        webContents.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult hr = ((WebView)v).getHitTestResult();
                if(hr.getType()==5){
                    final String url = hr.getExtra();
                    ImageSelect imageSelect = new ImageSelect(GoodsDetailActivity.this);
                    imageSelect.selectDialog(ValueDefine.DETAIL_WEB_IMAGE,goodsBoard,url);
                }
                return false;
            }
        });


        if(goodsBoard.getType().equals(ValueDefine.GOOD_TYPE_WRITE)){
            if(goodsBoard.getImgThumbUrl()!=null && !goodsBoard.getImgThumbUrl().equals("")){
                if(goodsBoard.getImgThumbUrl().contains(".gif")){
                    imgPicture.setVisibility(View.GONE);
                    imgPictureGif.setVisibility(View.VISIBLE);
                    imgPictureGif.setImageUrl(UrlDefine.DATA+goodsBoard.getImgUrl());
                    imgPictureGif.startAnimation();

                    imgPictureGif.setOnLongClickListener(longClickListener);
                }else{
                    imgPictureGif.setVisibility(View.GONE);
                    imgPicture.setVisibility(View.VISIBLE);
                    imgPicture.setImageUrl(UrlDefine.DATA+goodsBoard.getImgUrl(),imageLoader);
                    //imgPicture.setImageUrl(UrlDefine.SERVER+goodsBoard.getImgThumbUrl(),imageLoader);

                    imgPicture.setOnLongClickListener(longClickListener);
                }
            }else{
                imgPicture.setVisibility(View.GONE);
                imgPictureGif.setVisibility(View.GONE);
            }

        }else if(goodsBoard.getType().equals(ValueDefine.GOOD_TYPE_MOVIE)){
            movieUrl = goodsBoard.getMovieUrl();
            imgPicture.setVisibility(View.GONE);
            imgPictureGif.setVisibility(View.GONE);
            youTubeView.setVisibility(View.VISIBLE);
            youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        }

        Timestamp date = Timestamp.valueOf(goodsBoard.getCreateDate());
        long regTime = date.getTime();
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;

        TimeUtil timeUtil = new TimeUtil(GoodsDetailActivity.this);
        String time =timeUtil.getFormatTime(diffTime);

        txtTitle.setText(goodsBoard.getTitle());
        txtDate.setText(goodsBoard.getCreateDate());

        if(goodsBoard.getReservationDate()!=null){
            if(!goodsBoard.getReservationDate().equals("0000-00-00 00:00:00")){
                txtDate.setText(goodsBoard.getReservationDate());
            }
        }
        txtReadCount.setText(String.valueOf(goodsBoard.getReadCount()));

        Button btnBookmark = (Button)findViewById(R.id.btnBookmark);
        ImageView imgLike = (ImageView)findViewById(R.id.imgLike);
        if(goodsBoard.getBookCount()>0){
            //btnBookmark.setTextColor(Color.parseColor("#FFBB00"));
            btnBookmark.setBackgroundResource(R.drawable.page_top_bookmark_on);
        }
        if(goodsBoard.getMyLikeCount()>0){
            imgLike.setBackgroundResource(R.drawable.page_under_like_on);
        }


    }
    private void setBottomLayout(int position) {
        currentPosition = position;
        timer.cancel();
        timer.start();
        bottomLayout.removeAllViews();
        for (int i = 0; i < goodsBoardList.size()-1; i++) {
            ImageView img = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    30, 30);
            img.setLayoutParams(params);
            if (position == i) {
                img.setBackgroundResource(R.drawable.pop_03);
            } else {
                img.setBackgroundResource(R.drawable.pop_02);
            }
            bottomLayout.addView(img);
        }
    }
    public void getGoodsUrl(){
        GetBoardParam getBoardParam = new GetBoardParam();

        getBoardParam.setStartRow(startRow);
        getBoardParam.setFetchSize(10);
        getBoardParam.setUid(pref.getUid());
        Log.e("post1",""+pref.getUid());
        getBoardParam.setSeq(seq);
        Log.e("post2",""+getBoardParam.getSeq());

        VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> getGoodsDetailHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(this);
        getGoodsDetailHelper.request(UrlDefine.API_GET_GOODS_DETAIL, getBoardParam, GetGoodsBoardResult.class, getGoodsDetailHelperListener, isInit, true, true);
        isInit = false;
    }
    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam,GetGoodsBoardResult> getGoodsDetailHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            viewProgressLoading(View.GONE);
            List<GoodsComment> list = getGoodsBoardResult.getGoodsCommentList();

            goodsBoard = getGoodsBoardResult.getGoodsBoard();
            goodsBoardList = getGoodsBoardResult.getGoodsBoardList();

            getHeaderView(getGoodsBoardResult.getGoodsBoard());
            goodsBottomView.setBottomView(getGoodsBoardResult.getGoodsBoard());

            if(goodsBoardList!=null){
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int deviceWidth = displayMetrics.widthPixels;

                AppImgResize appImgResize = new AppImgResize(GoodsDetailActivity.this, deviceWidth,(int)(deviceWidth/2.5f));
                //AppImgResize appImgResize = new AppImgResize(GoodsDetailActivity.this,510,640);
                currentPosition = 0;

                bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
                mViewPager = (ViewPager)footer.findViewById(R.id.pager);
                footer.findViewById(R.id.viewFooter).setVisibility(View.VISIBLE);
                appImgResize.resizeHeight(mViewPager);
                GoodsImagePagerAdapter imagePagerAdapter = new GoodsImagePagerAdapter(GoodsDetailActivity.this,goodsBoardList,GoodsDetailActivity.this,mGlideRequestManager);
                mViewPager.setAdapter(imagePagerAdapter);

                timer = new CountDownTimer(2 * 1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        if (currentPosition == (goodsBoardList.size() - 1))
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

            }
            if(list!=null){
                TextView txtCommentCount = (TextView) header.findViewById(R.id.txtCommentCount);
                txtCommentCount.setText(String.valueOf(getGoodsBoardResult.getCommentCount()));

                findViewById(R.id.btnCommentView).setVisibility(View.VISIBLE);
                findViewById(R.id.linear_bottom).setVisibility(View.VISIBLE);

                listView.setVisibility(View.VISIBLE);
                if(list.size()>0){
                    for(GoodsComment goodsComment : list){
                        goodsCommentList.add(goodsComment);
                    }
                    adapter.notifyDataSetChanged();
                }
            }else{
                setInitList();
                listView.setVisibility(View.VISIBLE);


            }
        }
        @Override
        public void onMessage(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };
    public void setShareUrl(){
        GetBoardParam getBoardParam = new GetBoardParam();
        getBoardParam.setSeq(seq);

        VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> setShareHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(this);
        setShareHelper.request(UrlDefine.API_SET_SHARE, getBoardParam, GetGoodsBoardResult.class, setShareHelperListener, true, true, true);
    }
    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam,GetGoodsBoardResult> setShareHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            goodsBottomView.setShareCount(1);
            goodsBottomView.setShareCountView();

            ImageSyncSize syncSize = new ImageSyncSize(GoodsDetailActivity.this,goodsBoard);
            syncSize.excute(UrlDefine.DATA+goodsBoard.getImgBannerUrl());
//            KaKaoLink kaKaoLink = new KaKaoLink(GoodsDetailActivity.this);
//            kaKaoLink.sendKakao(goodsBoard);
        }
        @Override
        public void onMessage(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };
    public void setImageShare(String url){
        ImageSyncSize syncSize = new ImageSyncSize(GoodsDetailActivity.this,goodsBoard);
        syncSize.excuteImageUrl(url);

    }
    public void setLikeUrl(){
        boolean isLogin = getUserLogin();
        if(isLogin){
            GetBoardParam getBoardParam = new GetBoardParam();
            getBoardParam.setSeq(seq);
            getBoardParam.setUid(pref.getUid());

            VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> setLikeHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(this);
            setLikeHelper.request(UrlDefine.API_SET_GOODS_LIKE, getBoardParam, GetGoodsBoardResult.class, setLikeHelperListener, true, true, true);
        }
    }
    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam,GetGoodsBoardResult> setLikeHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            Toast.makeText(GoodsDetailActivity.this,getStr(R.string.goods_like_complete),Toast.LENGTH_SHORT).show();
            goodsBottomView.setLikeCount(1);
            goodsBottomView.setLikeCountView();
            ImageView imgLike = (ImageView)findViewById(R.id.imgLike);
            imgLike.setBackgroundResource(R.drawable.page_under_like_on);
        }
        @Override
        public void onMessage(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };
    public void setCommentUrl(String comment){
        GetCommentParam getCommentParam = new GetCommentParam();
        getCommentParam.setContents(comment);
        getCommentParam.setUserUid(pref.getUid());
        getCommentParam.setGoodsBoardSeq(seq);

        VolleyJsonActivityHelper<GetCommentParam, GetGoodsBoardResult> setCommentHelper = new VolleyJsonActivityHelper<GetCommentParam, GetGoodsBoardResult>(this);
        setCommentHelper.request(UrlDefine.API_SET_GOODS_COMMENT, getCommentParam, GetGoodsBoardResult.class, setCommentHelperListener, false, true, true);
    }
    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetCommentParam,GetGoodsBoardResult> setCommentHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetCommentParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetCommentParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            goodsCommentList.add(0,getGoodsBoardResult.getGoodsComment());
            adapter.notifyDataSetChanged();

            goodsBottomView.setCommentCount(1);
            goodsBottomView.setCommentCountView();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listView.setSelection(1);
                }
            }, 50);
        }
        @Override
        public void onMessage(GetCommentParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetCommentParam getBoardParam, VolleyError error) {
        }
    };
    private void setBookMarkUrl(){
        boolean isLogin = getUserLogin();
        if(isLogin){
            GetBoardParam getBoardParam = new GetBoardParam();
            getBoardParam.setUid(pref.getUid());
            getBoardParam.setSeq(seq);

            VolleyJsonActivityHelper<GetBoardParam, BaseResult> setBookMarkHelper = new VolleyJsonActivityHelper<GetBoardParam, BaseResult>(this);
            setBookMarkHelper.request(UrlDefine.API_SET_BOOKMARK, getBoardParam, BaseResult.class, setBookMarkHelperListener, true, true, true);
        }

    }
    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam,BaseResult> setBookMarkHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, BaseResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, BaseResult baseResult) {
            Button btnBookmark = (Button)findViewById(R.id.btnBookmark);
            if(goodsBoard.getBookCount()==0){
                goodsBoard.setBookCount(1);
                btnBookmark.setBackgroundResource(R.drawable.page_top_bookmark_on);
                //btnBookmark.setTextColor(Color.parseColor("#FFBB00"));
            }else{
                goodsBoard.setBookCount(0);
                btnBookmark.setBackgroundResource(R.drawable.page_top_bookmark);
                //btnBookmark.setTextColor(Color.parseColor("#9a9a9a"));
            }
            Toast.makeText(GoodsDetailActivity.this,baseResult.getResultMessage(),Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onMessage(GetBoardParam getBoardParam, BaseResult baseResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };
    public void deleteCommentUrl(final GoodsComment item){
        goodsComment = item;
        new JAlertConfirm(GoodsDetailActivity.this, getStr(R.string.comment_delete_q), false) {
            @Override
            protected void onYes() {
                super.onYes();
                GetCommentParam getCommentParam = new GetCommentParam();
                getCommentParam.setSeq(item.getSeq());
                getCommentParam.setGoodsBoardSeq(seq);

                VolleyJsonActivityHelper<GetCommentParam, BaseResult> setDeleteCommentkHelper = new VolleyJsonActivityHelper<GetCommentParam, BaseResult>(GoodsDetailActivity.this);
                setDeleteCommentkHelper.request(UrlDefine.API_SET_DELETE_COMMENT, getCommentParam, BaseResult.class, setDeleteCommentHelperListener, true, true, true);
            }
        };
    }
    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetCommentParam,BaseResult> setDeleteCommentHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetCommentParam, BaseResult>() {
        @Override
        public void onSuccess(GetCommentParam getBoardParam, BaseResult baseResult) {
            goodsCommentList.remove(goodsComment);
            adapter.notifyDataSetChanged();

            goodsBottomView.setCommentCount(-1);
            goodsBottomView.setCommentCountView();
        }
        @Override
        public void onMessage(GetCommentParam getBoardParam, BaseResult baseResult) {
        }

        @Override
        public void onError(GetCommentParam getBoardParam, VolleyError error) {
        }
    };
    private boolean getUserLogin(){
        boolean isLogin = true;
        if(pref.getUid().equals("")){
            new JAlertConfirm(GoodsDetailActivity.this, getStr(R.string.setting_login_q), false) {
                @Override
                protected void onYes() {
                    super.onYes();
                    Intent intent = new Intent(GoodsDetailActivity.this,KaKaoActivity.class);
                    intent.putExtra("type", ValueDefine.KAKAO_DETAIL);
                    intent.putExtra("seq",seq);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            };
            isLogin = false;
        }
        return isLogin;
    }
    public void moveActivity(GoodsComment item){
        Intent intent = new Intent(GoodsDetailActivity.this, CommentModifyActivity.class);
        intent.putExtra("seq",item.getSeq());
        intent.putExtra("contents",item.getContents());
        intent.putExtra("parentSeq",seq);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            switch (view.getId()){
                case R.id.imgPictureGif:
                case R.id.imgPicture:

                    ImageSelect imageSelect = new ImageSelect(GoodsDetailActivity.this);
                    imageSelect.selectDialog(ValueDefine.DETAIL_MAIN_IMAGE,goodsBoard,UrlDefine.DATA+goodsBoard.getImgThumbUrl());

                    break;
            }
            return false;
        }
    };
    public void menuClick(View v){
        switch(v.getId()){
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnBookmark:
                setBookMarkUrl();
                new Analytics(getApplication()).getClickEvent("부자되는 글   상세화면", "북마크 클릭 이벤트", "0");
                break;
        }
    }
}
