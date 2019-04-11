package com.jeycorp.impressletters.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.jeycorp.impressletters.volleyimage.NetworkImageViewFull;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.define.UrlDefine;
import com.jeycorp.impressletters.type.Notice;
import com.jeycorp.impressletters.utill.TimeUtil;
import com.jeycorp.impressletters.volley.VolleyQueue;

import java.util.List;


public class NoticeAdapter extends ArrayAdapter<Notice>{
    private ImageLoader mImageLoader;
    private static final int resource = R.layout.adapter_notice;

    public NoticeAdapter(Context context, List<Notice> objects) {
        super(context, resource, objects);
        mImageLoader = VolleyQueue.getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.adapter_notice, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.linearView = (LinearLayout)convertView.findViewById(R.id.liearView);
            holder.linearContents = (LinearLayout)convertView.findViewById(R.id.linearContents);
            holder.txtTitle = (TextView)convertView.findViewById(R.id.txtTitle);
            holder.txtDate = (TextView)convertView.findViewById(R.id.txtDate);
            holder.txtContents = (TextView)convertView.findViewById(R.id.txtContents);
            holder.imgArrow = (ImageView)convertView.findViewById(R.id.imgArrow);
            holder.imgPicture = (NetworkImageViewFull)convertView.findViewById(R.id.imgPicture);


            convertView.setTag(holder);
        }
        final ViewHolder holder = (ViewHolder)convertView.getTag();

        final Notice board = getItem(position);
        holder.txtTitle.setText(board.getTitle());

        TimeUtil timeUtil = new TimeUtil(this.getContext());
        timeUtil.setTimeStampConvertString(board.getCreateDate());

        String date = timeUtil.getMonth()+"월"+timeUtil.getDay()+"일";
        holder.txtDate.setText(date);

        holder.txtContents.setText(board.getContents());
        holder.imgPicture.setImageUrl(UrlDefine.SERVER+board.getImgUrl(), mImageLoader);

        if(board.isShowView()){
            holder.imgArrow.setBackgroundResource(R.drawable.selection_up);
            holder.linearContents.setVisibility(View.VISIBLE);
        }else{
            holder.imgArrow.setBackgroundResource(R.drawable.selection_down);
            holder.linearContents.setVisibility(View.GONE);
        }

        holder.linearView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(board.isShowView()){
                    board.setShowView(false);
                    holder.imgArrow.setBackgroundResource(R.drawable.selection_down);
                    holder.linearContents.setVisibility(View.GONE);
                }
                else{
                    board.setShowView(true);
                    holder.imgArrow.setBackgroundResource(R.drawable.selection_up);
                    holder.linearContents.setVisibility(View.VISIBLE);
                }

            }
        });




        return convertView;
    }
    class ViewHolder{
        NetworkImageViewFull imgPicture;
        LinearLayout linearView,linearContents;
        TextView txtTitle,txtDate,txtContents;
        ImageView imgArrow;
    }
}
