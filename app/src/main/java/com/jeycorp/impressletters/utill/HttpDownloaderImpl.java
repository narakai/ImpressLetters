package com.jeycorp.impressletters.utill;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;


public interface HttpDownloaderImpl {
	
	static class DownloadCompletedBase {
		public Throwable exception;
	}
	
	public static class DownloadStringCompleted extends DownloadCompletedBase {
		public String text;
	}
	
	public static class DownloadFileCompleted extends DownloadCompletedBase {
		public File file;
	}
	
	public static interface OnDownloadStringCompletedListener {
		public void onDownloadStringCompleted(DownloadStringCompleted event);
	}
	
	public static interface OnDownloadFileCompletedListener {
		public void onDownloadFileCompleted(DownloadFileCompleted event);
	}
	
	public static interface OnDownloadProgressChangedListener {
		public void onDownloadProgressChanged(long bytesReceived, long totalBytesReceived);
	}
	
	public void setOnDownloadStringCompletedListener(OnDownloadStringCompletedListener listener);
	public void setOnDownloadFileCompletedListener(OnDownloadFileCompletedListener listener);
	public void setOnDownloadProgressChangedListener(OnDownloadProgressChangedListener listener);
	
	public String downloadString(String address, ArrayList<NameValuePair> nameValuePairs) throws IOException;
	public void downloadStringAsync(String address, ArrayList<NameValuePair> nameValuePairs);
	public void downloadFileAsync(String address, String fileName, ArrayList<NameValuePair> nameValuePairs);
}
