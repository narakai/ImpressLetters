package com.jeycorp.impressletters.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.android.volley.toolbox.ImageLoader;
import com.jeycorp.impressletters.activity.FinishPopupActivity;
import com.jeycorp.impressletters.define.LinkType;
import com.jeycorp.impressletters.volley.VolleyQueue;
import com.jeycorp.impressletters.volleyimage.NetworkImageViewGrid;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.type.Finish;


import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {
	private Context context;
	LayoutInflater mInflater;
	private final int MENUSIZE = 8;
	private List<Finish> boards;
	private ImageLoader mImageLoader;
	private FinishPopupActivity activity;
	
	public ImagePagerAdapter(Context c, List<Finish> boards, FinishPopupActivity activity ) {
		super();
	
		context = c;
		mInflater = LayoutInflater.from(c);
		
		this.boards = boards;
		this.activity = activity;
		
		mImageLoader = VolleyQueue.getImageLoader();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return boards.size();
	}
	@Override
	public Object instantiateItem(View pager, int position) {
		position %=MENUSIZE;
		final int pos = position;
		
		View view = mInflater.inflate(R.layout.pop_image, null);
		
		NetworkImageViewGrid imgPicture = (NetworkImageViewGrid) view.findViewById(R.id.img_picture);
		ImageLoader imageLoader = VolleyQueue.getImageLoader();
		imgPicture.setImageUrl(UrlDefine.API_POPUP_URL+boards.get(position).getImgUrl(), imageLoader);
		
//		ImageView imgPicture = (ImageView) view.findViewById(R.id.img_picture);		
//		ImageLoader imageLoader = new ImageLoader(Volley.newRequestQueue(context), new BitmapLruCache(1024));
//	    imageLoader.get(UrlDefine.SERVER+boards.get(position).getImgUrl(),  imageLoader.getImageListener(imgPicture, Color.TRANSPARENT, Color.TRANSPARENT));
	    
		

		imgPicture.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String type = boards.get(pos).getLinkType();
				
				if (type.equals(LinkType.APP)) {
					Intent intent = activity.getIntent();
					intent.putExtra("STATE", boards.get(pos).getLinkUrl());
					activity.setResult(activity.RESULT_OK, intent);
					activity.finish();
				}
				else if (type.equals(LinkType.WEB)) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(boards.get(pos).getLinkUrl()));
					context.startActivity(intent);
				}
				
			}
		});

		((ViewPager) pager).addView(view, 0);

		return view;
	}

	@Override
	public void destroyItem(View pager, int position, Object view) {
		((ViewPager) pager).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View pager, Object obj) {
		return pager == obj;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public void finishUpdate(View arg0) {
	}



}
