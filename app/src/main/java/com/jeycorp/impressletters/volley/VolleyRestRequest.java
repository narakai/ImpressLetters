package com.jeycorp.impressletters.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public abstract class VolleyRestRequest<T> extends Request<JSONArray> {
    
    private Map<String, String> params;
    private final Listener<JSONArray> listener;
//    private Type resultType;
    
	public VolleyRestRequest(int method, String url, Map<String, String> params, Listener<JSONArray> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		// TODO Auto-generated constructor stub
		this.listener = listener;
		this.params = params;
//		this.resultType = responseType;
		setRetryPolicy(new DefaultRetryPolicy(VolleyDefine.DEFAULT_TIMEOUT_MS, VolleyDefine.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		
		StringBuilder log = new StringBuilder(url);
				
		if(params != null) {
			Iterator<String> iterator = params.values().iterator();
			
			int i = 0;
			while(iterator.hasNext()) {
				String next = iterator.next();				
				log.append(String.format(Locale.getDefault(), "\n[post param(%d)] : %s", i++, next));
			}		
		}
		
		Log.i(getClass().getSimpleName(), "[request] : " + log.toString());
	}
	
	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {
		// TODO Auto-generated method stub
		String responseJson = new String(networkResponse.data);
		Log.i(getClass().getName(), "[response] : " + responseJson);
		JSONArray jsonArray = null ;
		try {
			 jsonArray = new JSONArray(responseJson);
			//onValueResult(jsonArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//T response = GsonDefine.gson.fromJson(responseJson, resultType);
		
		return Response.success(jsonArray, HttpHeaderParser.parseCacheHeaders(networkResponse));
		//return null;
	}


	//protected abstract void onValueResult(JSONArray response);
	

	@Override
	protected void deliverResponse(JSONArray response) {
	
		listener.onResponse(response);
	}
	

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		// TODO Auto-generated method stub
		return params;
	}
}
