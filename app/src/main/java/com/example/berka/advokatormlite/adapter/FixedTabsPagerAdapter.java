package com.example.berka.advokatormlite.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.berka.advokatormlite.fragments.FragmentSaProcentima;
import com.example.berka.advokatormlite.fragments.FragmentSaSpinerom;


/**
 * Created by berka on 19-Jan-18.
 */

public class FixedTabsPagerAdapter extends FragmentPagerAdapter {

    int numOfTabs;

    public FixedTabsPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentSaProcentima fsp = new FragmentSaProcentima();
                return fsp;
            case 1:
                FragmentSaSpinerom fss = new FragmentSaSpinerom();
                return fss;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
