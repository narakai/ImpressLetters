package com.jeycorp.impressletters.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.jeycorp.impressletters.utill.AppImgResize;
import com.jeycorp.impressletters.volleyimage.NetworkGifImageView;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.activity.GoodsDetailActivity;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.type.GoodsBoard;
import com.jeycorp.impressletters.volley.VolleyQueue;
import com.jeycorp.impressletters.volleyimage.NetworkImageViewGrid;

import java.util.List;

public class GoodsImagePagerAdapter extends PagerAdapter {
    private Context context;
    LayoutInflater mInflater;
    private final int MENUSIZE = 2;
    private List<GoodsBoard> boards;
    private ImageLoader mImageLoader;
    private GoodsDetailActivity activity;
    private RequestManager mGlideRequestManager;

    public GoodsImagePagerAdapter(Context c, List<GoodsBoard> boards, GoodsDetailActivity activity , RequestManager mGlideRequestManager) {
        super();

        context = c;
        mInflater = LayoutInflater.from(c);

        this.boards = boards;
        this.activity = activity;
        this.mGlideRequestManager = mGlideRequestManager;

        mImageLoader = VolleyQueue.getImageLoader();
    }
    public GoodsImagePagerAdapter(){}
    @Override
    public int getCount() {
        int size = boards == null ? 0 : boards.size();
        return size;
    }
    @Override
    public Object instantiateItem(View pager, int position) {
        //position %=MENUSIZE;

        final int pos = position;

        View convertView = mInflater.inflate(R.layout.adapter_goods_image_pager, null);
        ViewHolder holder = new ViewHolder();
        holder.views = (FrameLayout)convertView.findViewById(R.id.views);
        holder.parentView = (LinearLayout)convertView.findViewById(R.id.parentView);
        holder.imgPicture = (NetworkImageViewGrid)convertView.findViewById(R.id.imgPicture);
        holder.imgPictureGif = (NetworkGifImageView)convertView.findViewById(R.id.imgPictureGif);
        holder.txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
        holder.txtTitle2 = (TextView)convertView.findViewById(R.id.txtTitle2);
        holder.txtReadCount = (TextView)convertView.findViewById(R.id.txtReadCount);
        holder.bgMovie = (ImageView)convertView.findViewById(R.id.bgMovie);
        holder.imgNew = (ImageView)convertView.findViewById(R.id.imgNew);
        holder.txtSubTitle = convertView.findViewById(R.id.txtSubTitle);

        holder.txtLike = (TextView)convertView.findViewById(R.id.txtLike);
        holder.txtComment = (TextView)convertView.findViewById(R.id.txtComment);
        holder.txtShare = (TextView)convertView.findViewById(R.id.txtShare);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;

        AppImgResize appImgResize = new AppImgResize(activity, deviceWidth,(int)(deviceWidth/2.5f));
        //AppImgResize appImgResize = new AppImgResize(activity,510,440);


        final GoodsBoard item = boards.get(pos);

        holder.bgMovie.setVisibility(View.GONE);
        holder.imgNew.setVisibility(View.GONE);


        if(item.getType().equals(ValueDefine.GOOD_TYPE_MOVIE)){
            holder.bgMovie.setVisibility(View.VISIBLE);
        }

        if(item.getImgBannerUrl().contains(".gif")){
//            appImgResize.resize(holder.imgPictureGif);
            holder.imgPictureGif.setVisibility(View.VISIBLE);
            holder.imgPicture.setVisibility(View.GONE);
            holder.imgPictureGif.setType("LIST");

            final GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(holder.imgPictureGif);
            //final RequestManager glide = Glide.with(activity);
            holder.imgPictureGif.post(new Runnable() {
                @Override
                public void run() {
                    mGlideRequestManager .load(UrlDefine.DATA+item.getImgBannerUrl()).into(imageViewTarget);
                }
            });


            // holder.imgPictureGif.setImageUrl(UrlDefine.SERVER+item.getImgBannerUrl());
            //holder.imgPictureGif.startAnimation();
        }else{
//            appImgResize.resize(holder.imgPicture);
            holder.imgPictureGif.setVisibility(View.GONE);
            holder.imgPicture.setVisibility(View.VISIBLE);
            holder.imgPicture.setImageUrl(UrlDefine.DATA+item.getImgBannerUrl(),mImageLoader);
        }


        holder.txtTitle.setText(item.getTitle());
        if(holder.txtTitle.getText().toString().length()==13){
            holder.txtTitle2.setVisibility(View.VISIBLE);
        }else {
            holder.txtTitle2.setVisibility(View.INVISIBLE);
        }



        String[] subTitle= Html.fromHtml(item.getContents()).toString().split("<h3 style=\\\"color:#aaaaaa; font-style:italic\\\">");
        holder.txtSubTitle.setText(subTitle[0].substring(0,20)+"...");
        holder.txtReadCount.setText(String.valueOf(item.getReadCount()));
        holder.txtLike.setText(String.valueOf(item.getLikeCount()));
        holder.txtComment.setText(String.valueOf(item.getCommentCount()));
        holder.txtShare.setText(String.valueOf(item.getShareCount()));

        holder.views.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("seq",item.getSeq());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(intent);

            }
        });

        ((ViewPager) pager).addView(convertView, 0);

        return convertView;
    }

    @Override
    public void destroyItem(View pager, int position, Object view) {

        Glide.clear((View) view);
        Glide.get(activity).clearMemory();
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


    class ViewHolder{
        FrameLayout views;
        LinearLayout parentView;
        NetworkImageViewGrid imgPicture;
        NetworkGifImageView imgPictureGif;
        TextView txtTitle,txtTitle2,txtReadCount,txtSubTitle;
        ImageView bgMovie,imgNew;
        TextView txtLike,txtComment,txtShare;
    }


}
