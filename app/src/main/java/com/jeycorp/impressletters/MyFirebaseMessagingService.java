package com.jeycorp.impressletters;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.firebase.messaging.RemoteMessage;
import com.jeycorp.impressletters.activity.IntroActivity;
import com.jeycorp.impressletters.activity.MainActivity;
import com.jeycorp.impressletters.activity.NoticeActivity;
import com.jeycorp.impressletters.define.DirectoryDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.utill.GcmPreferenceManager;
import com.jeycorp.impressletters.utill.PushWakeLock;
import com.jeycorp.impressletters.volleyimage.NetworkImage;

import com.jeycorp.impressletters.define.UrlDefine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    private static final String TAG = "FirebaseMsgService";
    boolean isExcute = false;
    private GcmPreferenceManager gcmPreferenceManager;

    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //추가한것

        final String type = remoteMessage.getData().get("type");
        final String message = remoteMessage.getData().get("message");
        final String debugMode = remoteMessage.getData().get("debugMode");
        final String imgUrl = remoteMessage.getData().get("imgUrl");
        String mTitle="";
        final String goodsSeq = remoteMessage.getData().get("goodsSeq");
        boolean isSendPush = false;

        if(type.equals(ValueDefine.MESSAGE_DEFAULT)){
            mTitle = "부자되는 글";
        }
        else if(type.equals(ValueDefine.MESSAGE_CANCEL)){
            mTitle = "부자되는 글";
        }
        else{
            mTitle = "부자되는 글";
        }
        gcmPreferenceManager = new GcmPreferenceManager(this);
        if(gcmPreferenceManager.isPush()){
            if(UrlDefine.PUSH.equals(debugMode)) {
                isSendPush = true;
            }
        }


        if (TextUtils.isEmpty(mTitle)) {
            mTitle = this.getString(com.jeycorp.impressletters.R.string.app_name);
        }

        final String title = mTitle;


        if (isSendPush) {
            if (isExcute == false) {
                if(type.equals(ValueDefine.MESSAGE_GOODS) || type.equals(ValueDefine.MESSAGE_DEFAULT) || type.equals(ValueDefine.MESSAGE_NOTICE)){

                    gcmPreferenceManager.setPushCount(gcmPreferenceManager.getPushCount() + 1);
                    //gcmPreferenceManager.setPushCount(1);
                    int count = gcmPreferenceManager.getPushCount();// count 가 0 일 경우는 사라집니다.

                    Intent intents = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                    intents.putExtra("badge_count", count);
                    intents.putExtra("badge_count_package_name", "com.jeycorp.impressletters");
                    intents.putExtra("badge_count_class_name", "com.jeycorp.impressletters.activity.IntroActivity");
                    sendBroadcast(intents);
                }
                isExcute = true;
            }

            if(type.equals(ValueDefine.MESSAGE_GOODS) || type.equals(ValueDefine.MESSAGE_NOTICE)){
                if (TextUtils.isEmpty(imgUrl) == false) {
                    ArrayList<String> imageUrl = new ArrayList<String>();
                    imageUrl.add(UrlDefine.DATA + imgUrl);
                    new NetworkImage(imageUrl) {
                        @Override
                        protected void onResponse(ArrayList<Bitmap> bitmaps) {
                            super.onResponse(bitmaps);
                            if (bitmaps.get(0) != null) {
                                File fileCacheItem = new File(Environment.getExternalStorageDirectory().toString()+ DirectoryDefine.GOODTHINGS + imgUrl);
                                OutputStream out = null;
                                try {
                                    fileCacheItem.createNewFile();
                                    out = new FileOutputStream(fileCacheItem);
                                    bitmaps.get(0).compress(Bitmap.CompressFormat.PNG, 0, out);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            sendNotification(title,message,type,goodsSeq,bitmaps.get(0));
                        }
                    };
                }else{
                    sendNotification(title,message,type,goodsSeq,null);
                }
            }else{
                sendNotification(title,message,type,goodsSeq,null);
            }


        }


    }

    private void sendNotification(String title,String message,String type,String reservationSeq,Bitmap bitmap) {
        String strPackage = "";
        boolean isExcute = false;
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> proceses = am.getRunningAppProcesses();
        //프로세서 전체를 반복
        for(ActivityManager.RunningAppProcessInfo process : proceses)
        {		//IMPORTANCE_FOREGROUND
            if(process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND || process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND )
            {
                strPackage = process.processName;
                if(this.getPackageName().equals(strPackage)){
                    if(gcmPreferenceManager.isAppExcute()){
                        isExcute = true;
                    }
                }
            }

        }

        Intent intent = null;

        if(isExcute){
            if(type.equals(ValueDefine.MESSAGE_NOTICE)){
                intent = new Intent(this, NoticeActivity.class);
            }
            else if(type.equals(ValueDefine.MESSAGE_DEFAULT)){
                intent = new Intent(this, MainActivity.class);;
            }
            else if(type.equals(ValueDefine.MESSAGE_GOODS)){
                intent = new Intent(this, IntroActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("push", true);
                intent.putExtra("type", type);
                if(reservationSeq!=null){
                    intent.putExtra("goodsSeq", Long.parseLong(reservationSeq));
//                    GcmPreferenceManager gcmPreferenceManager = new GcmPreferenceManager(this);
//                    gcmPreferenceManager.setReservationSeq(Long.parseLong(reservationSeq));
                }
//
//                GcmPreferenceManager gcmPreferenceManager = new GcmPreferenceManager(this);
//                gcmPreferenceManager.setReservationSeq(Long.parseLong(reservationSeq));
            }
            else{
                intent = new Intent(this, MainActivity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else{
            intent = new Intent(this, IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("push", true);
            intent.putExtra("type", type);
            if(reservationSeq!=null){
                intent.putExtra("goodsSeq", Long.parseLong(reservationSeq));
//                GcmPreferenceManager gcmPreferenceManager = new GcmPreferenceManager(this);
//                gcmPreferenceManager.setReservationSeq(Long.parseLong(reservationSeq));
            }

        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (PushWakeLock.screenOn(this) == false) {
            PushWakeLock.acquireCpuWakeLock(this);
            PushWakeLock.releaseCpuLock();
        }

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notificationBuilder;
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChanel = new NotificationChannel("impressletters", "impressletters", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChanel);
            notificationBuilder = new NotificationCompat.Builder(this, mChanel.getId());
        } else {
            notificationBuilder = new NotificationCompat.Builder(this);
        }
        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setNumber(gcmPreferenceManager.getPushCount());
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_gcm);
        notificationBuilder.setContentIntent(pendingIntent);

        if(bitmap!=null){
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(title);
            bigPictureStyle.setSummaryText(message);
            bigPictureStyle.bigPicture(bitmap);
            notificationBuilder.setStyle(bigPictureStyle);
        }


        notificationBuilder.build().flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
