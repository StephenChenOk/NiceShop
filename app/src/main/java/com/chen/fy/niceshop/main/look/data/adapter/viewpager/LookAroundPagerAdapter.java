package com.chen.fy.niceshop.main.look.data.adapter.viewpager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.chen.fy.niceshop.main.look.view.fragment.LookAroundFragment;
import com.chen.fy.niceshop.main.look.view.fragment.LookAroundItemFragment;

public class LookAroundPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGER_COUNT = 5;

    private LookAroundItemFragment allFragment;
    private LookAroundItemFragment jjFragment;
    private LookAroundItemFragment czFragment;
    private LookAroundItemFragment cdFragment;
    private LookAroundItemFragment msFragment;

    public LookAroundPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        allFragment = new LookAroundItemFragment();
        jjFragment = new LookAroundItemFragment();
        czFragment = new LookAroundItemFragment();
        cdFragment = new LookAroundItemFragment();
        msFragment = new LookAroundItemFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case LookAroundFragment.PAGER_ONE:
                allFragment.fillData(null);
                fragment = allFragment;
                break;
            case LookAroundFragment.PAGER_TWO:
                msFragment.fillData("美食");
                fragment = msFragment;
                break;
            case LookAroundFragment.PAGER_THREE:
                czFragment.fillData("彩妆");
                fragment = czFragment;
                break;
            case LookAroundFragment.PAGER_FOUR:
                cdFragment.fillData("穿搭");
                fragment = cdFragment;
                break;
            case LookAroundFragment.PAGER_FIVE:
                jjFragment.fillData("家具");
                fragment = jjFragment;
                break;
            default:
                allFragment.fillData(null);
                fragment = allFragment;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
