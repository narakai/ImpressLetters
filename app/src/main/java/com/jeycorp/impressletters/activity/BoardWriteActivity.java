package com.jeycorp.impressletters.activity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jeycorp.impressletters.param.GetBoardParam;
import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;

public class BoardWriteActivity extends BaseActivity {
    private EditText mEditTitle, mEditContents;
    private String mCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_board_write);
            setInitView();
            new Analytics(getApplication()).getOutputEvent("1:1 문의 화면");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputMethodManager immhide = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        immhide.hideSoftInputFromWindow(mEditTitle.getWindowToken(), 0);
    }

    private void setInitView() {
        mEditTitle = (EditText) findViewById(R.id.editTitle);
        mEditContents = (EditText) findViewById(R.id.editContents);
        getBundles();

        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setWriteCheck();
                new Analytics(getApplication()).getClickEvent("1:1문의 화면", "글등록 클릭 이벤트", "0");
            }
        });
    }

    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mCategory = bundle.getString("category");
        }

    }

    private void setWriteCheck() {
        PreferenceManagers pref = new PreferenceManagers(activity);

        String title = mEditTitle.getText().toString();
        String contents = mEditContents.getText().toString();
        String id = pref.getUid();
        String nickname = pref.getNickname();
        if (title.equals("")) {
            Toast.makeText(activity, getStr(R.string.str_none_title), Toast.LENGTH_SHORT).show();
            return;
        }
        if (contents.equals("")) {
            Toast.makeText(activity, getStr(R.string.str_none_contents), Toast.LENGTH_SHORT).show();
            return;
        }

        setBoardUrl(title, contents, id, nickname);

    }

    private void setBoardUrl(String title, String contents, String id, String nickname) {
        GetBoardParam boardParam = new GetBoardParam();
        boardParam.setCategory(ValueDefine.BOARD_1ON1);
        boardParam.setTitle(title);
        boardParam.setContents(contents);
        boardParam.setUid(id);

        VolleyJsonHelper<GetBoardParam, BaseResult> setBoardListHelper = new VolleyJsonHelper<GetBoardParam, BaseResult>(this);
        setBoardListHelper.request(UrlDefine.API_SET_BOARD, boardParam, BaseResult.class, setBoardHelperListener, true, true, true);

    }

    private VolleyJsonHelper.VolleyJsonHelperListener<GetBoardParam, BaseResult> setBoardHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetBoardParam, BaseResult>() {
        @Override
        public void onSuccess(GetBoardParam boardParam, BaseResult baseResult) {
            String str = "";
            if (mCategory.equals(ValueDefine.BOARD_1ON1)) {
                str = getStr(R.string.str_write_complete);
            }
            Toast.makeText(activity, str, Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onMessage(GetBoardParam boardParam, BaseResult baseResult) {
        }

        @Override
        public void onError(GetBoardParam boardParam, VolleyError error) {
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
