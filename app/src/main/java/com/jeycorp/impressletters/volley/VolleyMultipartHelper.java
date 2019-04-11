package com.jeycorp.impressletters.volley;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.jeycorp.impressletters.result.BaseResult;
import com.jeycorp.impressletters.volley.VolleyMultipartRequest.VolleyMultipartRequestListener;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class VolleyMultipartHelper<RESULT> {

    // jssong : for Load Balancing
    private static final int DELAY_REQUEST_TIME = 1000;
    private FragmentActivity activity;
    private boolean messageDialog;
    private boolean errorDialog;
    private MultipartEntityBuilder builder;

    public VolleyMultipartHelper(FragmentActivity activity) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
        builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Charset.forName("UTF-8"));
    }

    public interface VolleyMultipartHelperListener<RESULT> {
        public void onSuccess(RESULT result);

        public void onMessage(RESULT result);

        public void onError(VolleyError error);
    }

    private VolleyMultipartHelperListener<RESULT> volleyMultipartHelperListener;
    private VolleyMultipartRequest<RESULT> volleyMultipartRequest;

    private VolleyMultipartRequestListener<RESULT> volleyMultipartRequestListener = new VolleyMultipartRequestListener<RESULT>() {

        @Override
        public void onSuccessResult(RESULT result) {
            // TODO Auto-generated method stub
            VolleyDialog.dismissProgressDialog();

            if (volleyMultipartHelperListener != null) {
                BaseResult baseResult = (BaseResult) result;
                int resultCode = baseResult.getResultCode();

                switch (resultCode) {
                    case BaseResult.RESULT_CODE_MESSAGE:
                        if (messageDialog) {
                            VolleyDialog.showMessageDialog(activity, baseResult.getResultMessage());
                        }
                        volleyMultipartHelperListener.onMessage(result);
                        break;
                    default:
                        volleyMultipartHelperListener.onSuccess(result);
                        break;
                }
            }
        }

        @Override
        public void onErrorResult(VolleyError error) {
            // TODO Auto-generated method stub
            VolleyDialog.dismissProgressDialog();
            if (errorDialog) {
                VolleyDialog.showMessageDialog(activity, error.getClass().getSimpleName());
            }

            if (volleyMultipartHelperListener != null) {
                volleyMultipartHelperListener.onError(error);
            }

            error.printStackTrace();
        }
    };

    private OnCancelListener onProgressCancelListener = new OnCancelListener() {

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            cancelRequest();
        }
    };

    private void cancelRequest() {
        if (volleyMultipartRequest != null && volleyMultipartRequest.isCanceled() == false) {
            volleyMultipartRequest.cancel();
        }
    }

    public void addFile(String paramName, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            builder.addPart(paramName, new StringBody("", ContentType.create("text/plain", "UTF-8")));
        } else {
            File file = new File(filePath);
            builder.addPart(paramName, new FileBody(file));
        }
    }

    public void addParam(String paramName, String value) {
        if (value == null) {
            value = "";
        }

        builder.addPart(paramName, new StringBody(value, ContentType.create("text/plain", "UTF-8")));
    }

    public void request(String url, Type resultType, VolleyMultipartHelperListener<RESULT> volleyMultipartHelperListener) {
        request(url, resultType, volleyMultipartHelperListener, true, true, true);
    }

    public void request(String url, Type resultType, VolleyMultipartHelperListener<RESULT> volleyMultipartHelperListener, boolean progress, boolean messageDialog, boolean errorDialog) {
        HttpEntity httpEntity = builder.build();
        request(url, httpEntity, resultType, volleyMultipartHelperListener, progress, messageDialog, errorDialog);
    }


    public void request(String url, HttpEntity httpEntity, Type resultType, VolleyMultipartHelperListener<RESULT> volleyMultipartHelperListener) {
        request(url, httpEntity, resultType, volleyMultipartHelperListener, true, true, true);
    }

    public void request(String url, HttpEntity httpEntity, Type resultType, VolleyMultipartHelperListener<RESULT> volleyMultipartHelperListener, boolean progress, boolean messageDialog, boolean errorDialog) {

        this.messageDialog = messageDialog;
        this.errorDialog = errorDialog;

        if (progress) {
            VolleyDialog.showProgressDialog(activity, onProgressCancelListener);
        }

        this.volleyMultipartHelperListener = volleyMultipartHelperListener;
        cancelRequest();

        volleyMultipartRequest = new VolleyMultipartRequest<RESULT>(url, httpEntity, resultType, volleyMultipartRequestListener);
        VolleyQueue.getRequestQueue().add(volleyMultipartRequest);
    }
}
