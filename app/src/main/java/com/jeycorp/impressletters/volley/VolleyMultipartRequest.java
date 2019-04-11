package com.jeycorp.impressletters.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.HttpEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class VolleyMultipartRequest<RESULT> extends Request<RESULT> {
    
	public interface VolleyMultipartRequestListener<RESULT> {
		public void onSuccessResult(RESULT result);
		public void onErrorResult(VolleyError error);
	}
	
	private HttpEntity httpEntity;
	private Type resultType;
	VolleyMultipartRequestListener<RESULT> volleyMultipartRequestListener;
    	
	public VolleyMultipartRequest(String url, HttpEntity httpEntity, Type resultType, VolleyMultipartRequestListener<RESULT> volleyMultipartRequestListener) {
		super(Method.POST, url, null);
		// TODO Auto-generated constructor stub
				
		this.httpEntity = httpEntity;
		this.resultType = resultType;
		this.volleyMultipartRequestListener = volleyMultipartRequestListener;
		setRetryPolicy(new DefaultRetryPolicy(VolleyDefine.DEFAULT_TIMEOUT_MS, VolleyDefine.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	@Override
	protected Response<RESULT> parseNetworkResponse(NetworkResponse networkResponse) {
		// TODO Auto-generated method stub
		String responseJson = new String(networkResponse.data);
		VolleyLog.i(getClass().getName(), "responseJson : " + responseJson);
		RESULT result = VolleyDefine.gson.fromJson(responseJson, resultType);
		return Response.success(result, HttpHeaderParser.parseCacheHeaders(networkResponse));
	}

	@Override
	protected void deliverResponse(RESULT response) {
		// TODO Auto-generated method stub
		volleyMultipartRequestListener.onSuccessResult(response);
	}

	@Override
	public void deliverError(VolleyError error) {
		// TODO Auto-generated method stub
		super.deliverError(error);
		volleyMultipartRequestListener.onErrorResult(error);
	}

	@Override
	public String getBodyContentType() {
		// TODO Auto-generated method stub
		String contentType = httpEntity.getContentType().getValue();
		VolleyLog.i(getClass().getName(), "getBodyContentType : " + contentType);
		return contentType;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		// TODO Auto-generated method stub
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			httpEntity.writeTo(os);
			return os.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}	

}
