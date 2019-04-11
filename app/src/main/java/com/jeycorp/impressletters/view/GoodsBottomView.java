package com.jeycorp.impressletters.view;


import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeycorp.impressletters.activity.GoodsDetailActivity;
import com.jeycorp.impressletters.type.GoodsBoard;
import com.jeycorp.impressletters.utill.Analytics;
import com.jeycorp.impressletters.utill.SoftKeyboardDectectorView;
import com.jeycorp.impressletters.R;

public class GoodsBottomView {
    private GoodsDetailActivity activity;
    private int commentCount = 0;
    private int likeCount = 0;
    private int shareCount = 0;
    private SoftKeyboardDectectorView softKeyboardDecector;


    public GoodsBottomView(GoodsDetailActivity activity){
        this.activity = activity;

        activity.findViewById(R.id.btnLike).setOnClickListener(bottomClick);
        activity.findViewById(R.id.btnComment).setOnClickListener(bottomClick);
        activity.findViewById(R.id.btnShare).setOnClickListener(bottomClick);

        setKeyboardEvent();
    }

    public void setBottomView(GoodsBoard goodsBoard){
        TextView txtLike = (TextView)activity.findViewById(R.id.txtLike);
        TextView txtComment = (TextView)activity.findViewById(R.id.txtComment);
        TextView txtShare = (TextView)activity.findViewById(R.id.txtShare);

        String strLike = activity.getResources().getString(R.string.goods_bottom_like);
        String strComment = activity.getResources().getString(R.string.goods_bottom_comment);
        String strShare = activity.getResources().getString(R.string.goods_bottom_share);
        likeCount = goodsBoard.getLikeCount();
        commentCount = goodsBoard.getCommentCount();
        shareCount = goodsBoard.getShareCount();
        txtLike.setText(strLike+" "+String.valueOf(likeCount));
        txtComment.setText(strComment+" "+String.valueOf(commentCount));
        txtShare.setText(strShare+" "+String.valueOf(shareCount));
    }

    private void setLike(){
        activity.setLikeUrl();
    }
    private void setShare(){
        activity.setShareUrl();
    }
    public void setVisibleKeyBoard(){
        activity.addContentView(softKeyboardDecector, new FrameLayout.LayoutParams(-1, -1));

        final EditText editText = (EditText) activity.findViewById(R.id.editComment);
        editText.setHint(Html.fromHtml("<small>" + activity.getString(R.string.goods_comment_hint) + "</small>"));
        editText.requestFocus();

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        activity.findViewById(R.id.bottomMenuView).setVisibility(View.GONE);
        activity.findViewById(R.id.bottomCommentView).setVisibility(View.VISIBLE);

        activity.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(activity,activity.getStr(R.string.goods_comment_empty),Toast.LENGTH_SHORT).show();
                    return;
                }
                activity.setCommentUrl(editText.getText().toString());
                editText.setText("");
                setHideKeyBoard();
            }
        });
    }

    private void setHideKeyBoard(){
        InputMethodManager immhide = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    public void setCommentCountView(){
        TextView txtComment = (TextView)activity.findViewById(R.id.txtComment);
        String strComment = activity.getResources().getString(R.string.goods_bottom_comment);
        txtComment.setText(strComment+" "+String.valueOf(commentCount));
    }
    public void setLikeCountView(){
        TextView txtLike = (TextView)activity.findViewById(R.id.txtLike);
        String strLike = activity.getResources().getString(R.string.goods_bottom_like);
        txtLike.setText(strLike+" "+String.valueOf(likeCount));
    }
    public void setShareCountView(){
        TextView txtShare = (TextView)activity.findViewById(R.id.txtShare);
        String strShare = activity.getResources().getString(R.string.goods_bottom_share);
        txtShare.setText(strShare+" "+String.valueOf(shareCount));

    }
    public int getCommentCount(){
        return commentCount;
    }
    public void setCommentCount(int count){
        commentCount += count;
    }
    public int getLikeCount(){
        return  likeCount;
    }
    public void setLikeCount(int count){
        likeCount +=count;
    }
    public int getShareCount() {
        return shareCount;
    }
    public void setShareCount(int count){
        shareCount +=count;
    }

    private void setKeyboardEvent(){
        softKeyboardDecector = new SoftKeyboardDectectorView(activity);
        softKeyboardDecector.setOnShownKeyboard(new SoftKeyboardDectectorView.OnShownKeyboardListener() {
            @Override
            public void onShowSoftKeyboard() {
                EditText editText = (EditText) activity.findViewById(R.id.editComment);
                editText.setHint(Html.fromHtml("<small>" + activity.getString(R.string.goods_comment_hint) + "</small>"));
                editText.setText("");
                editText.requestFocus();
                activity.findViewById(R.id.bottomMenuView).setVisibility(View.GONE);
                activity.findViewById(R.id.bottomCommentView).setVisibility(View.VISIBLE);
            }
        });

        softKeyboardDecector.setOnHiddenKeyboard(new SoftKeyboardDectectorView.OnHiddenKeyboardListener() {
            @Override
            public void onHiddenSoftKeyboard() {
                ViewGroup parentViewGroup = (ViewGroup) softKeyboardDecector.getParent();
                if (null != parentViewGroup) {
                    parentViewGroup.removeView(softKeyboardDecector);
                }
                activity.findViewById(R.id.bottomMenuView).setVisibility(View.VISIBLE);
                activity.findViewById(R.id.bottomCommentView).setVisibility(View.GONE);

            }
        });
    }
    View.OnClickListener bottomClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnLike:
                    setLike();
                    new Analytics(activity.getApplication()).getClickEvent("부자되는 글   상세화면", "좋아요 클릭 이벤트", "0");
                    break;
                case R.id.btnComment:
                    setVisibleKeyBoard();
                    new Analytics(activity.getApplication()).getClickEvent("부자되는 글   상세화면", "댓글 클릭 이벤트", "0");
                    break;
                case R.id.btnShare:
                    setShare();
                    new Analytics(activity.getApplication()).getClickEvent("부자되는 글   상세화면", "공유 클릭 이벤트", "0");
                    break;
            }
        }
    };
}
