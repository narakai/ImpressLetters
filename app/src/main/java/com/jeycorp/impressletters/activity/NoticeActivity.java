package com.jeycorp.impressletters.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.adapter.NoticeAdapter;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.param.BaseParam;
import com.jeycorp.impressletters.result.GetNoticeResult;
import com.jeycorp.impressletters.type.Notice;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;

import java.util.List;

public class NoticeActivity extends BaseActivity {
    private List<Notice> boards;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_notice);
            setInitView();
            getBoardListUrl();
            new Analytics(getApplication()).getOutputEvent("공지사항 화면");
        }
    }

    private void setInitView() {
        listView = (ListView) findViewById(R.id.listView);
    }

    private void getBoardListUrl() {
        BaseParam baseParam = new BaseParam();

        VolleyJsonHelper<BaseParam, GetNoticeResult> getNoticeListHelper = new VolleyJsonHelper<BaseParam, GetNoticeResult>(this);
        getNoticeListHelper.request(UrlDefine.API_GET_NOTICE_LIST, baseParam, GetNoticeResult.class, getNoticeListHelperListener, true, true, true);
    }

    private VolleyJsonHelper.VolleyJsonHelperListener<BaseParam, GetNoticeResult> getNoticeListHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<BaseParam, GetNoticeResult>() {
        @Override
        public void onSuccess(BaseParam baseParam, GetNoticeResult getNoticeResult) {
            boards = getNoticeResult.getNoticeList();
            if (boards != null) {
                listView.setVisibility(View.VISIBLE);
                findViewById(R.id.txt_none_list).setVisibility(View.GONE);
                NoticeAdapter adapter = new NoticeAdapter(activity, boards);
                listView.setAdapter(adapter);

            } else {
                listView.setVisibility(View.GONE);
                findViewById(R.id.txt_none_list).setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onMessage(BaseParam baseParam, GetNoticeResult getNoticeResult) {
        }

        @Override
        public void onError(BaseParam baseParam, VolleyError error) {
        }
    };

    public void menuClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }

}
