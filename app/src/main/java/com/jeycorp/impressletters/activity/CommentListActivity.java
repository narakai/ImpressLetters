package com.jeycorp.impressletters.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jeycorp.impressletters.param.GetBoardParam;
import com.jeycorp.impressletters.param.GetCommentParam;
import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.result.GetGoodsBoardResult;
import com.jeycorp.impressletters.type.GoodsComment;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.adapter.GoodsCommentAdapter;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.type.GoodsBoard;
import com.jeycorp.impressletters.utill.JAlertConfirm;
import com.jeycorp.impressletters.volley.VolleyJsonActivityHelper;
import com.jeycorp.impressletters.volleyimage.ImageSyncSize;

import java.util.ArrayList;
import java.util.List;

public class CommentListActivity extends BaseActivity {
    private PreferenceManagers pref;
    private List<GoodsComment> goodsCommentList;
    private ListView listView;
    private GoodsCommentAdapter adapter;
    private GoodsBoard goodsBoard;
    private GoodsComment goodsComment;
    private long minSeq;
    private int startRow;
    private boolean isInit = true;
    private long seq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_comment_list);
            setInitView();
            getGoodsUrl();
            new Analytics(getApplication()).getOutputEvent("부자되는 글   댓글 리스트화면");
        }
    }


    private void setInitView() {
        pref = new PreferenceManagers(this);
        listView = (ListView) findViewById(R.id.listView);

        goodsCommentList = new ArrayList<GoodsComment>();
        adapter = new GoodsCommentAdapter(this, goodsCommentList, ValueDefine.ADAPTER_LIST);
        adapter.setActivity(this);
        listView.setAdapter(adapter);

        pref.setAdCount(pref.getAdCount() + 1);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            seq = bundle.getLong("seq", 0);
        }
    }

    private void setInitList() {
        minSeq = 0;
        startRow = 0;
        goodsCommentList.clear();
        adapter.notifyDataSetChanged();
    }

    public void viewProgressLoading(int v) {
        findViewById(R.id.progressLoading).setVisibility(v);
    }


    public void getGoodsUrl() {
        GetBoardParam getBoardParam = new GetBoardParam();
        getBoardParam.setMinSeq(minSeq);
        getBoardParam.setStartRow(startRow);
        getBoardParam.setFetchSize(20);
        getBoardParam.setUid(pref.getUid());
        getBoardParam.setSeq(seq);

        VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> getGoodsDetailHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(this);
        getGoodsDetailHelper.request(UrlDefine.API_GET_GOODS_COMMENTLIST, getBoardParam, GetGoodsBoardResult.class, getGoodsDetailHelperListener, isInit, true, true);
        isInit = false;
    }

    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult> getGoodsDetailHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            viewProgressLoading(View.GONE);
            List<GoodsComment> list = getGoodsBoardResult.getGoodsCommentList();
            if (minSeq == 0) {
                goodsBoard = getGoodsBoardResult.getGoodsBoard();
            }
            if (list != null) {
                listView.setVisibility(View.VISIBLE);
                if (list.size() > 0) {
                    if (minSeq == 0) {
                        goodsCommentList.clear();
                    }
                    for (GoodsComment goodsComment : list) {
                        goodsCommentList.add(goodsComment);
                    }
                    minSeq = list.get(list.size() - 1).getSeq();
                    startRow = startRow + 20;
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (minSeq == 0) {
                    setInitList();
                    listView.setVisibility(View.VISIBLE);
                }

            }
        }

        @Override
        public void onMessage(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };

    public void setShareUrl() {
        GetBoardParam getBoardParam = new GetBoardParam();
        getBoardParam.setSeq(seq);

        VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> setShareHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(this);
        setShareHelper.request(UrlDefine.API_SET_SHARE, getBoardParam, GetGoodsBoardResult.class, setShareHelperListener, true, true, true);
    }

    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult> setShareHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {

            ImageSyncSize syncSize = new ImageSyncSize(CommentListActivity.this, goodsBoard);
            syncSize.excute(UrlDefine.SERVER + goodsBoard.getImgBannerUrl());
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

    public void setImageShare(String url) {
        ImageSyncSize syncSize = new ImageSyncSize(CommentListActivity.this, goodsBoard);
        syncSize.excuteImageUrl(url);

    }

    public void setLikeUrl() {
        boolean isLogin = getUserLogin();
        if (isLogin) {
            GetBoardParam getBoardParam = new GetBoardParam();
            getBoardParam.setSeq(seq);
            getBoardParam.setUid(pref.getUid());

            VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult> setLikeHelper = new VolleyJsonActivityHelper<GetBoardParam, GetGoodsBoardResult>(this);
            setLikeHelper.request(UrlDefine.API_SET_GOODS_LIKE, getBoardParam, GetGoodsBoardResult.class, setLikeHelperListener, true, true, true);
        }
    }

    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult> setLikeHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            Toast.makeText(CommentListActivity.this, getStr(R.string.goods_like_complete), Toast.LENGTH_SHORT).show();
            ImageView imgLike = (ImageView) findViewById(R.id.imgLike);
            imgLike.setBackgroundResource(R.drawable.page_under_like_on);
        }

        @Override
        public void onMessage(GetBoardParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };

    public void setCommentUrl(String comment) {
        GetCommentParam getCommentParam = new GetCommentParam();
        getCommentParam.setContents(comment);
        getCommentParam.setUserUid(pref.getUid());
        getCommentParam.setGoodsBoardSeq(seq);

        VolleyJsonActivityHelper<GetCommentParam, GetGoodsBoardResult> setCommentHelper = new VolleyJsonActivityHelper<GetCommentParam, GetGoodsBoardResult>(this);
        setCommentHelper.request(UrlDefine.API_SET_GOODS_COMMENT, getCommentParam, GetGoodsBoardResult.class, setCommentHelperListener, false, true, true);
    }

    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetCommentParam, GetGoodsBoardResult> setCommentHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetCommentParam, GetGoodsBoardResult>() {
        @Override
        public void onSuccess(GetCommentParam getBoardParam, GetGoodsBoardResult getGoodsBoardResult) {
            goodsCommentList.add(0, getGoodsBoardResult.getGoodsComment());
            adapter.notifyDataSetChanged();


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

    private void setBookMarkUrl() {
        boolean isLogin = getUserLogin();
        if (isLogin) {
            GetBoardParam getBoardParam = new GetBoardParam();
            getBoardParam.setUid(pref.getUid());
            getBoardParam.setSeq(seq);

            VolleyJsonActivityHelper<GetBoardParam, BaseResult> setBookMarkHelper = new VolleyJsonActivityHelper<GetBoardParam, BaseResult>(this);
            setBookMarkHelper.request(UrlDefine.API_SET_BOOKMARK, getBoardParam, BaseResult.class, setBookMarkHelperListener, true, true, true);
        }

    }

    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, BaseResult> setBookMarkHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetBoardParam, BaseResult>() {
        @Override
        public void onSuccess(GetBoardParam getBoardParam, BaseResult baseResult) {
            /*
            ImageButton btnBookMark = (ImageButton)findViewById(R.id.btnBookmark);
            if(goodsBoard.getBookCount()==0){
                goodsBoard.setBookCount(1);
                btnBookMark.setBackgroundResource(R.drawable.page_top_bookmark_on);
            }else{
                goodsBoard.setBookCount(0);
                btnBookMark.setBackgroundResource(R.drawable.page_top_bookmark);
            }
            */
            Toast.makeText(CommentListActivity.this, baseResult.getResultMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onMessage(GetBoardParam getBoardParam, BaseResult baseResult) {
        }

        @Override
        public void onError(GetBoardParam getBoardParam, VolleyError error) {
        }
    };

    public void deleteCommentUrl(final GoodsComment item) {
        goodsComment = item;
        new JAlertConfirm(CommentListActivity.this, getStr(R.string.comment_delete_q), false) {
            @Override
            protected void onYes() {
                super.onYes();
                GetCommentParam getCommentParam = new GetCommentParam();
                getCommentParam.setSeq(item.getSeq());
                getCommentParam.setGoodsBoardSeq(seq);

                VolleyJsonActivityHelper<GetCommentParam, BaseResult> setDeleteCommentkHelper = new VolleyJsonActivityHelper<GetCommentParam, BaseResult>(CommentListActivity.this);
                setDeleteCommentkHelper.request(UrlDefine.API_SET_DELETE_COMMENT, getCommentParam, BaseResult.class, setDeleteCommentHelperListener, true, true, true);
            }
        };
    }

    private VolleyJsonActivityHelper.VolleyJsonHelperListener<GetCommentParam, BaseResult> setDeleteCommentHelperListener = new VolleyJsonActivityHelper.VolleyJsonHelperListener<GetCommentParam, BaseResult>() {
        @Override
        public void onSuccess(GetCommentParam getBoardParam, BaseResult baseResult) {
            goodsCommentList.remove(goodsComment);
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onMessage(GetCommentParam getBoardParam, BaseResult baseResult) {
        }

        @Override
        public void onError(GetCommentParam getBoardParam, VolleyError error) {
        }
    };

    private boolean getUserLogin() {
        boolean isLogin = true;
        if (pref.getUid().equals("")) {
            new JAlertConfirm(CommentListActivity.this, getStr(R.string.setting_login_q), false) {
                @Override
                protected void onYes() {
                    super.onYes();
                    Intent intent = new Intent(CommentListActivity.this, KaKaoActivity.class);
                    intent.putExtra("type", ValueDefine.KAKAO_DETAIL);
                    intent.putExtra("seq", seq);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            };
            isLogin = false;
        }
        return isLogin;
    }

    public void moveActivity(GoodsComment item) {
        Intent intent = new Intent(CommentListActivity.this, CommentModifyActivity.class);
        intent.putExtra("seq", item.getSeq());
        intent.putExtra("contents", item.getContents());
        intent.putExtra("parentSeq", seq);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void menuClick(View v) {
        switch (v.getId()) {
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
