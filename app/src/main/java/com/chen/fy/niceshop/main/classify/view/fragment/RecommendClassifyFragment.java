package com.chen.fy.niceshop.main.classify.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.utils.ShowUtils;


/**
 * 推荐
 */
public class RecommendClassifyFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.recommend_classify_fragment, container, false);
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

        initData();
    }

    /**
     * Fragment在show与hide状态转换时调用此方法
     * @param hidden 是否是hide状态
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && getActivity() != null) {
            ShowUtils.changeStatusBarTextImgColor(getActivity(), true);
        }
    }

    //region view
    private void bindView() {
        setListener();
    }

    private void setListener() {

    }
    //endregion

    //region data
    private void initData() {

    }

    private void fillDataList() {

    }
    //endregion
}


