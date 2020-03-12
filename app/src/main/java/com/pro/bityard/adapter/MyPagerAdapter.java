package com.pro.bityard.adapter;

import android.os.Bundle;

import com.pro.bityard.fragment.user.ResetPassFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments;
    private List<String> mTitle;



    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments=new ArrayList<>();
        mTitle=new ArrayList<>();
    }


    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mTitle.add(title);
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }



    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
