package com.jeycorp.impressletters;


import android.util.Log;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.param.GetUserParam;
import com.jeycorp.impressletters.result.GetUserResult;
import com.jeycorp.impressletters.type.Device;
import com.jeycorp.impressletters.utill.GcmPreferenceManager;
import com.jeycorp.impressletters.utill.JUtil;
import com.jeycorp.impressletters.volley.VolleyJsonBackHelper;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIIDService";

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
        GcmPreferenceManager gcmPreferenceManager = new GcmPreferenceManager(this);
        gcmPreferenceManager.setGcmId(token);

        GcmPreferenceManager manager = new GcmPreferenceManager(this);
        manager.setGcmId(token);
        Log.e("푸시","푸시(이니트):"+ token);

        Device device = JUtil.getDevice(this);
        GetUserParam getUserParam = new GetUserParam();
        getUserParam.setDevice(device);

        VolleyJsonBackHelper<GetUserParam,GetUserResult> getIntroHelper = new VolleyJsonBackHelper<GetUserParam,GetUserResult>(this);
        getIntroHelper.request(UrlDefine.API_SET_DEVICE,getUserParam, GetUserResult.class,getIntroHelperListener);
        /*
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token", token)
                .build();

        //request
        Request request = new Request.Builder()
                .url("http://서버주소/fcm/register.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }
    private VolleyJsonBackHelper.VolleyJsonHelperListener<GetUserParam,GetUserResult> getIntroHelperListener = new VolleyJsonBackHelper.VolleyJsonHelperListener<GetUserParam, GetUserResult>() {
        @Override
        public void onSuccess(GetUserParam getUserParam, GetUserResult getUserResult) {

        }

        @Override
        public void onMessage(GetUserParam getUserParam, GetUserResult getUserResult) {

        }

        @Override
        public void onError(GetUserParam getUserParam, VolleyError error) {
        }
    };
}
