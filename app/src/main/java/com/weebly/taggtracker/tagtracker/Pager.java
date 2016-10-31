package com.weebly.taggtracker.tagtracker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barbara on 17/10/2016.
 *
 * Serve para trocar as páginas da tab
 */

public class Pager extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();
    private int numItens = 2;

    public Pager(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        addFragment(fragment, title, true);
    }

    public void addFragment(Fragment fragment, String title, boolean hasOptionsMenu) {
        fragment.setHasOptionsMenu(hasOptionsMenu);
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return numItens;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles.get(position);
    }
}
