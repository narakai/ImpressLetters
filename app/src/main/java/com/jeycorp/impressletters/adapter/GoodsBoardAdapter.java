package com.jeycorp.impressletters.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.activity.GoodsDetailActivity;
import com.jeycorp.impressletters.activity.GoodsSearchActivity;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.fragment.GoodsStorageFragment;
import com.jeycorp.impressletters.fragment.GoodsWriteFragment;
import com.jeycorp.impressletters.type.GoodsBoard;
import com.jeycorp.impressletters.utill.AppImgResize;
import com.jeycorp.impressletters.utill.TimeUtil;
import com.jeycorp.impressletters.volley.VolleyQueue;
import com.jeycorp.impressletters.volleyimage.NetworkGifImageView;
import com.jeycorp.impressletters.volleyimage.NetworkImageViewGrid;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GoodsBoardAdapter extends ArrayAdapter<GoodsBoard>{
    private static final int resource = R.layout.adapter_goods_board;
    private Fragment fragment;
    private Activity activity;
    private List<GoodsBoard> list;
    private ImageLoader imageLoader;
    private Context context;
    private String searchStr="";
    private String toDay="";
    private long currentTimeLong;


    public GoodsBoardAdapter(Context context, List<GoodsBoard> list){
        super(context,resource,list);
        this.list = list;
        this.context = context;
        TimeUtil timeUtil = new TimeUtil(getContext());
        toDay = timeUtil.getToDate();

    }
    public void setInit(Fragment fragment,Activity activity){
        this.fragment = fragment;
        this.activity = activity;
        imageLoader = VolleyQueue.getImageLoader();
        currentTimeLong = System.currentTimeMillis();
    }
    public void setInit(Activity activity){
        this.activity = activity;
        imageLoader = VolleyQueue.getImageLoader();
        currentTimeLong = System.currentTimeMillis();
    }
    public void setSearch(String searchStr){
        this.searchStr =searchStr;
    }

    @Override
    public int getCount() {
        int size = list == null ? 0 : list.size();
        return size;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater vi = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.adapter_goods_board,parent,false);

            ViewHolder holder = new ViewHolder();
            holder.parentView = (LinearLayout)convertView.findViewById(R.id.parentView);
            holder.imgPicture = (NetworkImageViewGrid)convertView.findViewById(R.id.imgPicture);
            holder.imgPictureGif = (NetworkGifImageView)convertView.findViewById(R.id.imgPictureGif);
            holder.txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
            holder.txtReadCount = (TextView)convertView.findViewById(R.id.txtReadCount);
            holder.btnLike = (ImageButton)convertView.findViewById(R.id.btnLike);
            holder.btnComment = (ImageButton)convertView.findViewById(R.id.btnComment);
            holder.btnShare = (ImageButton)convertView.findViewById(R.id.btnShare);
            holder.txtLike = (TextView)convertView.findViewById(R.id.txtLike);
            holder.txtComment = (TextView)convertView.findViewById(R.id.txtComment);
            holder.txtShare = (TextView)convertView.findViewById(R.id.txtShare);
            holder.bgMovie = (ImageView)convertView.findViewById(R.id.bgMovie);
            holder.imgNew = (ImageView)convertView.findViewById(R.id.imgNew);

            convertView.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder)convertView.getTag();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;


