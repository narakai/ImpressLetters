package com.jeycorp.impressletters.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.param.GetUserParam;
import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.result.GetUserResult;
import com.jeycorp.impressletters.type.Device;
import com.jeycorp.impressletters.type.User;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.FileDownLoad;
import com.jeycorp.impressletters.utill.JUtil;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.utill.SunUtill;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;
import com.jeycorp.impressletters.volleyimage.MultiPartRequest;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;


public class KaKaoActivity extends BaseActivity {
    public static final String JOIN = "JOIN";
    public static final String LONGIN = "LOGIN";
    private Context context = KaKaoActivity.this;
    private LoginButton loginButton;
    private SessionCallback callback;
    private Button logoutButton;
    private String uid;
    private String nickname;
    private String imgUrl;
    private User user;
    private String resultMessage = "";
    private String pageType;
    private String imagePath;
    private String ext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_ka_kao);
            callback = new SessionCallback();
            Session.getCurrentSession().addCallback(callback);
            Session.getCurrentSession().checkAndImplicitOpen();
            loginButton = (LoginButton) findViewById(R.id.loginButton1);
            logoutButton = (Button) findViewById(R.id.unlinkButton);
            setInitView();
            logoutButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (Session.getCurrentSession().isOpened()) {

                        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                // redirectLoginActivity();
                            }
                        });
                    }
                }

            });

            new Analytics(getApplication()).getOutputEvent("카카오톡 로그인 화면");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context, SettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Session.getCurrentSession().isOpened()) {
//            OnSessionOpened();
//        }
    }

    private void setInitView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pageType = bundle.getString("type");
        }
    }

    public void setProfile(String path, String ext, GetUserParam getUserParam) {
        imagePath = path;
        this.ext = ext;

        setUserUrl(getUserParam);
    }

    private void getUrlKaKao(MeV2Response userProfile) {

        nickname = userProfile.getNickname();
        imgUrl = userProfile.getProfileImagePath();

        Device device = JUtil.getDevice(context);
        User user = new User();
        uid = "kakao_" + userProfile.getId();

        user.setUid(uid);
        user.setSns("kakao");
        user.setNickname(nickname);

        GetUserParam getUserParam = new GetUserParam();
        getUserParam.setUser(user);
        getUserParam.setDevice(device);

        if (imgUrl != null && !imgUrl.equals("")) {
            SunUtill mSunUtill = new SunUtill(activity);
            FileDownLoad downLoad = new FileDownLoad(this);
            downLoad.setProfileImgUrl(imgUrl, mSunUtill.getPath() + "/tempImage.jpg", getUserParam);
        } else {
            setUserUrl(getUserParam);
        }

    }

    private void setUserUrl(GetUserParam getUserParam) {

        VolleyJsonHelper<GetUserParam, GetUserResult> getIntroHelper = new VolleyJsonHelper<GetUserParam, GetUserResult>(this);
        getIntroHelper.request(UrlDefine.API_GET_USER_KAKAO, getUserParam, GetUserResult.class, getKakaoHelperListener, false, true, true);
    }

    public void OnSessionOpened() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                //redirectLoginActivity();
            }

            @Override
            public void onSuccess(MeV2Response response) {
                if (response != null) {
                    getUrlKaKao(response);
                }
            }
        });
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            // redirectSignupActivity();
            OnSessionOpened();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }


    private class MyLogoutCallback implements Drawable.Callback {

        @Override
        public void invalidateDrawable(Drawable who) {
            // TODO Auto-generated method stub

        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            // TODO Auto-generated method stub

        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            // TODO Auto-generated method stub

        }

    }


    private VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, GetUserResult> getKakaoHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, GetUserResult>() {
        @Override
        public void onSuccess(GetUserParam getUserParam, GetUserResult getUserResult) {
            user = getUserResult.getUser();
            resultMessage = getUserResult.getResultMessage();

            if (user == null) {
                Toast.makeText(activity, getStr(R.string.user_state_b), Toast.LENGTH_SHORT).show();
            } else {
                if (getUserResult.getQue().equals("JOIN")) {
                    if (imgUrl != null && !imgUrl.equals("")) {
                        setProfileImgUrl();
                    } else {
                        goActivity();
                    }
                } else {
                    goActivity();
                }
            }
        }

        @Override
        public void onMessage(GetUserParam getUserParam, GetUserResult getUserResult) {
        }

        @Override
        public void onError(GetUserParam getUserParam, VolleyError error) {
        }
    };

    private void goActivity() {
        PreferenceManagers pref = new PreferenceManagers(activity);

        Toast.makeText(activity, resultMessage, Toast.LENGTH_SHORT).show();
        pref.setUid(user.getUid());


        Intent intent = null;
        if (pageType.equals(ValueDefine.KAKAO_SETTING)) {
            intent = new Intent(activity, SettingActivity.class);
        } else if (pageType.equals(ValueDefine.KAKAO_DETAIL)) {
            intent = new Intent(activity, GoodsDetailActivity.class);
            intent.putExtra("seq", getIntent().getExtras().getLong("seq"));
        } else if (pageType.equals(ValueDefine.KAKAO_MAIN)) {
            intent = new Intent(activity, MainActivity.class);
        } else if (pageType.equals(ValueDefine.KAKAO_SEARCH)) {
            intent = new Intent(activity, GoodsSearchActivity.class);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setProfileImgUrl() {
        MultiPartRequest multiPartReq = new MultiPartRequest(activity, UrlDefine.API_SET_USER_PROFILE, BaseResult.class) {
            protected void onResult(BaseResult result) {
                super.onResult(result);
                goActivity();
            }
        };

        if (imagePath != null && imagePath.length() > 1) {

            SunUtill mSunUtill = new SunUtill(activity);

            if (ext.equals("png")) {
                imagePath = mSunUtill.getPath() + "/tempImage.png";
            } else {
                imagePath = mSunUtill.getPath() + "/tempImage.jpg";
            }

            multiPartReq.addFile("file_profile", imagePath);
            Log.e("아머지", "아머지:" + imagePath);
            Log.e("아머지", "아머지:" + uid);
        }
        multiPartReq.addParam("uid", uid);
        multiPartReq.submit();
    }

}
