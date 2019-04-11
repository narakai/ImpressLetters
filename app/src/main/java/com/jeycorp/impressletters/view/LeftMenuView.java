package com.jeycorp.impressletters.view;

import android.content.Intent;
import android.view.View;

import com.jeycorp.impressletters.activity.BoardWriteActivity;
import com.jeycorp.impressletters.activity.MainActivity;
import com.jeycorp.impressletters.activity.NoticeActivity;
import com.jeycorp.impressletters.activity.SettingActivity;
import com.jeycorp.impressletters.activity.StorageActivity;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.GcmPreferenceManager;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.activity.KaKaoActivity;
import com.jeycorp.impressletters.activity.QuestionWebViewActivity;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.dialog.AccessDialog;
import com.jeycorp.impressletters.utill.JAlertConfirm;
import com.jeycorp.impressletters.utill.KaKaoLink;
import com.jeycorp.impressletters.utill.RegisterStatic;


public class LeftMenuView {
	public static MainActivity mainActivity;
	private MainActivity activity;
	private PreferenceManagers pref;
	private GcmPreferenceManager gcmPreferenceManager;

	public LeftMenuView(MainActivity activity) {
		this.activity = activity;
		pref = new PreferenceManagers(activity);
		gcmPreferenceManager = new GcmPreferenceManager(activity);

	}
	public void setInit(){
		getContents();
		setOnClick();
	}

	private void setOnClick(){
		activity.findViewById(R.id.btnSetting).setOnClickListener(leftMenuClick);
		activity.findViewById(R.id.btnStorage).setOnClickListener(leftMenuClick);
		activity.findViewById(R.id.btnNotice).setOnClickListener(leftMenuClick);
		activity.findViewById(R.id.btnNotice).setOnClickListener(leftMenuClick);
		activity.findViewById(R.id.btn1on1).setOnClickListener(leftMenuClick);
		activity.findViewById(R.id.btnShare).setOnClickListener(leftMenuClick);
		activity.findViewById(R.id.btnAssess).setOnClickListener(leftMenuClick);
		activity.findViewById(R.id.viewClose).setOnClickListener(leftMenuClick);
		activity.findViewById(R.id.btnGuide).setOnClickListener(leftMenuClick);
	}
	private void getContents(){
	}
	public String getStr(int resId) {
		return activity.getResources().getString(resId);
	}

	private boolean getUserLogin(){
		boolean isLogin = true;
		if(pref.getUid().equals("")){
			new JAlertConfirm(activity, getStr(R.string.setting_login_q), false) {
				@Override
				protected void onYes() {
					super.onYes();
					Intent intent = new Intent(activity,KaKaoActivity.class);
					intent.putExtra("type", ValueDefine.KAKAO_SETTING);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					activity.startActivity(intent);
				}
			};
			isLogin = false;
		}
		return isLogin;
	}


	View.OnClickListener leftMenuClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
				case R.id.btnSetting:
					intent = new Intent(activity, SettingActivity.class);
					new Analytics(activity.getApplication()).getClickEvent("부자되는 글   왼쪽메뉴", "설정 클릭 이벤트", "0");
					//intent = new Intent(activity, ReservationCompleteListActivity.class);
					break;
				case R.id.btnStorage:
					intent = new Intent(activity, StorageActivity.class);
					new Analytics(activity.getApplication()).getClickEvent("부자되는 글   왼쪽메뉴", "보관함 클릭 이벤트", "0");
					break;
				case R.id.btnNotice:
					intent = new Intent(activity, NoticeActivity.class);
					new Analytics(activity.getApplication()).getClickEvent("부자되는 글   왼쪽메뉴", "공지사항 클릭 이벤트", "0");
					//activity.movePager(1);
					break;
				case R.id.btn1on1:
					boolean isLogin = getUserLogin();
					if(isLogin) {
						intent = new Intent(activity, BoardWriteActivity.class);
						intent.putExtra("category", ValueDefine.BOARD_1ON1);
					}
					new Analytics(activity.getApplication()).getClickEvent("부자되는 글   왼쪽메뉴", "1:1문의 클릭 이벤트", "0");
					break;
				case R.id.btnShare:
					new Analytics(activity.getApplication()).getClickEvent("부자되는 글   왼쪽메뉴", "공유 클릭 이벤트", "0");

					String text ="부자되는 글";
					String imageUrl = UrlDefine.SERVER+ UrlDefine.GET_IMAGE_ICON;

					String linkText = "내마음속 힐링\n부자되는 글 여러분의 마음속에 좋은 생각만을 드리겠습니다.";
					String ButtonText = "자세히 보기";
					String buttonUrl = RegisterStatic.getUpdateUrl(activity);

					int width = 200;
					int height = 133;

					new KaKaoLink(activity).sendKakaoTalkLink(text, imageUrl, linkText, ButtonText, buttonUrl, width, height);
					//intent = new Intent(activity, MessageActivity.class);
					break;
				case R.id.btnAssess:
					new Analytics(activity.getApplication()).getClickEvent("부자되는 글   왼쪽메뉴", "평가하기 클릭 이벤트", "0");
					mainActivity = activity;
					new AccessDialog(activity,activity).show();
					//intent = new Intent(activity, UserEngineerInfoActivity.class);
					break;
				case R.id.btnGuide:
					mainActivity = activity;
					intent = new Intent(activity, QuestionWebViewActivity.class);
					new Analytics(activity.getApplication()).getClickEvent("부자되는 글   왼쪽메뉴", "가이드 클릭 이벤트", "0");
					break;
				case R.id.viewClose:
					activity.onBackPressed();
					break;

			}
			if(intent!=null){
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
			}

		}
	};

}
