package com.myxh.coolshopping.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.adhoc.adhocsdk.AdhocTracker;
import com.adhoc.config.AdhocConfig;
import com.adhoc.editor.IAdhocCount;
import com.alipay.euler.andfix.patch.PatchManager;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.myxh.coolshopping.R;
//import com.myxh.coolshopping.util.ToastUtil;
import com.squareup.leakcanary.LeakCanary;
import com.uuzuche.lib_zxing.ZApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import c.b.BP;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by asus on 2016/8/30.
 */
public class CoolApplication extends ZApplication {

    // 此为支付插件的官方最新版本号,请在更新时留意更新说明
    private static final int PLUGINVERSION = 7;
    private static CoolApplication appContext;
    private boolean flag = true;
    private List<BDLocation> mLocations = new ArrayList<>();
    private PatchManager mPatchManager;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

//        //NoHttp初始化
//        NoHttp.initialize(this);
        //Fresco初始化
        try {
            Fresco.initialize(this);
            SDKInitializer.initialize(this);
            Bmob.initialize(this, AppConstant.BMOB_AppID);
            //百度地图初始化
            //Bmob初始化
            //热更新
            //Bmob支付初始化
            initPay();

            //Bmob检查更新
            if (flag) {
                flag = false;
                BmobUpdateAgent.initAppVersion();
            }
        } catch (Throwable e) {

        }
        LeakCanary.install(this);

//        PatchManager patchManager = new PatchManager(this);
//        try {
//            patchManager.init(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);//current version
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        patchManager.loadPatch();
//        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ali.apatch");
//        try {
//            patchManager.addPatch(file.getAbsolutePath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        AdhocConfig adhocConfig = new AdhocConfig.Builder()
                //设置App上下文(必要参数)
                .context(this)
                //设置Appkey(必要参数)
//                .appKey("ADHOC_6cd6a401-05ca-4ae8-acb1-35254d2f3042")
//                .enableDebugAssist(true)
//                .appKey("ADHOC_ba870cf3-cd5f-40ec-89cf-cec195a93a41")
//                .appKey("ADHOC_7207c39e-48df-4d4a-b142-4e4eddbb0ddb")
                .appKey("ADHOC_a75b344c-4c67-49ce-8fce-9a2739193b71")
//                .addCustom("ip", "124.65.147.10")
//                .reportImmediately()
                .build();
//
        AdhocTracker.init(adhocConfig);


    }

    /**
     * 初始化Bmob支付
     */
    private void initPay() {
        BP.init(this, AppConstant.BMOB_AppID);
//        int pluginVersion = BP.getPluginVersion();
//        if (pluginVersion < PLUGINVERSION) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
//            ToastUtil.show(this,
//                    pluginVersion == 0 ? getString(R.string.plugin_not_installed)
//                            : getString(R.string.plugin_not_latest));
//            installBmobPayPlugin(AppConstant.PAY_PLUGIN_NAME);
//        }
    }

    /**
     * 安装Bmob支付插件
     *
     * @param fileName
     */
    public void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void initAndfix() {
//        //初始化patch管理类
//        mPatchManager = new PatchManager(this);
//        mPatchManager.init("1.0");//补丁版本
//        mPatchManager.loadPatch();//加载补丁
//
//        try {
//            mPatchManager.addPatch("/sdcard/ali.apatch");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static CoolApplication getAppContext() {
        return appContext;
    }

    public List<BDLocation> getLocations() {
        return mLocations;
    }

    public void setLocations(List<BDLocation> locations) {
        mLocations = locations;
    }

    public String getAppVersion() {
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return this.getString(R.string.current_version) + version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return this.getString(R.string.version_unknown);
        }
    }

    public String getAppPackageName() {
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String packageName = info.packageName;
            return packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "com.netease.edu.study";
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
