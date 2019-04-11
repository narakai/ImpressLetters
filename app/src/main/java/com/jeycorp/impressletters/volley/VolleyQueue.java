package com.jeycorp.impressletters.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyQueue {

    //private static final int MAX_IMAGE_CACHE_ENTIRES  = 200;

    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private VolleyQueue() {}

    public static int getDefaultLruCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(getDefaultLruCacheSize()));
        //mImageLoader = new ImageLoader(mRequestQueue, new com.jeycorp.volley.BitmapLruCache(MAX_IMAGE_CACHE_ENTIRES));
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }
}
