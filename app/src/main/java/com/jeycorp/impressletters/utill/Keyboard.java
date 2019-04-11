package com.jeycorp.impressletters.utill;


import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Keyboard {
    private Activity activity;


    public Keyboard(Activity activity){
        this.activity = activity;
    }

    public void hide(EditText editText){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
}
