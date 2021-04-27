package com.chen.fy.niceshop.main.home.data.adapter.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chen.fy.niceshop.main.home.view.fragment.AttentionFragment;
import com.chen.fy.niceshop.main.home.view.fragment.RecommendFragment;
import com.chen.fy.niceshop.main.home.HomeFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private static final int PAGER_COUNT = 2;

    private RecommendFragment recommendFragment;
    private AttentionFragment attentionFragment;

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        recommendFragment = new RecommendFragment();
        attentionFragment = new AttentionFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case HomeFragment.PAGER_ONE:
                fragment = recommendFragment;
                break;
            case HomeFragment.PAGER_TWO:
                fragment = attentionFragment;
                break;
            default:
                fragment = recommendFragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

//    public void refreshAttention(){
//        attentionFragment.refresh();
//    }
}
