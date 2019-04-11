package com.jeycorp.impressletters.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jeycorp.impressletters.param.GetCommentParam;
import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;

public class CommentModifyActivity extends BaseActivity {
    private long seq;
    private long parentSeq;
    private String contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_comment_modify);
            setInitView();
            getContents();
        }
    }

    private void setInitView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            parentSeq = bundle.getLong("parentSeq");
            seq = bundle.getLong("seq");
            contents = bundle.getString("contents");
        }
        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSubmitUrl();
            }
        });
    }

    private void getContents() {
        EditText editContents = (EditText) findViewById(R.id.editContents);
        editContents.setText(contents);
    }

    private void setSubmitUrl() {
        EditText editContents = (EditText) findViewById(R.id.editContents);
        if (editContents.getText().toString().equals("")) {
            Toast.makeText(activity, getStr(R.string.goods_comment_hint), Toast.LENGTH_SHORT).show();
            return;
        }
        GetCommentParam getCommentParam = new GetCommentParam();
        getCommentParam.setSeq(seq);
        getCommentParam.setContents(editContents.getText().toString());

        VolleyJsonHelper<GetCommentParam, BaseResult> setCommentHelper = new VolleyJsonHelper<GetCommentParam, BaseResult>(this);
        setCommentHelper.request(UrlDefine.API_SET_MODIFY_COMMENT, getCommentParam, BaseResult.class, setCommentHelperListener, true, true, true);

    }

    private VolleyJsonHelper.VolleyJsonHelperListener<GetCommentParam, BaseResult> setCommentHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetCommentParam, BaseResult>() {
        @Override
        public void onSuccess(GetCommentParam getBoardParam, BaseResult baseResult) {
            Toast.makeText(activity, baseResult.getResultMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, GoodsDetailActivity.class);
            intent.putExtra("seq", parentSeq);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        @Override
        public void onMessage(GetCommentParam getBoardParam, BaseResult baseResult) {
        }

        @Override
        public void onError(GetCommentParam getBoardParam, VolleyError error) {
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
