package com.myxh.coolshopping.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.myxh.coolshopping.R;
import com.myxh.coolshopping.common.LocationService;
import com.myxh.coolshopping.ui.base.BaseActivity;
import com.myxh.coolshopping.util.SharePreferenceUtil;

/**
 * Created by asus on 2016/8/28.
 */
public class SplashActivity extends BaseActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //开启定位服务
        startService(new Intent(this, LocationService.class));
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CALENDAR);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            String[] permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            ActivityCompat.requestPermissions(this, permissions, 100);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirst = SharePreferenceUtil.getBoolean(SplashActivity.this,"isFirst",true);
                if (isFirst) {
                    SharePreferenceUtil.putBoolean(SplashActivity.this,"isFirst",false);
                    openActivity(GuideActivity.class);
                } else {
                    openActivity(MainActivity.class);
                }
                finish();
            }
        },100);
    }

}
