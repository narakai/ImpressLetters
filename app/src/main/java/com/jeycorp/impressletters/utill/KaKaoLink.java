package com.jeycorp.impressletters.utill;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.dialog.Share_Dialog;
import com.jeycorp.impressletters.type.GoodsBoard;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.util.Hashtable;
import java.util.Map;

public class KaKaoLink {
	private Activity activity;
	private Share_Dialog shareDialog;
	public KaKaoLink(Activity activity) {
		this.activity = activity;
	}

	public  void sendKakao(GoodsBoard goodsBoard, int imgWidth, int imgHeight){
		int contentsLen = goodsBoard.getTitle().length();
		String contents;
		if(contentsLen>30){
			contents = goodsBoard.getTitle().substring(0, 30);
		}else{
			contents = goodsBoard.getTitle().substring(0, contentsLen);
		}

		String text ="[ 부자되는 글 ]\n"+contents;
		String imageUrl = UrlDefine.DATA+goodsBoard.getImgBannerUrl();

		String linkText = "[상세보기 이동]";
		String ButtonText = "자세히 보기";
		String buttonUrl = UrlDefine.SERVER+"/share_link.php?seq="+goodsBoard.getSeq();

		int width = 200;
		int height = 133;

		if(goodsBoard.getImgBannerUrl()==null || goodsBoard.getImgBannerUrl().contains(".gif")) {
			if(goodsBoard.getImgBannerUrl().contains(".gif")){
				imageUrl = UrlDefine.DATA + goodsBoard.getImgThumbUrl();
			}else{
				imageUrl = UrlDefine.SERVER + UrlDefine.GET_IMAGE_ICON;
			}
			width = 200;
			height = 133;
		}else{
			float percent = 300f/imgWidth;

			width = (int)(imgWidth*percent);
			height = (int)(imgHeight*percent);
		}

		shareDialog = new Share_Dialog(activity,activity,this);
		shareDialog.setInitData(goodsBoard.getSeq(), text, goodsBoard.getContents(), imageUrl, linkText, ButtonText, buttonUrl, width, height);
		shareDialog.show();
		//sendKakaoStory(goodsBoard.getSeq(), text, imageUrl, linkText, ButtonText, buttonUrl, width, height);
		//sendKakaoTalkLink(goodsBoard.getSeq(), text, imageUrl, linkText, ButtonText, buttonUrl, width, height);

	}
	public  void webSendKakao(String imgUrl,int imgWidth, int imgHeight){

		int width = 200;
		int height = 133;

		float percent = 300f/imgWidth;

		width = (int)(imgWidth*percent);
		height = (int)(imgHeight*percent);

		sendKakaoImage(imgUrl, width, height);

	}



