package com.example.liuan.screenview.adapter;

import android.database.DataSetObserver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.liuan.screenview.BaseFragment;

import java.util.List;


public class FragmentViewPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> fragments;
    private List<String> titles;

    public FragmentViewPagerAdapter(FragmentManager manager, List<BaseFragment> fragments, List<String> titles) {
        super(manager);
        this.fragments = fragments;
        this.titles = titles;

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer!=null) {
            super.unregisterDataSetObserver(observer);
        }
    }
}
