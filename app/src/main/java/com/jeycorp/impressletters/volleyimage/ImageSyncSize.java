package com.jeycorp.impressletters.volleyimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


import com.jeycorp.impressletters.type.GoodsBoard;
import com.jeycorp.impressletters.volley.VolleyDialog;
import com.jeycorp.impressletters.utill.KaKaoLink;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ImageSyncSize {
    private Activity activity;
    private ImageView imgView;
    private boolean isRound=false;
    private GoodsBoard goodsBoard;
    private int width = 200;
    private int height = 133;

    public ImageSyncSize(Activity activity, GoodsBoard goodsBoard) {
        this.activity = activity;
        this.goodsBoard = goodsBoard;
    }

    public void excute(String imgUrl){
        new ImageTask().execute(imgUrl);
    }
    public void excuteImageUrl(String imgUrl){
        new WebImageTask().execute(imgUrl);
    }


    public class ImageTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bm = null;
            try {
                URL url = new URL(params[0]);

                URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new

                        BufferedInputStream(conn.getInputStream());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                bm = BitmapFactory.decodeStream(bis, null, options);

                width = bm.getWidth();
                height = bm.getHeight();

                bis.close();

            } catch (IOException e) {

            }
            return bm;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result!=null){

                KaKaoLink kaKaoLink = new KaKaoLink(activity);
                kaKaoLink.sendKakao(goodsBoard,width,height);
//                imgView.setVisibility(View.VISIBLE);
//                //imgView.setImageBitmap(result);
//                if(isRound){
//                    BitmapCustom bitmapCustom = new BitmapCustom(activity);
//                    //Bitmap b =result;
//                    Bitmap b = bitmapCustom.getRoundedBitmap(result);
//                    imgView.setImageBitmap(b);
//                }else{
//                    imgView.setImageBitmap(result);
//                }
            }
            VolleyDialog.dismissProgressDialog();
        }
    }
    public class WebImageTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(String... params) {
            Bitmap bm = null;
            try {
                URL url = new URL(params[0]);

                URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new

                        BufferedInputStream(conn.getInputStream());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                bm = BitmapFactory.decodeStream(bis, null, options);

                width = bm.getWidth();
                height = bm.getHeight();

                bis.close();

            } catch (IOException e) {

            }
            return params[0];
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null){
                Log.e("하하하","하하핳:"+result);
                KaKaoLink kaKaoLink = new KaKaoLink(activity);
                kaKaoLink.webSendKakao(result,width,height);
//                imgView.setVisibility(View.VISIBLE);
//                //imgView.setImageBitmap(result);
//                if(isRound){
//                    BitmapCustom bitmapCustom = new BitmapCustom(activity);
//                    //Bitmap b =result;
//                    Bitmap b = bitmapCustom.getRoundedBitmap(result);
//                    imgView.setImageBitmap(b);
//                }else{
//                    imgView.setImageBitmap(result);
//                }
            }
            VolleyDialog.dismissProgressDialog();
        }
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

}
