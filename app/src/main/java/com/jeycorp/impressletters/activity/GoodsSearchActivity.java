package com.jeycorp.impressletters.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.etsy.android.grids.StaggeredGridView;
import com.jeycorp.impressletters.adapter.GoodsBoardAdapter;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.param.GetBoardParam;
import com.jeycorp.impressletters.result.GetGoodsBoardResult;
import com.jeycorp.impressletters.type.GoodsBoard;
import com.jeycorp.impressletters.type.GoodsCategory;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.utill.SoftKeyboardDectectorView;
import com.jeycorp.impressletters.view.FixGridLayout;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.param.BaseParam;
import com.jeycorp.impressletters.param.GetGoodsCategoryParam;
import com.jeycorp.impressletters.result.GetGoodsCategoryResult;
import com.jeycorp.impressletters.utill.JAlertConfirm;
import com.jeycorp.impressletters.volley.VolleyJsonActivityHelper;
import com.jeycorp.impressletters.volleyimage.ImageSyncSize;

import java.util.ArrayList;
import java.util.List;

public class GoodsSearchActivity extends BaseActivity {
    private long categorySeq = 0;
    private SoftKeyboardDectectorView softKeyboardDecector;
    private EditText editSearch;
    private List<GoodsBoard> goodsBoardList;
    private List<GoodsCategory> categoryList;
    private StaggeredGridView listView;
    private GoodsBoardAdapter adapter;
    private GoodsBoard goodsBoard;
    private String minCreateDate;
    private final static String INIT_DATE = "0000-00-00 00:00:00";
    private int startRow;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isInit = true;
    private PreferenceManagers pref;
    private int imgWidth = 0, imgHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_goods_search);
            setInitView();
            setKeyboardEvent();
            getGoodsCategoryUrl();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showKeyboard();
                }
            }, 100);

            keyboardEnterSearch();

            new Analytics(getApplication()).getOutputEvent("부자되는 글   검색 화면");
        }
    }

    private void setInitView() {
        pref = new PreferenceManagers(activity);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (StaggeredGridView) findViewById(R.id.listView);
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        goodsBoardList = new ArrayList<GoodsBoard>();
        adapter = new GoodsBoardAdapter(activity, goodsBoardList);
        adapter.setInit(activity);
        listView.setAdapter(adapter);

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInitList();
                getListUrl();
                hideKeyboard();
            }
        });

        setInitList();
    }

    private void setInitList() {
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

    public void getListUrl() {
        GetGoodsCategoryParam getGoodsCategoryParam = new GetGoodsCategoryParam();
        getGoodsCategoryParam.setGoodsCategorySeq(categorySeq);
        getGoodsCategoryParam.setTitle(editSearch.getText().toString());
        getGoodsCategoryParam.setMinCreateDate(minCreateDate);
        getGoodsCategoryParam.setStartRow(startRow);
        getGoodsCategoryParam.setFetchSize(20);

        VolleyJsonHelper<GetGoodsCategoryParam, GetGoodsBoardResult> getGoodsListListHelper = new VolleyJsonHelper<GetGoodsCategoryParam, GetGoodsBoardResult>(this);
        getGoodsListListHelper.request(UrlDefine.API_GET_GOODS_SEARCH, getGoodsCategoryParam, GetGoodsBoardResult.class, getGoodsListListHelperListener, isInit, true, true);
        isInit = false;

    }

    private VolleyJsonHelper.VolleyJsonHelperListener<GetGoodsCategoryParam, GetGoodsBoardResult> getGoodsListListHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetGoodsCategoryParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetGoodsCategoryParam baseParam, GetGoodsBoardResult getGoodsBoardResult) {
            viewProgressLoading(View.GONE);
            mSwipeRefreshLayout.setRefreshing(false);
            List<GoodsBoard> list = getGoodsBoardResult.getGoodsBoardList();

            if (list != null) {
                adapter.setSearch(editSearch.getText().toString());
                listView.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_none_list).setVisibility(View.GONE);

                if (list.size() > 0) {
                    if (minCreateDate.equals(INIT_DATE)) {
                        goodsBoardList.clear();
                    }
                    for (GoodsBoard goodsBoard : list) {
                        goodsBoardList.add(goodsBoard);
                    }
                    minCreateDate = list.get(list.size() - 1).getCreateDate();
                    startRow = startRow + 20;
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (minCreateDate.equals(INIT_DATE)) {
                    setInitList();
                    listView.setVisibility(View.VISIBLE);
                    findViewById(R.id.txt_none_list).setVisibility(View.VISIBLE);
                }

            }

        }

        @Override
        public void onMessage(GetGoodsCategoryParam baseParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetGoodsCategoryParam baseParam, VolleyError error) {
        }
    };

    public void viewProgressLoading(int v) {
        findViewById(R.id.progressLoading).setVisibility(v);
    }

    private void getGoodsCategoryUrl() {
        BaseParam baseParam = new BaseParam();

        VolleyJsonHelper<BaseParam, GetGoodsCategoryResult> getGoodsCategoryListHelper = new VolleyJsonHelper<BaseParam, GetGoodsCategoryResult>(this);
        getGoodsCategoryListHelper.request(UrlDefine.API_GET_GOODS_CATEGORY, baseParam, GetGoodsCategoryResult.class, getCategoryListHelperListener, false, true, true);
    }

    private VolleyJsonHelper.VolleyJsonHelperListener<BaseParam, GetGoodsCategoryResult> getCategoryListHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<BaseParam, GetGoodsCategoryResult>() {
        @Override
        public void onSuccess(BaseParam baseParam, GetGoodsCategoryResult getNoticeResult) {
            categoryList = getNoticeResult.getGoodsCategoryList();

//            GoodsCategory category = new GoodsCategory();
//            category.setSeq(0);
//            category.setSelected(false);
//            category.setName("전체");
//            categoryList.add(0,category);

            getGoodsCategoryView(categoryList);
            // getListUrl();
        }

        @Override
        public void onMessage(BaseParam baseParam, GetGoodsCategoryResult getNoticeResult) {
        }

        @Override
        public void onError(BaseParam baseParam, VolleyError error) {
        }
    };


    private void getGoodsCategoryView(final List<GoodsCategory> list) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;

        FixGridLayout linearParent = (FixGridLayout) findViewById(R.id.categoryView);
        linearParent.removeAllViews();
        // LinearLayout linearHorizen = null;
        int maxWidth = 0;
        for (int i = 0; i < list.size(); i++) {
            final GoodsCategory category = list.get(i);
//            if(i==0){
//                linearHorizen = new LinearLayout(activity);
//                linearHorizen.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
//                linearHorizen.setOrientation(LinearLayout.HORIZONTAL);
//                linearHorizen.setGravity(Gravity.CENTER);
//                linearHorizen.setPadding(0, 10, 0, 10);
//                linearParent.addView(linearHorizen);
//            }else{
//                if(deviceWidth < maxWidth+40){
//                    maxWidth = 0;
//                    linearHorizen = new LinearLayout(activity);
//                    linearHorizen.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
//                    linearHorizen.setOrientation(LinearLayout.HORIZONTAL);
//                    linearHorizen.setGravity(Gravity.CENTER);
//                    linearHorizen.setPadding(0, 10, 0, 10);
//                    linearParent.addView(linearHorizen);
//                }
//            }
            LinearLayout linearWork = new LinearLayout(activity);
            FixGridLayout.LayoutParams fixParams = new FixGridLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearWork.setPadding(0, 0, 0, 0);
            linearWork.setLayoutParams(fixParams);

            final TextView txtWork = new TextView(activity);
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // params.leftMargin= 50;
            linearParams.leftMargin = 40;
            linearParams.topMargin = 40;
            txtWork.setLayoutParams(linearParams);
            txtWork.setPadding(0, 30, 0, 30);
            txtWork.setTextColor(Color.BLACK);
            txtWork.setGravity(Gravity.CENTER);
            txtWork.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimension(R.dimen.font_default_16dp));
            txtWork.setBackgroundResource(R.drawable.round_gray_full_little);
            txtWork.setText("   " + category.getName() + "   ");


            linearWork.addView(txtWork);
            linearParent.addView(linearWork);

            if (list.get(i).isSelected()) {
                txtWork.setBackgroundResource(R.drawable.round_orange_full_little);
                txtWork.setTextColor(Color.WHITE);
            } else {
                txtWork.setBackgroundResource(R.drawable.round_gray_full_little);
                txtWork.setTextColor(Color.BLACK);
            }
            txtWork.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int j = 0; j < list.size(); j++) {
                        list.get(j).setSelected(false);
                    }
                    ViewGroup viewGroup = (ViewGroup) v.getParent().getParent();
                    for (int a = 0; a < viewGroup.getChildCount(); a++) {
                        ViewGroup v1 = (ViewGroup) viewGroup.getChildAt(a);
                        for (int b = 0; b < v1.getChildCount(); b++) {
                            TextView v2 = (TextView) v1.getChildAt(b);
                            v2.setTextColor(Color.BLACK);
                            v2.setBackgroundResource(R.drawable.round_gray_full_little);
                        }
                    }
                    txtWork.setTextColor(Color.WHITE);
                    txtWork.setBackgroundResource(R.drawable.round_orange_full_little);
                    category.setSelected(true);
                    categorySeq = category.getSeq();

                    hideKeyboard();
                    setInitList();
                    getListUrl();


                }
            });
        }

    }

    private boolean getUserLogin() {
        boolean isLogin = true;
        if (pref.getUid().equals("")) {
            new JAlertConfirm(activity, getStr(R.string.setting_login_q), false) {
                @Override
                protected void onYes() {
                    super.onYes();
                    Intent intent = new Intent(activity, KaKaoActivity.class);
                    intent.putExtra("type", ValueDefine.KAKAO_SEARCH);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            };
            isLogin = false;
        }
        return isLogin;
    }

    public void setLikeUrl(GoodsBoard goodsBoard) {
        this.goodsBoard = goodsBoard;
        boolean isLogin = getUserLogin();
        if (isLogin) {
            GetBoardParam getBoardParam = new GetBoardParam();
            getBoardParam.setSeq(goodsBoard.getSeq());
            getBoardParam.setUid(pref.getUid());

            VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> setLikeHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(activity);
            setLikeHelper.request(UrlDefine.API_SET_GOODS_LIKE, getBoardParam, GetGoodsBoardResult.class, setLikeHelperListener, true, true, true);
        }

    }

    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult> setLikeHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            Toast.makeText(activity, getStr(R.string.goods_like_complete), Toast.LENGTH_SHORT).show();
            goodsBoard.setLikeCount(goodsBoard.getLikeCount() + 1);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onMessage(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };

    public void setShareUrl(GoodsBoard goodsBoard, int imgWidth, int imgHeight) {
        this.imgWidth = imgWidth;
        this.imgHeight = imgWidth;

        this.goodsBoard = goodsBoard;
        GetBoardParam getBoardParam = new GetBoardParam();
        getBoardParam.setSeq(goodsBoard.getSeq());

        VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> setShareHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(activity);
        setShareHelper.request(UrlDefine.API_SET_SHARE, getBoardParam, GetGoodsBoardResult.class, setShareHelperListener, true, true, true);

    }

    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult> setShareHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            goodsBoard.setShareCount(goodsBoard.getShareCount() + 1);
            adapter.notifyDataSetChanged();

            ImageSyncSize syncSize = new ImageSyncSize(activity, goodsBoard);
            syncSize.excute(UrlDefine.SERVER + goodsBoard.getImgBannerUrl());
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

    private void setKeyboardEvent() {
        softKeyboardDecector = new SoftKeyboardDectectorView(activity);
        activity.addContentView(softKeyboardDecector, new FrameLayout.LayoutParams(-1, -1));
        softKeyboardDecector.setOnShownKeyboard(new SoftKeyboardDectectorView.OnShownKeyboardListener() {
            @Override
            public void onShowSoftKeyboard() {
                activity.findViewById(R.id.categoryView).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.goodsView).setVisibility(View.GONE);
                findViewById(R.id.btnCancel).setVisibility(View.VISIBLE);
            }
        });

        softKeyboardDecector.setOnHiddenKeyboard(new SoftKeyboardDectectorView.OnHiddenKeyboardListener() {
            @Override
            public void onHiddenSoftKeyboard() {
                if (isInit) {
                    finish();
                }
                activity.findViewById(R.id.categoryView).setVisibility(View.GONE);
                activity.findViewById(R.id.goodsView).setVisibility(View.VISIBLE);
                findViewById(R.id.btnCancel).setVisibility(View.INVISIBLE);

            }
        });
    }


    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void hideKeyboard() {
        InputMethodManager immhide = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        activity.findViewById(R.id.categoryView).setVisibility(View.GONE);
        activity.findViewById(R.id.goodsView).setVisibility(View.VISIBLE);
        findViewById(R.id.btnCancel).setVisibility(View.INVISIBLE);
    }

    private void keyboardEnterSearch() {
        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.requestFocus();
                activity.findViewById(R.id.categoryView).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.goodsView).setVisibility(View.GONE);
                findViewById(R.id.btnCancel).setVisibility(View.VISIBLE);
            }
        });
        editSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editSearch.setInputType(InputType.TYPE_CLASS_TEXT);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        editSearch.requestFocus();
                        setInitList();
                        getListUrl();
                        hideKeyboard();
                        break;
                    default:
                        editSearch.requestFocus();
                        setInitList();
                        getListUrl();
                        hideKeyboard();
                        return false;
                }
                return true;
            }
        });
    }

    public void menuClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                hideKeyboard();
                finish();
                break;
            case R.id.btnSearch:
                new Analytics(activity.getApplication()).getClickEvent("부자되는 글   검색화면", "검색 클릭 이벤트", "0");
                setInitList();
                getListUrl();
                hideKeyboard();
                break;
        }
    }

}
