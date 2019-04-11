package com.jeycorp.impressletters.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jeycorp.impressletters.R;
import com.jeycorp.impressletters.fragment.GoodsBestFragment;
import com.jeycorp.impressletters.fragment.GoodsWriteFragment;

import java.util.Locale;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    Context mContext;
    Activity activity;
    GoodsWriteFragment goodsWriteFragment;
    GoodsBestFragment goodsBestFragment;



    public SectionsPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                goodsWriteFragment = new GoodsWriteFragment(mContext);
                return goodsWriteFragment;
            case 1:
                goodsBestFragment = new GoodsBestFragment();
                return goodsBestFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();

        switch (position) {
            case 0:
                return activity.getString(R.string.section_good_write).toUpperCase(l);
            case 1:
                return activity.getString(R.string.section_good_best).toUpperCase(l);
        }
        return null;
    }
    public void setMoveTop(int position){
        if(position==0){
            if(goodsWriteFragment!=null){
                goodsWriteFragment.setMoveTop();
            }
        }else{
            if(goodsBestFragment!=null){
                goodsBestFragment.setMoveTop();
            }
        }


    }





}
