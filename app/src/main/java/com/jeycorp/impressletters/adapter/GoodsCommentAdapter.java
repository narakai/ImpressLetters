package com.jeycorp.impressletters.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.jeycorp.impressletters.activity.CommentListActivity;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.define.ValueDefine;
import com.jeycorp.impressletters.dialog.CommentModifyDialog;
import com.jeycorp.impressletters.type.GoodsComment;
import com.jeycorp.impressletters.utill.PreferenceManagers;
import com.jeycorp.impressletters.utill.TimeUtil;
import com.jeycorp.impressletters.volley.VolleyQueue;
import com.jeycorp.impressletters.volleyimage.ImageDetail;
import com.jeycorp.impressletters.volleyimage.NetworkImageViewRound;
import com.jeycorp.impressletters.R;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by cha on 2016-11-15.
 */
public class GoodsCommentAdapter extends ArrayAdapter<GoodsComment>{
    private static final int resource = R.layout.adapter_goods_comment;
    private Activity activity;
    private List<GoodsComment> list;
    private ImageLoader mImageLoader;
    private Context context;
    private PreferenceManagers pref;
    private TimeUtil timeUtil;
    private String adapterType;

    public GoodsCommentAdapter(Context context,List<GoodsComment> list,String adapterType){
        super(context,resource,list);
        this.list = list;
        this.context = context;
        this.adapterType = adapterType;
        timeUtil = new TimeUtil(activity);
    }
    public void setActivity(Activity activity){
        this.activity = activity;
        pref = new PreferenceManagers(activity);
        mImageLoader = VolleyQueue.getImageLoader();
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
            convertView = vi.inflate(R.layout.adapter_goods_comment,parent,false);

            ViewHoler holder = new ViewHoler();
            holder.parentView = (LinearLayout)convertView.findViewById(R.id.parentView);
            holder.imgProfile = (NetworkImageViewRound)convertView.findViewById(R.id.imgProfile);
            holder.txtNickname = (TextView)convertView.findViewById(R.id.txtNickname);
            holder.txtContents = (TextView)convertView.findViewById(R.id.txtContents);
            holder.txtDate = (TextView)convertView.findViewById(R.id.txtDate);
            holder.btnModify = (ImageButton)convertView.findViewById(R.id.btnModify);

            holder.viewLine = (LinearLayout)convertView.findViewById(R.id.viewLine);

            convertView.setTag(holder);
        }

        ViewHoler holder = (ViewHoler)convertView.getTag();
        final GoodsComment item = getItem(position);

        String nickname = "GUEST";
        holder.btnModify.setVisibility(View.INVISIBLE);
        if(!item.getUserUid().equals("")){
            if(item.getNickname()!=null && !item.getNickname().equals("")){
                nickname = item.getNickname();
                if(item.getUserUid().equals(pref.getUid())){
                    holder.btnModify.setVisibility(View.VISIBLE);
                }
            }else{
                nickname = "닉네임없음";
            }

        }


        Timestamp date = Timestamp.valueOf(item.getCreateDate());
        long regTime = date.getTime();
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - regTime) / 1000;
        String time =timeUtil.getFormatTime(diffTime);

        holder.imgProfile.setDefaultImageResId(R.drawable.menu_04_option_05_01);
        holder.imgProfile.setImageUrl(UrlDefine.SERVER+item.getImgThumbUrl(),mImageLoader);
        holder.txtNickname.setText(nickname);
        holder.txtDate.setText(time);
        holder.txtContents.setText(item.getContents());

        holder.viewLine.setVisibility(View.VISIBLE);
        if(list.size()-1==position){
            holder.viewLine.setVisibility(View.INVISIBLE);
        }

        holder.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getUserUid().equals(pref.getUid()) && !item.getUserUid().equals("")) {
                    new CommentModifyDialog(activity, activity, item).show();
                }
            }
        });
        holder.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getImgUrl()!=null && !item.getImgUrl().equals("")){
                    Intent imageIntent = new Intent(activity,ImageDetail.class);
                    imageIntent.putExtra("url",UrlDefine.SERVER+item.getImgUrl());
                    imageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(imageIntent);
                }
            }
        });
        if(adapterType.equals(ValueDefine.ADAPTER_LIST)){
            if(list.size()<500){
                if(position==list.size()-10){
                    if(list.size()%20==0){
                        if(activity instanceof CommentListActivity){
                            ((CommentListActivity)activity).viewProgressLoading(View.VISIBLE);
                            ((CommentListActivity)activity).getGoodsUrl();
                        }
                    }
                }
            }
        }


        return convertView;
    }

    class ViewHoler{
        LinearLayout parentView,viewLine;
        NetworkImageViewRound imgProfile;
        TextView txtNickname,txtContents,txtDate;
        ImageButton btnModify;

    }
}
