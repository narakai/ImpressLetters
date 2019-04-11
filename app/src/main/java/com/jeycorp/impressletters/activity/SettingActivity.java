package com.jeycorp.impressletters.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jeycorp.impressletters.utill.JUtil;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.param.GetUserParam;
import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.result.GetUserResult;
import com.jeycorp.impressletters.type.Device;
import com.jeycorp.impressletters.type.User;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.GcmPreferenceManager;
import com.jeycorp.impressletters.utill.JAlertConfirm;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.volley.VolleyJsonHelper;
import com.jeycorp.impressletters.volleyimage.ImageSync;

public class SettingActivity extends BaseActivity {
    public static final String USE_URL = "use.html";
    public static final String INFO_URL = "info.html";
    private PreferenceManagers preferenceManagers;
    private GcmPreferenceManager gcmPreferenceManager;
    private ImageView imgProfile;
    private String imagePath = "";
    private String profileImage = "";
    private Bitmap rotatedSrc;
    private String ext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isPermission) {
            requestPermissions();
        } else {
            setContentView(R.layout.activity_setting);
            setInitView();
            getContents(true);
            new Analytics(getApplication()).getOutputEvent("설정 화면");
        }

    }

    private void setInitView() {
        preferenceManagers = new PreferenceManagers(activity);
        gcmPreferenceManager = new GcmPreferenceManager(activity);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
    }

    private void getProfileImg(User user) {
        profileImage = UrlDefine.SERVER + user.getImgUrl();
        ImageSync imageSync = new ImageSync(this, imgProfile, true);
        imageSync.excute(profileImage);
        imgProfile.setVisibility(View.VISIBLE);
        ImageView imgSqr = (ImageView) findViewById(R.id.imgSqr);
        if (user.getImgThumbUrl() != null && !user.getImgThumbUrl().equals("")) {
            imgSqr.setVisibility(View.VISIBLE);
        } else {
            imgSqr.setVisibility(View.VISIBLE);
        }
    }

    private void getContents(boolean is) {
        ImageButton btnWeather = (ImageButton) findViewById(R.id.btnWeather);
        ImageButton btnPush = (ImageButton) findViewById(R.id.btnPush);
        btnWeather.setBackgroundResource(R.drawable.menu_04_option_weatherswitch_off);
        btnPush.setBackgroundResource(R.drawable.menu_04_option_weatherswitch_off);
        if (preferenceManagers.isWeather()) {
            btnWeather.setBackgroundResource(R.drawable.menu_04_option_weatherswitch_on);
        }
        if (gcmPreferenceManager.isPush()) {
            btnPush.setBackgroundResource(R.drawable.menu_04_option_weatherswitch_on);
        }

        if (!preferenceManagers.getUid().equals("")) {
            getUserUrl(is);
        }

        if (preferenceManagers.getUid().equals("")) {
            findViewById(R.id.linearLine).setVisibility(View.GONE);
            findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
            findViewById(R.id.btnLogOut).setVisibility(View.GONE);
            findViewById(R.id.btnBreak).setVisibility(View.GONE);
        } else {
            findViewById(R.id.linearLine).setVisibility(View.VISIBLE);
            findViewById(R.id.btnLogin).setVisibility(View.GONE);
            findViewById(R.id.btnLogOut).setVisibility(View.VISIBLE);
            findViewById(R.id.btnBreak).setVisibility(View.VISIBLE);
        }
    }

    private void getContentsServer(User user) {
        TextView txtNickname = (TextView) findViewById(R.id.txtNickname);
        txtNickname.setText(preferenceManagers.getNickname());
        if (preferenceManagers.getNickname().equals("")) {
            txtNickname.setText("닉네임입력");
        }

    }

    private void getUserUrl(boolean is) {
        GetUserParam getUserParam = new GetUserParam();
        getUserParam.setUid(preferenceManagers.getUid());

        VolleyJsonHelper<GetUserParam, GetUserResult> getUserHelper = new VolleyJsonHelper<GetUserParam, GetUserResult>(this);
        getUserHelper.request(UrlDefine.API_GET_USER, getUserParam, GetUserResult.class, getUserHelperListener, is, true, true);
    }

    private void logout() {
        new JAlertConfirm(activity, getStr(R.string.setting_info_logout), false) {
            @Override
            protected void onYes() {
                super.onYes();

                Intent intent = new Intent(activity, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);
                activity.finish();

                preferenceManagers.setNickname("");
                preferenceManagers.setUid("");
                preferenceManagers.setAppExcute(false);

                new Analytics(getApplication()).getClickEvent("설정 화면", "로그아웃 클릭 이벤트", "0");

            }
        };
    }

    private void userLeave() {
        GetUserParam getUserParam = new GetUserParam();
        getUserParam.setUid(preferenceManagers.getUid());

        VolleyJsonHelper<GetUserParam, BaseResult> setUserBreakHelper = new VolleyJsonHelper<GetUserParam, BaseResult>(this);
        setUserBreakHelper.request(UrlDefine.API_SET_USER_BREAK, getUserParam, GetUserResult.class, setUserBreakHelperListener, false, true, true);

        new Analytics(getApplication()).getClickEvent("설정 화면", "탈퇴 클릭 이벤트", "0");
    }

    private void moveActivity(Intent intent) {
        intent.putExtra("type", ValueDefine.KAKAO_SETTING);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void goWeb(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void setPushUrl(String is) {
        Device device = JUtil.getDevice(activity);
        device.setIsPush(is);
        GetUserParam getUserParam = new GetUserParam();
        getUserParam.setDevice(device);

        VolleyJsonHelper<GetUserParam, GetUserResult> setUserPushHelper = new VolleyJsonHelper<GetUserParam, GetUserResult>(this);
        setUserPushHelper.request(UrlDefine.API_SET_PUSH, getUserParam, GetUserResult.class, setUserPushHelperListener, false, false, true);
    }

    private VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, GetUserResult> setUserPushHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, GetUserResult>() {
        @Override
        public void onSuccess(GetUserParam getUserParam, GetUserResult getUserResult) {
            if (getUserResult.getDevice().getIsPush().equals("Y")) {
                Toast.makeText(activity, getStr(R.string.push_on), Toast.LENGTH_SHORT).show();
                gcmPreferenceManager.setPush(true);
            } else {
                Toast.makeText(activity, getStr(R.string.push_off), Toast.LENGTH_SHORT).show();
                gcmPreferenceManager.setPush(false);
            }
            getContents(false);
        }

        @Override
        public void onMessage(GetUserParam getUserParam, GetUserResult getUserResult) {
        }

        @Override
        public void onError(GetUserParam getUserParam, VolleyError error) {
        }
    };
    private VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, GetUserResult> getUserHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, GetUserResult>() {
        @Override
        public void onSuccess(GetUserParam baseParam, GetUserResult getUserResult) {
            User user = getUserResult.getUser();
            preferenceManagers.setNickname(user.getNickname());
            getProfileImg(user);
            getContentsServer(user);

        }

        @Override
        public void onMessage(GetUserParam baseParam, GetUserResult getUserResult) {
        }

        @Override
        public void onError(GetUserParam baseParam, VolleyError error) {
        }
    };
    private VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, BaseResult> setUserBreakHelperListener = new VolleyJsonHelper.VolleyJsonHelperListener<GetUserParam, BaseResult>() {
        @Override
        public void onSuccess(GetUserParam baseParam, BaseResult baseResult) {
            PreferenceManagers pref = new PreferenceManagers(activity);
            Intent intent = new Intent(activity, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();

            pref.setUid("");
            pref.setNickname("");
            pref.setAppExcute(false);
        }

        @Override
        public void onMessage(GetUserParam baseParam, BaseResult baseResult) {
        }

        @Override
        public void onError(GetUserParam baseParam, VolleyError error) {
        }
    };

    public void menuClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.imgProfile:
                if (preferenceManagers.getUid().equals("")) {
                    intent = new Intent(activity, KaKaoActivity.class);
                    moveActivity(intent);
                    return;
                }
                intent = new Intent(activity, ProfileImageActivity.class);
                intent.putExtra("image", profileImage);
                moveActivity(intent);
                break;
            case R.id.btnNickname:
            case R.id.txtNickname:
                if (preferenceManagers.getUid().equals("")) {
                    intent = new Intent(activity, KaKaoActivity.class);
                    moveActivity(intent);
                    return;
                }
                intent = new Intent(activity, NicknameChangeActivity.class);
                intent.putExtra("nickname", preferenceManagers.getNickname());
                moveActivity(intent);
                break;
            case R.id.btnLogin:
                intent = new Intent(activity, KaKaoActivity.class);
                moveActivity(intent);
                break;
            case R.id.btnWeather:
                new Analytics(getApplication()).getClickEvent("설정 화면", "날씨 ON,OFF 클릭 이벤트", "0");
                if (preferenceManagers.isWeather()) {
                    preferenceManagers.setWeather(false);
                    Toast.makeText(activity, getStr(R.string.weather_off), Toast.LENGTH_SHORT).show();
                } else {
                    preferenceManagers.setWeather(true);
                    Toast.makeText(activity, getStr(R.string.weather_on), Toast.LENGTH_SHORT).show();
                }
                getContents(false);
                break;
            case R.id.btnPush:
                new Analytics(getApplication()).getClickEvent("설정 화면", "푸시 ON,OFF 클릭 이벤트", "0");
                if (gcmPreferenceManager.isPush()) {
                    setPushUrl("N");

                } else {
                    setPushUrl("Y");

                }
                break;
            case R.id.btnUse:
                goWeb(UrlDefine.API + "/" + USE_URL);
                break;
            case R.id.btnInfo:
                goWeb(UrlDefine.API + "/" + INFO_URL);
                break;
            case R.id.btnLogOut:
                logout();
                break;
            case R.id.btnBreak:
                new JAlertConfirm(activity, getStr(R.string.setting_info_leave), false) {
                    @Override
                    protected void onYes() {
                        super.onYes();
                        new JAlertConfirm(activity, getStr(R.string.setting_info_leave02), false) {
                            @Override
                            protected void onYes() {
                                super.onYes();
                                userLeave();
                            }
                        };
                    }
                };
                break;
        }
    }

}
