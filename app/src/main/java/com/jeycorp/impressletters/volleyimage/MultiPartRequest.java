package com.jeycorp.impressletters.volleyimage;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.google.gson.Gson;
import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.utill.JAlertDialog;
import com.jeycorp.impressletters.volley.VolleyDialog;
import com.jeycorp.impressletters.R;

public class MultiPartRequest {
	private ProgressDialog progressDialog;
	private Activity activity;
	private String url;
	private Map<String, String> fileMap;
	private Map<String, String> paramMap;
	private Type resultType;

	public MultiPartRequest(Activity activity, String url, Type resultType) {
		this.activity = activity;
		this.url = url;
		this.fileMap = new HashMap<String, String>();
		this.paramMap = new HashMap<String, String>();
		this.resultType = resultType;

		progressDialog = new ProgressDialog(activity, ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setTitle(activity.getString(R.string.app_name));
		progressDialog.setMessage(activity.getString(R.string.please_wait));
		progressDialog.setCancelable(true);
	}

	public void addFile(String paramName, String filePath) {
		fileMap.put(paramName, filePath);
	}

	public void addParam(String paramName, String value) {
		paramMap.put(paramName, value);
	}

	public void submit() {
		new Upload().execute();
	}

	private synchronized void showProgressDialog() {
		VolleyDialog.dismissProgressDialog();
		VolleyDialog.showProgressDialog((FragmentActivity) activity, new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				
			}
		});
//		dismissProgressDialog();
//		progressDialog.show();
	}

	private synchronized void dismissProgressDialog() {
		VolleyDialog.dismissProgressDialog();
//		if (progressDialog != null && progressDialog.isShowing()) {
//			progressDialog.dismiss();
//		}
	}

	protected void onResult(BaseResult result) {

	}

	class Upload extends AsyncTask<Void, Void, Void> {
		private boolean success;
		private String resultString;

		public Upload() {
			showProgressDialog();
		}

		@Override
		protected void onPostExecute(Void res) {
			super.onPostExecute(res);
			dismissProgressDialog();
			
			Gson gson = new Gson();
			BaseResult result = (BaseResult) gson.fromJson(resultString, resultType);
			
			if (result.getResultCode() == BaseResult.RESULT_CODE_MESSAGE) {
				JAlertDialog.showMessage(activity, result.getResultMessage());
			} else if(success != true) {
				JAlertDialog.showMessage(activity,activity.getString(R.string.network_error));
			} else {
				onResult((BaseResult) result);
			}
		}

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			httppost.setHeader("Connection", "Keep-Alive");
			httppost.setHeader("Accept-Charset", "UTF-8");
			httppost.setHeader("ENCTYPE", "multipart/form-data");

			MultipartEntity reqEntity = new MultipartEntity();

			Iterator<String> iterator_file = fileMap.keySet().iterator();
			while (iterator_file.hasNext()) {
				String key = (String) iterator_file.next();
				File file = new File(fileMap.get(key));
				reqEntity.addPart(key, new FileBody(file));
			}

			// try {
			// reqEntity.addPart("att_file", new
			// FileBody(File.createTempFile("aaa", "bbb")));
			// } catch (IOException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }

			Iterator<String> iterator_param = paramMap.keySet().iterator();
			while (iterator_param.hasNext()) {
				String key = (String) iterator_param.next();
				try {
					reqEntity.addPart(key, new StringBody(paramMap.get(key),
							Charset.forName("UTF-8")));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			httppost.setEntity(reqEntity);

			HttpParams hparams = httpclient.getParams();
			hparams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			HttpConnectionParams.setConnectionTimeout(hparams, 15000);
			HttpConnectionParams.setSoTimeout(hparams, 15000);

			try {
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity httpEntity = response.getEntity();
				InputStream is = httpEntity.getContent();
				resultString = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                	resultString += line;
                }
                reader.close();
				//Log.d("test", "MultiPartRequest:" + resultString);
				
				this.success = true;
			} catch (ClientProtocolException e) {
				this.success = false;
				e.printStackTrace();
			} catch (IOException e) {
				this.success = false;
				e.printStackTrace();
			} finally {
				httpclient.getConnectionManager().shutdown();
			}

			if (this.success != true) {
				this.success = false;
			}

			return null;
		}

	}

}
