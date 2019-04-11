package com.jeycorp.impressletters.volley;


import android.app.Activity;
import android.content.DialogInterface;

import com.android.volley.VolleyError;
import com.jeycorp.impressletters.result.BaseResult;

import java.lang.reflect.Type;

public class VolleyJsonActivityHelper<PARAM, RESULT> {

    // jssong : for Load Balancing
    private static final int DELAY_REQUEST_TIME = 1000;
    private Activity activity;
    private boolean messageDialog;
    private boolean errorDialog;

    public VolleyJsonActivityHelper(Activity activity) {
        // TODO Auto-generated constructor stub
        this.activity = activity;
    }

    public interface VolleyJsonHelperListener<PARAM, RESULT> {
        public void onSuccess(PARAM param, RESULT result);
        public void onMessage(PARAM param, RESULT result);
        public void onError(PARAM param, VolleyError error);
    }

    private VolleyJsonHelperListener<PARAM, RESULT> volleyJsonHelperListener;
    private VolleyJsonRequest<PARAM, RESULT> volleyJsonRequest;

    private VolleyJsonRequest.VolleyJsonRequestListener<PARAM, RESULT> volleyJsonRequestListener = new VolleyJsonRequest.VolleyJsonRequestListener<PARAM, RESULT>() {

        @Override
        public void onSuccessResult(PARAM param, RESULT result) {
            // TODO Auto-generated method stub
            VolleyDialog.dismissProgressDialog();

            if(volleyJsonHelperListener != null) {
                BaseResult baseResult = (BaseResult) result;
                int resultCode = baseResult.getResultCode();

                switch(resultCode) {
                    case BaseResult.RESULT_CODE_MESSAGE:
                        if(messageDialog) {
                            VolleyDialog.showMessageDialog(activity, baseResult.getResultMessage());
                        }
                        volleyJsonHelperListener.onMessage(param, result);
                        break;
                    default:
                        volleyJsonHelperListener.onSuccess(param, result);
                        break;
                }
            }
        }

        @Override
        public void onErrorResult(PARAM param, VolleyError error) {
            // TODO Auto-generated method stub
            VolleyDialog.dismissProgressDialog();
            if(errorDialog) {
                VolleyDialog.showMessageDialog(activity, error.getClass().getSimpleName());
            }

            if(volleyJsonHelperListener != null) {
                volleyJsonHelperListener.onError(param, error);
            }

            error.printStackTrace();
        }
    };

    private DialogInterface.OnCancelListener onProgressCancelListener = new DialogInterface.OnCancelListener() {

        @Override
        public void onCancel(DialogInterface dialog) {
            // TODO Auto-generated method stub
            cancelRequest();
        }
    };

    private void cancelRequest(){
        if(volleyJsonRequest != null && volleyJsonRequest.isCanceled() == false) {
            volleyJsonRequest.cancel();
        }
    }

    public void request(String url, PARAM param, Type resultType, VolleyJsonHelperListener<PARAM, RESULT> volleyJsonHelperListener) {
        request(url, param, resultType, volleyJsonHelperListener, true, true, true);
    }

    public void request(String url, PARAM param, Type resultType, VolleyJsonHelperListener<PARAM, RESULT> volleyJsonHelperListener, boolean progress, boolean messageDialog, boolean errorDialog) {

        this.messageDialog = messageDialog;
        this.errorDialog = errorDialog;

        if(progress) {
           // VolleyDialog.showProgressDialog(activity, onProgressCancelListener);
        }

        this.volleyJsonHelperListener = volleyJsonHelperListener;
        cancelRequest();
        volleyJsonRequest = new VolleyJsonRequest<PARAM, RESULT>(url, param, resultType, volleyJsonRequestListener);
        VolleyQueue.getRequestQueue().add(volleyJsonRequest);
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				try {
//					Thread.sleep(DELAY_REQUEST_TIME);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		}).start();
    }
}
