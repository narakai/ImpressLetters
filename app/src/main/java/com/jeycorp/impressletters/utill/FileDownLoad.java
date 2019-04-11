package com.jeycorp.impressletters.utill;

import android.app.Activity;
import android.app.DownloadManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import com.jeycorp.impressletters.activity.KaKaoActivity;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.param.GetUserParam;
import com.jeycorp.impressletters.type.GoodsBoard;


public class FileDownLoad {
	private Activity activity;
	
	public FileDownLoad(Activity activity) {
		this.activity = activity;
	}


	public void setProfileImgUrl(final String url, final String name, final GetUserParam getUserParam){
		HttpDownloader downloader = new HttpDownloader();
		downloader
				.setOnDownloadFileCompletedListener(new HttpDownloaderImpl.OnDownloadFileCompletedListener() {
					@Override
					public void onDownloadFileCompleted(
							HttpDownloaderImpl.DownloadFileCompleted event) {
						// downloadProgress.cancel();

						//ImageView imgPicture = (ImageView) activity.findViewById(R.id.imgProfile);

						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 2;
						Bitmap bitmap = BitmapFactory.decodeFile(name, options);
						
//						imgPicture.setImageBitmap(bitmap);
//
//						ImageView imgSqr = (ImageView)activity.findViewById(R.id.imgSqr);
//						imgSqr.setVisibility(View.VISIBLE);

						String imagePath = name;
						String extArray[] = imagePath.split("\\.");
						String ext = extArray[1];
						if(activity instanceof KaKaoActivity){
							((KaKaoActivity)activity).setProfile(imagePath, ext, getUserParam);
						}
					}
				});

		downloader
				.setOnDownloadProgressChangedListener(new HttpDownloaderImpl.OnDownloadProgressChangedListener() {
					@Override
					public void onDownloadProgressChanged(long bytesReceived,
							long totalBytesReceived) {
						// if (downloadProgress.getMax() != (totalBytesReceived
						// / 1024)) {
						// downloadProgress
						// .setMax((int) (totalBytesReceived / 1024));
						// }
						// downloadProgress
						// .setProgress((int) (bytesReceived / 1024));
						//
						// fileSize = (int) bytesReceived;

					}
				});

		downloader.downloadFileAsync(url, name, null);
	}


}
