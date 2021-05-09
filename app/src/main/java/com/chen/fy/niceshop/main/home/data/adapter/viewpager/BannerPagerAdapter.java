package com.chen.fy.niceshop.main.home.data.adapter.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.chen.fy.niceshop.main.home.view.fragment.RecommendFragment;
import com.chen.fy.niceshop.network.ServiceCreator;
import com.chen.fy.niceshop.utils.ShowUtils;

import java.util.List;

public class BannerPagerAdapter extends PagerAdapter {

    private Context mContext;
    private Handler mHandler;
    private List<String> stringList;
    private List<Integer> integerList;
    private int count;

    public BannerPagerAdapter(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStringData(List<String> list) {
        this.stringList = list;
    }

    public void setIntegerData(List<Integer> list) {
        this.integerList = list;
    }

    /**
     * @return 滑动总次数
     */
    @Override
    public int getCount() {
        return count;
    }

    /**
     * 比较两个页面是否为同一个
     *
     * @param view 当前页面
     * @param o    instantiateItem方法返回的页面
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    /**
     * 初始化页面
     * @param container viewPager自身
     * @param position  当前实例化页面位置
     * @return 返回初始化好的页面
     */
    @NonNull
    @Override
    @SuppressLint("ClickableViewAccessibility")
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Object image_path = 0;
        if (stringList != null) {
            image_path = ServiceCreator.ROOT_URL + stringList.get(position % stringList.size());
        }
        if (integerList != null) {
            image_path = integerList.get(position % integerList.size());
        }
        Glide.with(mContext).load(image_path).into(imageView);

        //设置触摸事件,当用户点击了页面时,页面不再自己往后动,应该做停留
        imageView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHandler.removeCallbacksAndMessages(null);
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    mHandler.removeCallbacksAndMessages(null);
                    // 重新发送延迟消息，3秒后滑动
                    mHandler.sendEmptyMessageDelayed(
                            RecommendFragment.BANNER_SLIDE, RecommendFragment.slideInterval);
                    break;
            }
            // 不消化事件，否则onClick无法得到执行
            return false;
        });

        Object finalImage_path = image_path;
        imageView.setOnClickListener(v -> {
            ShowUtils.zoomPicture(mContext,v, finalImage_path);
        });

        //把初始化好的页面加入到viewPager中
        container.addView(imageView);
        return imageView;
    }

    /**
     * 释放资源
     *
     * @param container viewpager本身
     * @param position  要释放的位置
     * @param object    要释放的页面
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    }
}
