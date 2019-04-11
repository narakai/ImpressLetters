package com.jeycorp.impressletters.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.utill.RegisterStatic;

public class AccessDialog extends Dialog{
    private Activity activity;

    public AccessDialog(Context context, Activity activity){
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_access);
        setInitView();

    }
    private void setInitView(){
        findViewById(R.id.btnOk).setOnClickListener(menuClick);
        findViewById(R.id.btnNo).setOnClickListener(menuClick);
        findViewById(R.id.btnAfter).setOnClickListener(menuClick);
        findViewById(R.id.linearBg).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                AccessDialog.this.dismiss();
                return true;
            }
        });
    }

    View.OnClickListener menuClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnOk:
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(RegisterStatic.getUpdateUrl(activity)));
                    activity.startActivity(intent);
                    break;
                case R.id.btnNo:
                    break;
                case R.id.btnAfter:
                    break;
            }
            AccessDialog.this.dismiss();

        }
    };
}
