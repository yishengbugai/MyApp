package com.example.testactivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

            String title[]={"最新","分类","电脑壁纸","横图分类"};
    public PagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new FirstFragment();
        }
         else if(position==1){
            return new SecondFragment();
        }
        else if(position==2){
            return new ThirdFragment();
        }else {
            return new ForthFragment();
        }
    }

    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return 4;
    }
}
