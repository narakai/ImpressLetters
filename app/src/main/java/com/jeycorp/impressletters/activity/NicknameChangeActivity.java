package com.jeycorp.impressletters.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.param.GetUserParam;
import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.result.GetUserResult;
import com.jeycorp.impressletters.type.User;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.Keyboard;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;

public class NicknameChangeActivity extends BaseActivity {
    private EditText editNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_nickname_change);
            setInitView();
            new Analytics(getApplication()).getOutputEvent("닉네임 변경 화면");
        }
    }

    private void setInitView() {
        editNickname = (EditText) findViewById(R.id.editNickname);
        findViewById(R.id.linearParent).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Keyboard keyboard = new Keyboard(activity);
                keyboard.hide(editNickname);
                return false;
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            editNickname.setText(bundle.getString("nickname"));
        }
        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNicknameChangeUrl();
                new Analytics(getApplication()).getClickEvent("닉네임 변경 화면", "닉네임변경 클릭 이벤트", "0");
            }
        });
    }

    private void setNicknameChangeUrl() {
        if (editNickname.getText().toString().equals("")) {
            Toast.makeText(activity, getStr(R.string.nickname_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        PreferenceManagers preferenceManagers = new PreferenceManagers(activity);
        GetUserParam getUserParam = new GetUserParam();

        User user = new User();
        user.setUid(preferenceManagers.getUid());
        user.setNickname(editNickname.getText().toString());
        getUserParam.setUser(user);

        VolleyJsonHelper<GetUserParam, BaseResult> setUserBreakHelper = new VolleyJsonHelper<GetUserParam, BaseResult>(this);
        setUserBreakHelper.request(UrlDefine.API_SET_USER_NICKNAME, getUserParam, GetUserResult.class, setUserNicknameHelperListener, true, true, true);

    }

    private VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, BaseResult> setUserNicknameHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, BaseResult>() {
        @Override
        public void onSuccess(GetUserParam baseParam, BaseResult baseResult) {
            Toast.makeText(activity, baseResult.getResultMessage(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(activity, SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
        }

        @Override
        public void onMessage(GetUserParam baseParam, BaseResult baseResult) {
        }

        @Override
        public void onError(GetUserParam baseParam, VolleyError error) {
        }
    };

    public void menuClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnDelete:
                editNickname.setText("");
                break;
        }
    }

}
