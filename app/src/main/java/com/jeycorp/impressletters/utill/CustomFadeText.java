package com.jeycorp.impressletters.utill;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.jeycorp.impressletters.R;

import believe.cht.fadeintextview.TextViewListener;


class Letter extends CharacterStyle implements UpdateAppearance {

    private float alpha = 0.0f;

    @Override
    public void updateDrawState(TextPaint tp) {
        int color = ((int) (0xFF * alpha) << 24) | (tp.getColor() & 0x00FFFFFF);
        tp.setColor(color);
    }

    public void setAlpha(float alpha) {
        this.alpha = Math.max(Math.min(alpha, 1.0f), 0.0f);
    }

}