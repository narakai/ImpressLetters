package com.jeycorp.impressletters.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class VolleyJsonRequest<PARAM, RESULT> extends Request<RESULT> {
	
	public interface VolleyJsonRequestListener<PARAM, RESULT> {
		public void onSuccessResult(PARAM param, RESULT result);
		public void onErrorResult(PARAM param, VolleyError error);
	}
	
    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
        String.format("application/json; charset=%s", PROTOCOL_CHARSET);
	
	private final VolleyJsonRequestListener<PARAM, RESULT> volleyJsonRequestListener;
	private PARAM param;
    private Type resultType;
    

	public VolleyJsonRequest(String url, PARAM param, Type resultType, VolleyJsonRequestListener<PARAM, RESULT> volleyJsonRequestListener) {
		super(Method.POST, url, null);
		// TODO Auto-generated constructor stub
		setRetryPolicy(new DefaultRetryPolicy(VolleyDefine.DEFAULT_TIMEOUT_MS, VolleyDefine.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		this.param = param;
		this.resultType = resultType;
		this.volleyJsonRequestListener = volleyJsonRequestListener;

	}

	@Override
	protected Response<RESULT> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		String responseJson = new String(response.data);
		VolleyLog.i(getClass().getName(), "responseJson : " + responseJson);
		RESULT result = VolleyDefine.gson.fromJson(responseJson, resultType);
		return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	protected void deliverResponse(RESULT response) {
		// TODO Auto-generated method stub
		volleyJsonRequestListener.onSuccessResult(param, response);
	}

	@Override
	public void deliverError(VolleyError error) {
		// TODO Auto-generated method stub
		super.deliverError(error);
		volleyJsonRequestListener.onErrorResult(param, error);
	}

	@Override
	public String getBodyContentType() {
		// TODO Auto-generated method stub
		return PROTOCOL_CONTENT_TYPE;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		// TODO Auto-generated method stub
		try {
			String paramJson = VolleyDefine.gson.toJson(param);
			VolleyLog.i(getClass().getName(), "paramJson : " + paramJson);
			return paramJson.getBytes(PROTOCOL_CHARSET);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	


}
