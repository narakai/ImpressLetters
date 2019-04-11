package com.jeycorp.impressletters.utill;

import android.content.Context;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.jeycorp.impressletters.GlobalApplication;


public class Analytics {

	public Context context;

	public Analytics(Context context) {
		this.context = context;
	}
	//화면 수집
	public void getOutputEvent(String name){
        Tracker t = ((GlobalApplication) context).getTracker(
                GlobalApplication.TrackerName.APP_TRACKER);
        t.setScreenName(name);
        t.send(new HitBuilders.AppViewBuilder().build());
	}
	//클릭 수집
	public void getClickEvent(String categoryId, String actionId, String labelId){
        Tracker t = ((GlobalApplication) context).getTracker(
                GlobalApplication.TrackerName.APP_TRACKER);
        t.send(new HitBuilders.EventBuilder().setCategory(categoryId)
                .setAction(actionId).setLabel(labelId).build());
	}

}