	public void sendKakaoTalkLink(String text, String imageUrl, String linkText, String ButtonText, String buttonUrl, int width, int height) {
		try {
			FeedTemplate params = FeedTemplate
					.newBuilder(ContentObject.newBuilder(text,
							imageUrl,
							LinkObject.newBuilder().setWebUrl(buttonUrl)
									.setMobileWebUrl(buttonUrl).build())
							.setDescrption(linkText)
							.build())
					.addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl(buttonUrl).setMobileWebUrl(buttonUrl).build()))
					.addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
							.setWebUrl(buttonUrl)
							.setMobileWebUrl(buttonUrl)
							.build()))
					.build();

			KakaoLinkService.getInstance().sendDefault(activity, params, new ResponseCallback<KakaoLinkResponse>() {
				@Override
				public void onFailure(ErrorResult errorResult) {
					Logger.e(errorResult.toString());
				}

				@Override
				public void onSuccess(KakaoLinkResponse result) {

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public void sendKakaoTalkLink(long seq, String text, String imageUrl, String linkText, String ButtonText, String buttonUrl, int width, int height) {
		try {

			FeedTemplate params = FeedTemplate
					.newBuilder(ContentObject.newBuilder(text,
							imageUrl,
							LinkObject.newBuilder().setWebUrl(buttonUrl)
									.setMobileWebUrl(buttonUrl).build())
							.setDescrption(linkText)
							.build())
					.addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl(buttonUrl).setMobileWebUrl(buttonUrl).build()))
					.addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
							.setWebUrl(buttonUrl)
							.setMobileWebUrl(buttonUrl)
							.setAndroidExecutionParams("seq=" + String.valueOf(seq))
							.setIosExecutionParams("seq=" + String.valueOf(seq))
							.build()))
					.build();
			KakaoLinkService.getInstance().sendDefault(activity, params, new ResponseCallback<KakaoLinkResponse>() {
				@Override
				public void onFailure(ErrorResult errorResult) {
					Logger.e(errorResult.toString());
				}

				@Override
				public void onSuccess(KakaoLinkResponse result) {

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	public void sendKakaoImage(String imgUrl, int width, int height) {
		try {
			FeedTemplate params = FeedTemplate
					.newBuilder(ContentObject.newBuilder("[ 부자되는 글 ]",
							imgUrl,
							LinkObject.newBuilder().setWebUrl(imgUrl)
									.setMobileWebUrl(imgUrl).build())
							.build())
					.build();

			KakaoLinkService.getInstance().sendDefault(activity, params, new ResponseCallback<KakaoLinkResponse>() {
				@Override
				public void onFailure(ErrorResult errorResult) {
					Logger.e(errorResult.toString());
				}

				@Override
				public void onSuccess(KakaoLinkResponse result) {

				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	public void sendKakaoStory(long seq,String text,String content, String imageUrl, String linkText, String ButtonText, String buttonUrl,int width, int height) {
		String title ="[ 부자되는 글 ]\n";
		Map<String, Object> urlInfoAndroid = new Hashtable<String, Object>(1);
		urlInfoAndroid.put("title", text.replace(title,""));
		urlInfoAndroid.put("desc", Html.fromHtml(content).toString());
		urlInfoAndroid.put("imageurl", new String[] {imageUrl});
		urlInfoAndroid.put("type", "article");

		// Recommended: Use application context for parameter.
		StoryLink storyLink = StoryLink.getLink(activity);

		// check, intent is available.
		if (!storyLink.isAvailableIntent()) {

			return;
		}

		/**
		 * @param activity
		 * @param post (message or url)
		 * @param appId
		 * @param appVer
		 * @param appName
		 * @param encoding
		 * @param urlInfoArray
		 */
		PackageInfo pi = null;

		try {
			pi = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		String verSion = pi.versionName;

		storyLink.openKakaoLink(activity,text+"\n"+buttonUrl,
				activity.getPackageName(),
				verSion,
				"부자되는 글",
				"UTF-8",
				urlInfoAndroid);
	}

	public void publishStory(Bundle savedInstanceState, String imgUrl, String title, String urlLink) {
		// uiHelper = new UiLifecycleHelper(activity, callback);
		Log.e("sin", "imgUrl : " + imgUrl);
		ShareDialog shareDialog = new ShareDialog(activity);
		ShareLinkContent content = new ShareLinkContent.Builder()
				//링크 제목
				.setContentTitle(title)
				//게시물에 표시될 썸네일 이미지의 URL
				//  .setImageUrl(Uri.parse(imgUrl))
				//공유될 링크
				.setContentUrl(Uri.parse(urlLink))
				.build();
		shareDialog.show(content, ShareDialog.Mode.FEED);
		// ShareDialog shareDialog = new ShareDialog(activity);

       /* if (FacebookDialog.canPresentShareDialog(activity,
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            // Publish the post using the Share Dialog
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(activity)
                    .setLink(urlLink)
                    .setName(title)
                    .setPicture(imgUrl)
                    .build();
            //  uiHelper.trackPendingDialogCall(shareDialog.present());
        } else {

        }*/
	}

    

}
