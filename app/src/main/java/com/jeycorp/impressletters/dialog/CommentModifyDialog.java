package com.jeycorp.impressletters.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.jeycorp.impressletters.type.GoodsComment;
import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.activity.CommentListActivity;
import com.jeycorp.impressletters.activity.GoodsDetailActivity;

public class CommentModifyDialog extends Dialog{
    private Activity activity;
    private GoodsComment item;

    public CommentModifyDialog(Context context, Activity activity, GoodsComment item){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.activity = activity;
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_comment_modify);
        setInitView();

    }
    private void setInitView(){
        findViewById(R.id.btnModify).setOnClickListener(menuClick);
        findViewById(R.id.btnDelete).setOnClickListener(menuClick);
        findViewById(R.id.linearBg).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CommentModifyDialog.this.dismiss();
                return true;
            }
        });
    }

    View.OnClickListener menuClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnModify:
                    if(activity instanceof GoodsDetailActivity){
                        ((GoodsDetailActivity)activity).moveActivity(item);
                    }
                    if(activity instanceof CommentListActivity){
                        ((CommentListActivity)activity).moveActivity(item);
                    }
                    break;
                case R.id.btnDelete:
                    if(activity instanceof GoodsDetailActivity){
                        ((GoodsDetailActivity)activity).deleteCommentUrl(item);
                    }
                    if(activity instanceof CommentListActivity){
                        ((CommentListActivity)activity).deleteCommentUrl(item);
                    }
                    break;
            }
            CommentModifyDialog.this.dismiss();

        }
    };
}
