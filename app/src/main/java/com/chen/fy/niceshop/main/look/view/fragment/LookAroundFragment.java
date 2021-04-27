package com.chen.fy.niceshop.main.look.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.look.data.adapter.viewpager.LookAroundPagerAdapter;
import com.chen.fy.niceshop.main.look.publish.PublishActivity;
import com.chen.fy.niceshop.utils.ShowUtils;
import com.chen.fy.niceshop.main.search.SearchActivity;

import java.util.Objects;

/**
 * 逛逛
 */
public class LookAroundFragment extends Fragment implements ViewPager.OnPageChangeListener {

    public static final int PAGER_ONE = 0;
    public static final int PAGER_TWO = 1;
    public static final int PAGER_THREE = 2;
    public static final int PAGER_FOUR = 3;
    public static final int PAGER_FIVE = 4;

    private View mView;

    private ViewPager mViewPager;

    private RadioButton rbAll;
    private RadioButton rbJj;
    private RadioButton rbCz;
    private RadioButton rbCd;
    private RadioButton rbMs;

    private LookAroundPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.look_around_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        initData();
    }

    /**
     * Fragment在show与hide状态转换时调用此方法
     *
     * @param hidden 是否是hide状态
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && getActivity() != null) {
            ShowUtils.changeStatusBarTextImgColor(getActivity(), true);

            configViewPager();
        }
    }

    private void bindView() {
        setClick();
    }

    private void setClick() {
        mView.findViewById(R.id.iv_search).setOnClickListener(v -> {
            SearchActivity.start(getContext());
        });
        mView.findViewById(R.id.iv_share).setOnClickListener(v -> {
            PublishActivity.start(getContext());
        });
    }

    private void initData() {
        configViewPager();
        configRadioGroup();
    }

    private void configViewPager() {
        mViewPager = mView.findViewById(R.id.vp_look_around);
        mAdapter = new LookAroundPagerAdapter(
                Objects.requireNonNull(getChildFragmentManager()));
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(this);
    }

    private void configRadioGroup() {
        RadioGroup radioGroup = mView.findViewById(R.id.rg_look_around);
        rbAll = mView.findViewById(R.id.rb_all_look_around);
        rbMs = mView.findViewById(R.id.rb_ms_look_around);
        rbJj = mView.findViewById(R.id.rb_jj_look_around);
        rbCz = mView.findViewById(R.id.rb_cz_look_around);
        rbCd = mView.findViewById(R.id.rb_cd_look_around);
        radioGroup.setOnCheckedChangeListener((group, id) -> {
            clickRadioBtn(id);
        });
    }

    private void clickRadioBtn(int checkedId) {
        switch (checkedId) {
            case R.id.rb_all_look_around: {
                mViewPager.setCurrentItem(PAGER_ONE);
            }
            break;
            case R.id.rb_ms_look_around: {
                mViewPager.setCurrentItem(PAGER_TWO);
            }
            break;
            case R.id.rb_cz_look_around: {
                mViewPager.setCurrentItem(PAGER_THREE);
            }
            break;
            case R.id.rb_cd_look_around: {
                mViewPager.setCurrentItem(PAGER_FOUR);
            }
            break;
            case R.id.rb_jj_look_around: {
                mViewPager.setCurrentItem(PAGER_FIVE);
            }
            break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // i 表示界面当前的状态,0表示什么都没做,1正在滑动,2滑动完毕
        if (state == 2) {
            switch (mViewPager.getCurrentItem()) {
                case PAGER_ONE:
                    rbAll.setChecked(true);
                    break;
                case PAGER_TWO:
                    rbMs.setChecked(true);
                    break;
                case PAGER_THREE:
                    rbCz.setChecked(true);
                    break;
                case PAGER_FOUR:
                    rbCd.setChecked(true);
                    break;
                case PAGER_FIVE:
                    rbJj.setChecked(true);
                    break;
            }
        }
    }
}


