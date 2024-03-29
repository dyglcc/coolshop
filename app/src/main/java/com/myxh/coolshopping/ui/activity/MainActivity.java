package com.myxh.coolshopping.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.adhoc.adhocsdk.AdhocTracker;
import com.adhoc.adhocsdk.ExperimentFlags;
import com.adhoc.adhocsdk.OnAdHocReceivedData;
import com.myxh.coolshopping.R;
import com.myxh.coolshopping.listener.TextInputWatcher;
import com.myxh.coolshopping.model.User;
import com.myxh.coolshopping.ui.base.BaseActivity;
import com.myxh.coolshopping.ui.fragment.AroundFragment;
import com.myxh.coolshopping.ui.fragment.HomeFragment;
import com.myxh.coolshopping.ui.fragment.MeFragment;
import com.myxh.coolshopping.ui.fragment.MoreFragment;

public class MainActivity extends BaseActivity {

    private FragmentTabHost tabhost;

    private Class[] fragments = new Class[]{
            HomeFragment.class, AroundFragment.class,
            MeFragment.class, MoreFragment.class};
    private int[] resTitles = new int[]{
            R.string.tab_title_home, R.string.tab_title_around,
            R.string.tab_title_me, R.string.tab_title_more};
    private int[] icons = new int[]{
            R.drawable.tab_home_selector, R.drawable.tab_around_selector,
            R.drawable.tab_me_selector, R.drawable.tab_more_selector};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("cycle", "oncreate");

        initViews();
        setViewWithIntentData();
//        Apptimize.setup(this, "D4tKbTLE3UVnBhbebWkbkAvE5f7sEac");

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.i(TAG, "onGlobalLayout: aaaaaaaaaaaaaaaaaaa");
            }
        });

        Log.i("cycle", "onstart");
        final long t1 = System.currentTimeMillis();
        AdhocTracker.asyncGetFlag(1000, new OnAdHocReceivedData() {
            @Override
            public void onReceivedData(ExperimentFlags experimentFlags) {
                long t2 = System.currentTimeMillis();
                Log.i("Main", "" + (t2 - t1));
                new AlertDialog.Builder(MainActivity.this).setMessage("aaaa").show();
                Toast.makeText(MainActivity.this, "async 返回" + experimentFlags.getRawFlags(), Toast.LENGTH_LONG).show();

            }
        });

//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//
//                long t2 = System.currentTimeMillis();
//                Log.i("Main", "run " + (t2 - t1));
//            }
//        };
//        boolean result = new Handler(Looper.getMainLooper()).postDelayed(run, 200);
//        Handler handler = new Handler(Looper.getMainLooper());
//        Message msg = handler.obtainMessage();
//
//        handler.sendMessageAtFrontOfQueue(msg);



//        Log.i("Main", result + "");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
    }

    private void setViewWithIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            User user = (User) intent.getSerializableExtra(RegisterActivity.INTENT_USER);
            if (user != null) {
                tabhost.setCurrentTab(2);
            }
        }
    }

    private void initViews() {
        tabhost = (FragmentTabHost) findViewById(R.id.main_tabHost);
        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                System.out.println("tabId " + tabId);
                System.out.println(tabhost.getCurrentTabView());
            }
        });
        tabhost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        for (int i = 0; i < fragments.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.item_tab, null);
            ImageView tabIcon = (ImageView) view.findViewById(R.id.item_tab_iv);
            TextView tabTitle = (TextView) view.findViewById(R.id.item_tab_tv);
            tabTitle.addTextChangedListener(new TextInputWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                    System.out.println("aaaasdfasdfsdf");
                }
            });
            tabIcon.setImageResource(icons[i]);
            tabTitle.setText(resTitles[i]);
            tabhost.addTab(tabhost.newTabSpec("" + i).setIndicator(view), fragments[i], null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i("cycle", "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("cycle", "onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i("cycle", "onDestory");
    }

}
