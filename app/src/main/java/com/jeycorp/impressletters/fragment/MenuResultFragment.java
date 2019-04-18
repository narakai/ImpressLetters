package com.jeycorp.impressletters.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.etsy.android.grids.StaggeredGridView;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.activity.KaKaoActivity;
import com.jeycorp.impressletters.activity.MainActivity;
import com.jeycorp.impressletters.adapter.GoodsBoardAdapter;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.param.GetBoardParam;
import com.jeycorp.impressletters.result.GetGoodsBoardResult;
import com.jeycorp.impressletters.type.GoodsBoard;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.JAlertConfirm;
import com.jeycorp.impressletters.utill.JUtil;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.utill.TimeUtil;
import com.jeycorp.impressletters.view.WeatherHeaderView;
import com.jeycorp.impressletters.volley.VolleyJsonActivityHelper;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;
import com.jeycorp.impressletters.volleyimage.ImageSyncSize;
import com.jeycorp.impressletters.weather.WeatherInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class MenuResultFragment extends android.support.v4.app.Fragment {
    private PreferenceManagers pref;
    private Context context;
    private View view,header;
    private StaggeredGridView listView;
    private MainActivity activity;
    private List<GoodsBoard> goodsBoardList;
    private GoodsBoardAdapter adapter;
    private GoodsBoard goodsBoard;
    private String minCreateDate;
    private final static String INIT_DATE = "0000-00-00 00:00:00";
    private int startRow;
    private boolean isInit=false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int imgWidth=0,imgHeight=0;

    private String strLocationName="";
    private int dustGrade=1;
    private int dustValue=50;

    public MenuResultFragment() {
    }

    public MenuResultFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_result,null);

        Bundle bundle = getArguments();
        Log.e("여긴타니1",""+bundle);
        if(bundle!=null){

            String seq = bundle.getString("seq");
            Log.e("여긴타니2",seq);
        }


        setInitView();
        getListUrl();
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(pref.isWeather()){
            header.findViewById(R.id.linearBg).setVisibility(View.GONE);
        }else{
            header.findViewById(R.id.linearBg).setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  MainActivity){
            activity = (MainActivity)context;
            new Analytics(activity.getApplication()).getOutputEvent("부자되는 글   리스트 화면");
        }
    }
    public void setInitView(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        listView = (StaggeredGridView)view.findViewById(R.id.listView);
        pref = new PreferenceManagers(activity);

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        header = inflater.inflate(R.layout.header_weather,null,false);

        int gridPadding = JUtil.getDpiToPixel(activity,3);
        listView.setGridPadding(gridPadding,0,gridPadding,gridPadding);

        listView.addHeaderView(header);

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

    private void setInitList(){
        minCreateDate = INIT_DATE;
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
                    Intent intent = new Intent(activity, KaKaoActivity.class);
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
        Log.e("litUrl","ㅇㅇ");
        GetBoardParam getBoardParam = new GetBoardParam();
        getBoardParam.setMinCreateDate(minCreateDate);
        getBoardParam.setStartRow(startRow);
        getBoardParam.setFetchSize(20);
        getBoardParam.setType(ValueDefine.GOOD_TYPE_WRITE);
        Bundle bundle = getArguments();
        Log.e("번들",""+bundle);

        if(bundle!=null){
            Log.e("번들타냐1","ㅇㅇ");
            String seq = bundle.getString("seq");
            getBoardParam.setCategorySeq(seq);
            Log.e("번들타냐2",""+getBoardParam.getCategorySeq());
        }

        if(minCreateDate.equals(INIT_DATE)){
            WeatherInfo wc = new WeatherInfo();
            int wordPosition = wc.getWeatherCode(pref.getWeatherCode());
            getBoardParam.setWeatherCode(wordPosition);

            TimeUtil timeUtil = new TimeUtil(activity);
            if(timeUtil.getDayAfeterNight().equals("NIGHT")){
                if(wordPosition==0||wordPosition==1){
                    getBoardParam.setWeatherCode(4);
                }
            }

        }

        VolleyJsonHelper<GetBoardParam, GetGoodsBoardResult> getGoodsBoardHelper = new VolleyJsonHelper<GetBoardParam, GetGoodsBoardResult>(activity);
        getGoodsBoardHelper.request(UrlDefine.API_GET_GOODS_BOARD_LIST, getBoardParam, GetGoodsBoardResult.class, getGoodsBoardHelperListener,isInit, true, true);
        isInit = false;
    }
    public VolleyJsonHelper.VolleyJsonHelperListener<GetBoardParam,GetGoodsBoardResult> getGoodsBoardHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam baseParam, GetGoodsBoardResult getGoodsBoardResult) {
            viewProgressLoading(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            List<GoodsBoard> list = getGoodsBoardResult.getGoodsBoardList();

            if(minCreateDate.equals(INIT_DATE)){
                String weatherWord = getGoodsBoardResult.getWeatherWord();



                /*
                GeoPoint in_pt = new GeoPoint(pref.getLongitude(),pref.getLatitude());
                GeoPoint tm_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, in_pt);
                double tm_x = tm_pt.getX();
                double tm_y = tm_pt.getY();
                String url = "http://openapi.airkorea.or.kr/openapi/services/rest/MsrstnInfoInqireSvc/getNearbyMsrstnList?tmX="+tm_x+"&tmY="+tm_y+"&pageNo=1&numOfRows=10&ServiceKey=Fp8ksAers5TQmkPhEg8Xfu3J%2Fe6w0qnX6OtN5mKJ70hKbrq25UgexELp6NRm8w6A2%2BwEapkvgSrV8NA2a495gw%3D%3D";
                new LocationTask(url, "location").execute();
                */
            }
            if(list!=null){
                listView.setVisibility(View.VISIBLE);
                view.findViewById(R.id.txt_none_list).setVisibility(View.GONE);
                // findViewById(R.id.txt_none_list).setVisibility(View.GONE);
                if(list.size()>0){
                    if(minCreateDate.equals(INIT_DATE)){
                        goodsBoardList.clear();
                    }
                    for(GoodsBoard reservationCleaning : list){
                        goodsBoardList.add(reservationCleaning);
                    }
                    minCreateDate = list.get(list.size()-1).getCreateDate();
                    startRow = startRow + 20;
                    adapter.notifyDataSetChanged();
                }
            }else{
                if(minCreateDate.equals(INIT_DATE)){
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
    private void responseDust(){

        String str="좋음";
        TextView txtCurrentDust = (TextView) header.findViewById(R.id.txt_current_dust);

        txtCurrentDust.setVisibility(View.VISIBLE);


        switch (dustGrade){
            case 1:
                str = "좋음";
                txtCurrentDust.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 2:
                str = "보통";
                txtCurrentDust.setTextColor(Color.parseColor("#ffffff"));
                break;
            case 3:
                str = "나쁨";
                txtCurrentDust.setTextColor(Color.parseColor("#d84315"));
                break;
            case 4:
                str = "매우나쁨";
                txtCurrentDust.setTextColor(Color.parseColor("#d84315"));
                break;
            default:
                break;
        }

        txtCurrentDust.setText(str+" ("+String.valueOf(dustValue)+"ug)");


    }
    private void responseLocationName(){
        String name="";
        try{
            name = URLEncoder.encode(strLocationName,"utf-8");
        }catch (Exception e){

        }

        String url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName="+name+"&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=Fp8ksAers5TQmkPhEg8Xfu3J%2Fe6w0qnX6OtN5mKJ70hKbrq25UgexELp6NRm8w6A2%2BwEapkvgSrV8NA2a495gw%3D%3D";
        new DustTask(url, "location").execute();
    }
    class LocationTask extends AsyncTask<Void, Void, Void> {
        private String  url;
        private int finishCode;
        private String xml;
        private String woeid;

        public LocationTask(String url, String xml) {
            this.url = url;
            this.xml = xml;
            finishCode = 0;

        }
        @Override
        protected Void doInBackground(Void... params) {
            int count = 0;
            try {
                URL httpUrl = new URL(this.url);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(httpUrl.openStream(), "UTF-8");

                int eventType = parser.getEventType();


                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if(parser.getName() != null) {
                        if(eventType == XmlPullParser.START_TAG) {

                            if(parser.getName().equals("stationName")) {

                                strLocationName = parser.nextText();


                                break;

                            }

                        }
                    }
                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(strLocationName.equals("")==false){
                responseLocationName();
            }

        }

    }
    class DustTask extends AsyncTask<Void, Void, Void> {
        private String  url;
        private int finishCode;
        private String xml;
        private String woeid;

        public DustTask(String url, String xml) {
            this.url = url;
            this.xml = xml;
            finishCode = 0;
        }
        @Override
        protected Void doInBackground(Void... params) {
            int count = 0;
            try {
                URL httpUrl = new URL(this.url);
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(httpUrl.openStream(), "UTF-8");

                int eventType = parser.getEventType();


                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if(parser.getName() != null) {
                        if(eventType == XmlPullParser.START_TAG) {
                            if(parser.getName().equals("pm10Value")){
                                dustValue = Integer.parseInt(parser.nextText());
                            }
                            if(parser.getName().equals("pm10Grade")) {

                                dustGrade = Integer.parseInt(parser.nextText());


                                break;

                            }

                        }
                    }
                    eventType = parser.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(dustGrade!=0){
                responseDust();
            }

        }

    }
    public int getDustGrade(){
        return dustGrade;
    }
    public int getDustValue(){
        return dustValue;
    }

}
