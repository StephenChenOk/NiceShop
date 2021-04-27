package com.chen.fy.niceshop.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.chen.fy.niceshop.R;
import com.chen.fy.niceshop.main.goodprice.GoodPriceFragment;
import com.chen.fy.niceshop.main.home.HomeFragment;
import com.chen.fy.niceshop.main.look.view.fragment.LookAroundFragment;
import com.chen.fy.niceshop.main.user.MineFragment;
import com.chen.fy.niceshop.utils.ShowUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CODE = 1;

    private HomeFragment homeFragment;
    private GoodPriceFragment goodPriceFragment;
    private LookAroundFragment lookAroundFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //将状态栏字体变为黑色
        ShowUtils.changeStatusBarTextImgColor(this, true);
        applyPermission();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("chenchen","onResume");
    }

    private void initView() {
        // 底部导航栏
        RadioGroup radioGroup = findViewById(R.id.rg_main);
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
        // 首先显示home
        transaction.show(homeFragment).commit();
    }

    private void initFragment(FragmentTransaction transaction) {
        // 初始化fragment
        homeFragment = new HomeFragment();
        goodPriceFragment = new GoodPriceFragment();
        lookAroundFragment = new LookAroundFragment();
        mineFragment = new MineFragment();

        // 添加进事务
        transaction.add(R.id.fragment_main, homeFragment);
        transaction.add(R.id.fragment_main, goodPriceFragment);
        transaction.add(R.id.fragment_main, lookAroundFragment);
        transaction.add(R.id.fragment_main, mineFragment);
    }

    /**
     * 使用replace时每次都会销毁并重建Fragment，而使用show、hide则避免了此问题
     * 隐藏所有的Fragment
     */
    private void hideFragment(FragmentTransaction transaction) {
        transaction.hide(homeFragment);
        transaction.hide(goodPriceFragment);
        transaction.hide(lookAroundFragment);
        transaction.hide(mineFragment);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.rb_home:
                hideFragment(transaction);
                transaction.show(homeFragment).commit();
                break;
            case R.id.rb_good_price:
                hideFragment(transaction);
                transaction.show(goodPriceFragment).commit();
                break;
            case R.id.rb_look_around:
                hideFragment(transaction);
                transaction.show(lookAroundFragment).commit();
                break;
            case R.id.rb_mine:
                hideFragment(transaction);
                transaction.show(mineFragment).commit();
                break;
        }
    }

    /**
     * 动态申请危险权限
     */
    private void applyPermission() {
        //权限集合
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.
                READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "必须同意所有权限才可以使用本程序!", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
}
