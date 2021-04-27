package com.chen.fy.niceshop.main.home.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.classify.view.fragment.BabyProductsFragment;
import com.chen.fy.niceshop.main.classify.view.fragment.ComputerFragment;
import com.chen.fy.niceshop.main.classify.view.fragment.DailyUseFragment;
import com.chen.fy.niceshop.main.classify.view.fragment.EquipmentFragment;
import com.chen.fy.niceshop.main.classify.view.fragment.FoodFragment;
import com.chen.fy.niceshop.main.classify.view.fragment.OutdoorSportFragment;
import com.chen.fy.niceshop.main.classify.view.fragment.RecommendClassifyFragment;
import com.chen.fy.niceshop.main.classify.view.fragment.ShoesFragment;

/**
 * 分类导航
 */
public class ClassifyNavigationActivity extends AppCompatActivity implements View.OnClickListener {

    private RecommendClassifyFragment recommendClassifyFragment;
    private ComputerFragment computerFragment;
    private ShoesFragment shoesFragment;
    private FoodFragment foodFragment;
    private EquipmentFragment equipmentFragment;
    private OutdoorSportFragment outdoorSportFragment;
    private DailyUseFragment dailyUseFragment;
    private BabyProductsFragment babyProductsFragment;

    public static void start(Context context) {
        Intent intent = new Intent(context, ClassifyNavigationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.classify_navigation);
        bindView();
    }

    private void bindView() {
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        // 底部导航栏
        RadioGroup radioGroup = findViewById(R.id.rg_classify);
        radioGroup.setOnCheckedChangeListener((group, id) -> {
            //底部导航栏按钮选中事件
            RadioButton radioButton = group.findViewById(id);
            radioButton.setOnClickListener(this);
        });

        // 开启事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 初始化fragment
        initFragment(transaction);
        // 隐藏所有fragment
        hideFragment(transaction);
        // 首先显示电脑数码
        transaction.show(computerFragment).commit();
    }

    private void initFragment(FragmentTransaction transaction) {
        // 初始化fragment
        recommendClassifyFragment = new RecommendClassifyFragment();
        computerFragment = new ComputerFragment();
        shoesFragment = new ShoesFragment();
        foodFragment = new FoodFragment();
        equipmentFragment = new EquipmentFragment();
        outdoorSportFragment = new OutdoorSportFragment();
        dailyUseFragment = new DailyUseFragment();
        babyProductsFragment = new BabyProductsFragment();

        // 添加进事务
        transaction.add(R.id.fl_classify, recommendClassifyFragment);
        transaction.add(R.id.fl_classify, computerFragment);
        transaction.add(R.id.fl_classify, shoesFragment);
        transaction.add(R.id.fl_classify, foodFragment);
        transaction.add(R.id.fl_classify, equipmentFragment);
        transaction.add(R.id.fl_classify, outdoorSportFragment);
        transaction.add(R.id.fl_classify, dailyUseFragment);
        transaction.add(R.id.fl_classify, babyProductsFragment);
    }

    /**
     * 使用replace时每次都会销毁并重建Fragment，而使用show、hide则避免了此问题
     * 隐藏所有的Fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        transaction.hide(recommendClassifyFragment);
        transaction.hide(computerFragment);
        transaction.hide(shoesFragment);
        transaction.hide(foodFragment);
        transaction.hide(equipmentFragment);
        transaction.hide(outdoorSportFragment);
        transaction.hide(dailyUseFragment);
        transaction.hide(babyProductsFragment);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.rb_recommend_classify:
                hideFragment(transaction);
                transaction.show(recommendClassifyFragment).commit();
                break;
            case R.id.rb_computer:
                hideFragment(transaction);
                transaction.show(computerFragment).commit();
                break;
            case R.id.rb_shoes:
                hideFragment(transaction);
                transaction.show(shoesFragment).commit();
                break;
            case R.id.rb_food:
                hideFragment(transaction);
                transaction.show(foodFragment).commit();
                break;
            case R.id.rb_equipment:
                hideFragment(transaction);
                transaction.show(equipmentFragment).commit();
                break;
            case R.id.rb_outdoor_sport:
                hideFragment(transaction);
                transaction.show(outdoorSportFragment).commit();
                break;
            case R.id.rb_daily_use:
                hideFragment(transaction);
                transaction.show(dailyUseFragment).commit();
                break;
            case R.id.rb_baby_products:
                hideFragment(transaction);
                transaction.show(babyProductsFragment).commit();
                break;
        }
    }

}