//        AppImgResize appImgResize = new AppImgResize(this.getContext(), deviceWidth, (int)(deviceWidth/2.5f));
        //AppImgResize appImgResize = new AppImgResize(this.getContext(),510,440);


        final GoodsBoard item = getItem(position);

        holder.bgMovie.setVisibility(View.GONE);
        holder.imgNew.setVisibility(View.GONE);
        if(item.getType().equals(ValueDefine.GOOD_TYPE_MOVIE)){
            holder.bgMovie.setVisibility(View.VISIBLE);
        }else{
            String dateArray[] = item.getCreateDate().split(" ");
/*            if(dateArray[0].equals(toDay)){
                holder.imgNew.setVisibility(View.VISIBLE);
            }*/

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date itemDay = dateFormat.parse(item.getCreateDate(), new ParsePosition(0));
            long itemTime = itemDay.getTime();
            long oneTime = (24 * 60 * 60 * 1000);

            if((currentTimeLong - itemTime) <= oneTime){
                holder.imgNew.setVisibility(View.VISIBLE);
            }
        }

        if(item.getImgBannerUrl().contains(".gif")){
//            appImgResize.resize(holder.imgPictureGif);
            holder.imgPictureGif.setVisibility(View.VISIBLE);
            holder.imgPicture.setVisibility(View.GONE);
            holder.imgPictureGif.setType("LIST");
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(holder.imgPictureGif);
            Glide.with(this.getContext()).load(UrlDefine.DATA+item.getImgBannerUrl()).into(imageViewTarget);
           // holder.imgPictureGif.setImageUrl(UrlDefine.SERVER+item.getImgBannerUrl());
            //holder.imgPictureGif.startAnimation();
        }else{
//            appImgResize.resize(holder.imgPicture);
            holder.imgPictureGif.setVisibility(View.GONE);
            holder.imgPicture.setVisibility(View.VISIBLE);
            holder.imgPicture.setImageUrl(UrlDefine.DATA+item.getImgBannerUrl(),imageLoader);
        }
        if(item.getTitle().contains(searchStr)){
            String coloredString = "<font color=\"#000000\"><b>";
            //String coloredString = "<b>";
            coloredString += searchStr;
            coloredString +="</b></font>";
            String oldSource = item.getTitle();
            int start_index = oldSource.indexOf(searchStr);
            int end_index =  start_index + searchStr.length() -1;
            String splitStringBegin = oldSource.substring(0, start_index);
            String splitStringEnd = oldSource.substring(end_index+1, oldSource.length());
            String newSource = splitStringBegin + coloredString + splitStringEnd;
            holder.txtTitle.setText(Html.fromHtml(newSource));
        }else{
            holder.txtTitle.setText(item.getTitle());
        }


        holder.txtLike.setText(String.valueOf(item.getLikeCount()));
        holder.txtReadCount.setText(String.valueOf(item.getReadCount()));
        holder.txtComment.setText(String.valueOf(item.getCommentCount()));
        holder.txtShare.setText(String.valueOf(item.getShareCount()));
        holder.parentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("seq",item.getSeq());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(fragment!=null){
                    fragment.startActivity(intent);
                }
                if(activity!=null){
                    activity.startActivity(intent);
                }
            }
        });
        /*
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment instanceof GoodsWriteFragment){
                    ((GoodsWriteFragment) fragment).setLikeUrl(item);
                }
                if(fragment instanceof GoodsStorageFragment){
                    ((GoodsStorageFragment) fragment).setLikeUrl(item);
                }
                if(activity instanceof GoodsSearchActivity){
                    ((GoodsSearchActivity) activity).setLikeUrl(item);
                }

            }
        });
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment instanceof GoodsWriteFragment){
                    ((GoodsWriteFragment) fragment).setShareUrl(item, holder.imgPicture.getWidth(), holder.imgPicture.getHeight());
                }
                if(fragment instanceof GoodsStorageFragment){
                    ((GoodsStorageFragment) fragment).setShareUrl(item, holder.imgPicture.getWidth(), holder.imgPicture.getHeight());
                }
                if(activity instanceof GoodsSearchActivity){
                    ((GoodsSearchActivity) activity).setShareUrl(item, holder.imgPicture.getWidth(), holder.imgPicture.getHeight());
                }

            }
        });
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GoodsDetailActivity.class);
                intent.putExtra("seq",item.getSeq());
                intent.putExtra("comment",true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(fragment!=null){
                    fragment.startActivity(intent);
                }
                if(activity!=null){
                    activity.startActivity(intent);
                }
            }
        });
        */

        if(list.size()<500){
            if(position==list.size()-10){
                if(list.size()%20==0){
                    if(fragment instanceof GoodsWriteFragment){
                        ((GoodsWriteFragment) fragment).viewProgressLoading(View.VISIBLE);
                        ((GoodsWriteFragment) fragment).getListUrl();
                    }
                    if(fragment instanceof GoodsStorageFragment){
                        ((GoodsStorageFragment) fragment).viewProgressLoading(View.VISIBLE);
                        ((GoodsStorageFragment) fragment).getListUrl();
                    }
                    if(activity instanceof GoodsSearchActivity){
                        ((GoodsSearchActivity) activity).viewProgressLoading(View.VISIBLE);
                        ((GoodsSearchActivity) activity).getListUrl();
                    }
                }
            }
        }

        return convertView;
    }

    class ViewHolder{
        LinearLayout parentView;
        NetworkImageViewGrid imgPicture;
        NetworkGifImageView imgPictureGif;
        TextView txtTitle,txtReadCount;
        ImageButton btnLike,btnComment,btnShare;
        TextView txtLike,txtComment,txtShare;
        ImageView bgMovie,imgNew;
    }
}
