package com.jeycorp.impressletters.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.etsy.android.grids.StaggeredGridView;
import com.jeycorp.impressletters.activity.MainActivity;
import com.jeycorp.impressletters.adapter.GoodsBoardAdapter;
import com.jeycorp.impressletters.param.GetBoardParam;
import com.jeycorp.impressletters.result.GetGoodsBoardResult;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.JUtil;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.activity.KaKaoActivity;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.type.GoodsBoard;
import com.jeycorp.impressletters.utill.JAlertConfirm;
import com.jeycorp.impressletters.volley.VolleyJsonActivityHelper;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;
import com.jeycorp.impressletters.volleyimage.ImageSyncSize;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class GoodsStorageFragment extends Fragment {
    private PreferenceManagers pref;
    private Context context;
    private View view,header;
    private StaggeredGridView listView;
    private MainActivity activity;
    private List<GoodsBoard> goodsBoardList;
    private GoodsBoardAdapter adapter;
    private GoodsBoard goodsBoard;
    private long minSeq;
    private int startRow;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int imgWidth=0,imgHeight=0;


    public GoodsStorageFragment() {
    }

    public GoodsStorageFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods_storage,null);
        setInitView();
        getListUrl();
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(pref.isWeather()){
            header.findViewById(R.id.linearBg).setVisibility(View.VISIBLE);
        }else{
            header.findViewById(R.id.linearBg).setVisibility(View.GONE);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  MainActivity){
            activity = (MainActivity)context;
            new Analytics(activity.getApplication()).getOutputEvent("보관함 리스트 화면");
        }
    }
    private void setInitView(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        listView = (StaggeredGridView)view.findViewById(R.id.listView);
        pref = new PreferenceManagers(activity);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        header = inflater.inflate(R.layout.header_weather,null,false);

        int gridPadding = JUtil.getDpiToPixel(activity,3);
        listView.setGridPadding(gridPadding,0,gridPadding,gridPadding);

        //listView.addHeaderView(header);

        goodsBoardList = new ArrayList<GoodsBoard>();
        adapter = new GoodsBoardAdapter(activity,goodsBoardList);
        adapter.setInit(this,activity);
        listView.setAdapter(adapter);

        setInitList();
    }
    public void setMoveTop(){
        setInitList();
        getListUrl();
    }
    public void getHeaderView(String weatherWord){
//        WeatherHeaderView weatherHeaderView = new WeatherHeaderView(activity,this,header,0,0);
//        weatherHeaderView.getView(weatherWord);

    }

    private void setInitList(){
        minSeq = 0;
        startRow = 0;
        goodsBoardList.clear();
        adapter.notifyDataSetChanged();
    }
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            setInitList();
            getListUrl();
        }
    };
    public void viewProgressLoading(int v){
        view.findViewById(R.id.progressLoading).setVisibility(v);
    }
    private boolean getUserLogin(){
        boolean isLogin = true;
        if(pref.getUid().equals("")){
            new JAlertConfirm(activity, activity.getStr(R.string.setting_login_q), false) {
                @Override
                protected void onYes() {
                    super.onYes();
                    Intent intent = new Intent(activity,KaKaoActivity.class);
                    intent.putExtra("type", ValueDefine.KAKAO_MAIN);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            };
            isLogin = false;
        }
        return isLogin;
    }

    public void setShareUrl(GoodsBoard goodsBoard, int imgWidth, int imgHeight){
        this.imgWidth = imgWidth;
        this.imgHeight = imgWidth;

        this.goodsBoard = goodsBoard;
        GetBoardParam getBoardParam = new GetBoardParam();
        getBoardParam.setSeq(goodsBoard.getSeq());

        VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> setShareHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(activity);
        setShareHelper.request(UrlDefine.API_SET_SHARE, getBoardParam, GetGoodsBoardResult.class, setShareHelperListener, true, true, true);

    }
    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam,GetGoodsBoardResult> setShareHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            goodsBoard.setShareCount(goodsBoard.getShareCount()+1);
            adapter.notifyDataSetChanged();

            ImageSyncSize syncSize = new ImageSyncSize(activity,goodsBoard);
            syncSize.excute(UrlDefine.SERVER+goodsBoard.getImgBannerUrl());
//            KaKaoLink kakaoLink = new KaKaoLink(activity);
//            kakaoLink.sendKakao(goodsBoard);
        }
        @Override
        public void onMessage(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };
    public void setLikeUrl(GoodsBoard goodsBoard){
        this.goodsBoard = goodsBoard;

        boolean isLogin = getUserLogin();
        if(isLogin){
            GetBoardParam getBoardParam = new GetBoardParam();
            getBoardParam.setSeq(goodsBoard.getSeq());
            getBoardParam.setUid(pref.getUid());

            VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> setLikeHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(activity);
            setLikeHelper.request(UrlDefine.API_SET_GOODS_LIKE, getBoardParam, GetGoodsBoardResult.class, setLikeHelperListener, true, true, true);
        }

    }
    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam,GetGoodsBoardResult> setLikeHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            Toast.makeText(activity,activity.getStr(R.string.goods_like_complete),Toast.LENGTH_SHORT).show();
            goodsBoard.setLikeCount(goodsBoard.getLikeCount()+1);
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onMessage(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };
    public void getListUrl(){
        GetBoardParam getBoardParam = new GetBoardParam();
        getBoardParam.setMinSeq(minSeq);
        getBoardParam.setStartRow(startRow);
        getBoardParam.setFetchSize(20);
        getBoardParam.setUid(pref.getUid());
        getBoardParam.setType(ValueDefine.GOOD_TYPE_WRITE);
//        if(minSeq==0){
//            WeatherInfo wc = new WeatherInfo();
//            int wordPosition = wc.getWeatherCode(pref.getWeatherCode());
//            getBoardParam.setWeatherCode(wordPosition);
//        }

        VolleyJsonHelper<GetBoardParam, GetGoodsBoardResult> getGoodsBoardHelper = new VolleyJsonHelper<GetBoardParam, GetGoodsBoardResult>(activity);
        getGoodsBoardHelper.request(UrlDefine.API_GET_MY_GOODS_BOARD_LIST, getBoardParam, GetGoodsBoardResult.class, getGoodsBoardHelperListener,false, true, true);

    }
    private VolleyJsonHelper.VolleyJsonHelperListener<GetBoardParam,GetGoodsBoardResult> getGoodsBoardHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam baseParam, GetGoodsBoardResult getGoodsBoardResult) {
            viewProgressLoading(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            List<GoodsBoard> list = getGoodsBoardResult.getGoodsBoardList();

            if(minSeq==0){
//                String weatherWord = getGoodsBoardResult.getWeatherWord();
//                getHeaderView(weatherWord);
            }
            if(list!=null){
                listView.setVisibility(View.VISIBLE);
                view.findViewById(R.id.txt_none_list).setVisibility(View.GONE);
                // findViewById(R.id.txt_none_list).setVisibility(View.GONE);
                if(list.size()>0){
                    if(minSeq==0){
                        goodsBoardList.clear();
                    }
                    for(GoodsBoard reservationCleaning : list){
                        goodsBoardList.add(reservationCleaning);
                    }
                    minSeq = list.get(list.size()-1).getSeq();
                    startRow = startRow + 20;
                    adapter.notifyDataSetChanged();
                }
            }else{
                if(minSeq==0){
                    setInitList();
                    listView.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.txt_none_list).setVisibility(View.VISIBLE);
                }

            }
        }

        @Override
        public void onMessage(GetBoardParam baseParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetBoardParam baseParam, VolleyError error) {
        }
    };

}
