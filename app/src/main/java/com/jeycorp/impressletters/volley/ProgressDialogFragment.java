package com.jeycorp.impressletters.volley;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.jeycorp.impressletters.R;


public class ProgressDialogFragment extends DialogFragment {
	private OnCancelListener onProgressCancelListener;	
	private ImageView progressBar;
	
	public ProgressDialogFragment() {
		// Required empty public constructor
	}
	
	public void setOnCancelListener(OnCancelListener onCancelListener) {
		setCancelable(true);
		onProgressCancelListener = onCancelListener;
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Dialog);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_progress_dialog, container,	false);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getDialog().setCanceledOnTouchOutside(false);
		


		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
		animation.setRepeatCount(Animation.INFINITE);
		progressBar = (ImageView) v.findViewById(R.id.progressBars);
		progressBar.startAnimation(animation);



		/*
		progressBar = (ImageView) v.findViewById(R.id.progressBars);
		progressBar.setBackgroundResource(R.drawable.loading);
		AnimationDrawable animation = (AnimationDrawable) progressBar.getBackground();
		animation.start();
		*/

		
		return v;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onCancel(dialog);
		onProgressCancelListener.onCancel(dialog);
		progressBar.clearAnimation();
	}


}
