package com.chen.fy.niceshop.main.home;

import android.os.Bundle;
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
import com.chen.fy.niceshop.main.home.data.adapter.viewpager.HomePagerAdapter;
import com.chen.fy.niceshop.main.search.SearchActivity;
import com.chen.fy.niceshop.utils.ShowUtils;

import java.util.Objects;

public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener,
        RadioGroup.OnCheckedChangeListener {

    //代表界面view的常量
    public static final int PAGER_ONE = 0;
    public static final int PAGER_TWO = 1;

    private View mView;
    private ViewPager vpNews;

    private RadioButton rbRecommend;
    private RadioButton rbAttention;

    private HomePagerAdapter adapter;

    /// 第一次进入时显示的界面
    private final int firstPagerIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.home_fragment_layout, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        vpNews = mView.findViewById(R.id.vp_news);
        rbRecommend = mView.findViewById(R.id.rb_recommend);
        rbAttention = mView.findViewById(R.id.rb_attention);

        configViewPager();
        configRadioGroup();
        setClick();
    }

    private void configRadioGroup() {
        RadioGroup radioGroup = mView.findViewById(R.id.rg_news);
        rbRecommend = mView.findViewById(R.id.rb_recommend);
        rbAttention = mView.findViewById(R.id.rb_attention);
        radioGroup.setOnCheckedChangeListener(this);
    }

    private void configViewPager() {
        adapter = new HomePagerAdapter(
                Objects.requireNonNull(getActivity()).getSupportFragmentManager());
        vpNews.setAdapter(adapter);
        vpNews.setCurrentItem(firstPagerIndex);
        vpNews.addOnPageChangeListener(this);
    }

    private void setClick() {
        mView.findViewById(R.id.tv_search).setOnClickListener(v -> SearchActivity.start(getContext()));
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
            ShowUtils.changeStatusBarTextImgColor(getActivity(), false);
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
            switch (vpNews.getCurrentItem()) {
                case PAGER_ONE:
                    rbRecommend.setChecked(true);
                    break;
                case PAGER_TWO:
//                    adapter.refreshAttention();
                    rbAttention.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_recommend: {
                vpNews.setCurrentItem(PAGER_ONE);
            }
            break;
            case R.id.rb_attention: {
                vpNews.setCurrentItem(PAGER_TWO);
            }
            break;
        }
    }
}


