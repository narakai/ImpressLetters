package com.jeycorp.impressletters.volleyimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import com.felipecsl.gifimageview.library.GifImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class NetworkGifImageView extends GifImageView {

    private String mUrl;
    private int mDefaultImageId;
    private int mErrorImageId;
    private byte[] gifBytes;
    private DownLoadImageThread downLoadImageThread;
    public String type = "DETAIL";


	public NetworkGifImageView(Context context) {
		 this(context, null);
	}

	public NetworkGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void clean() {
        this.mUrl = "";
        setDefaultImageOrNull();
    }

    public void setType(String type){
        this.type = type;
    }


	public String getImageUrl() {
		return this.mUrl;
	}


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        Drawable d = getDrawable();
        if(d!=null){

            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            if(type.equals("LIST")){
                setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
            }else{
                setMeasuredDimension(width, height);
            }


        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }



    public void setImageUrl(String url) {
        this.mUrl = url;
        if(downLoadImageThread == null) {
            downLoadImageThread = new DownLoadImageThread(url);
            downLoadImageThread.start();
        }
    }


    public void setDefaultImageResId(int defaultImage) {
        this.mDefaultImageId = defaultImage;
        setImageResource(defaultImage);
    }

    public void setErrorImageResId(int errorImage) {
        this.mErrorImageId = errorImage;
    }



    private void setDefaultImageOrNull() {
        if(this.mDefaultImageId != 0) {
            this.setImageResource(this.mDefaultImageId);
        } else {
            this.setImageBitmap((Bitmap) null);
        }
    }


    Handler downloadCompleteHandler = new Handler() {
        public void handleMessage(Message msg) {
            setBytes(gifBytes);
        }
    };

    class DownLoadImageThread extends Thread {
        private String url;

        public DownLoadImageThread(String url) {
            this.url = url;
        }


        public void run() {
            HttpURLConnection conn;
            URL url = null;
            try {
                url = new URL(this.url);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            if(url != null) {

                try {
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();


                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    InputStream inputStream = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    int read = -1;
                    try {
                        while ( (read = inputStream.read(buffer)) != -1 ) {
                            bos.write(buffer, 0, read);
                        }

                        gifBytes = bos.toByteArray();
                        bos.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    inputStream.close();

                    downloadCompleteHandler.sendEmptyMessage(0);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

    }

}
