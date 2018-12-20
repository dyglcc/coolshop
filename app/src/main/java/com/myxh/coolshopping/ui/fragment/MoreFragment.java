package com.myxh.coolshopping.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adhoc.adhocsdk.AdhocTracker;
import com.adhoc.adhocsdk.ExperimentFlags;
import com.adhoc.adhocsdk.OnAdHocReceivedData;
import com.adhoc.config.AdhocConfig;
import com.myxh.coolshopping.R;
import com.myxh.coolshopping.common.CoolApplication;
import com.myxh.coolshopping.ui.activity.MainActivity;
import com.myxh.coolshopping.ui.base.BaseFragment;
import com.myxh.coolshopping.util.DataClearUtil;
import com.myxh.coolshopping.util.ToastUtil;

import java.util.Date;

import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by asus on 2016/8/27.
 */
public class MoreFragment extends BaseFragment implements View.OnClickListener {
    public static String TAG = MoreFragment.class.getSimpleName();
    private Toolbar mToolbar;
    private CheckBox mBtnWifiSwitch;
    private CheckBox mBtnNoticeSwitch;
    private RelativeLayout mItemShareLayout;
    private TextView mItemTvCacheSize;
    private RelativeLayout mItemClearCacheLayout;
    private RelativeLayout mItemCommentLayout;
    private RelativeLayout mItemFeedbackLayout;
    private TextView mItemTvKefu;
    private RelativeLayout mItemContactKefuLayout;
    private TextView mItemTvCurrentVersion;
    private RelativeLayout mItemCheckUpdateLayout;
    private RelativeLayout mItemAboutLayout;
    private View view;

    //    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: ");
        View view1 = inflater.inflate(R.layout.fragment_more, null);
        initView(view1);
        view = view1;
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Log.i("aaa", "onViewAttachedToWindow: ");
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
        return view1;
    }

    @Override
    public boolean getUserVisibleHint() {
        Log.i(TAG, "super.getUserVisibleHint(): " + super.getUserVisibleHint());
        return super.getUserVisibleHint();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i(TAG, "setUserVisibleHint: " + isVisibleToUser);
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.more_toolbar);
        mBtnWifiSwitch = (CheckBox) view.findViewById(R.id.more_btn_wifi_switch);
        mBtnNoticeSwitch = (CheckBox) view.findViewById(R.id.more_btn_notice_switch);
        mItemShareLayout = (RelativeLayout) view.findViewById(R.id.more_item_share_layout);
        mItemTvCacheSize = (TextView) view.findViewById(R.id.more_item_tv_cacheSize);
        mItemClearCacheLayout = (RelativeLayout) view.findViewById(R.id.more_item_clear_cache_layout);
        mItemCommentLayout = (RelativeLayout) view.findViewById(R.id.more_item_comment_layout);
        mItemFeedbackLayout = (RelativeLayout) view.findViewById(R.id.more_item_feedback_layout);
        mItemTvKefu = (TextView) view.findViewById(R.id.more_item_tv_kefu);
        mItemContactKefuLayout = (RelativeLayout) view.findViewById(R.id.more_item_contact_kefu_layout);
        mItemTvCurrentVersion = (TextView) view.findViewById(R.id.more_item_tv_current_version);
        mItemCheckUpdateLayout = (RelativeLayout) view.findViewById(R.id.more_item_check_update_layout);
        mItemAboutLayout = (RelativeLayout) view.findViewById(R.id.more_item_about_layout);
        view.findViewById(R.id.wifi_tip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdhocTracker.setClientId("aaaaaaaaaaaaaaaaaaaaa-clientid");
            }
        });

        mBtnWifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {

                }
            }
        });
        mBtnNoticeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {

                }
            }
        });
        mItemShareLayout.setOnClickListener(this);
        mItemClearCacheLayout.setOnClickListener(this);
        mItemCommentLayout.setOnClickListener(this);
        mItemFeedbackLayout.setOnClickListener(this);
        mItemContactKefuLayout.setOnClickListener(this);
        mItemCheckUpdateLayout.setOnClickListener(this);
        mItemAboutLayout.setOnClickListener(this);

        mItemTvCacheSize.setText(DataClearUtil.getTotalCacheSize(getActivity()));
        mItemTvCurrentVersion.setText(CoolApplication.getAppContext().getAppVersion());


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_item_share_layout:
                break;
            case R.id.more_item_clear_cache_layout:
                DataClearUtil.cleanAllCache(getActivity());
                ToastUtil.show(getActivity(), R.string.clear_cache_success);
                mItemTvCacheSize.setText(DataClearUtil.getTotalCacheSize(getActivity()));
                break;
            case R.id.more_item_comment_layout:
                openAppMarket("com.netease.edu.study");
                break;
            case R.id.more_item_feedback_layout:
                break;
            case R.id.more_item_contact_kefu_layout:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + mItemTvKefu.getText()));
                startActivity(intent);
                break;
            case R.id.more_item_check_update_layout:
                BmobUpdateAgent.forceUpdate(getActivity());
                break;
            case R.id.more_item_about_layout:
                break;
        }
    }

    /**
     * 打开应用商店
     *
     * @param packageName
     */
    private void openAppMarket(String packageName) {
        try {
            String str = "market://detail?id=" + packageName;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(str));
            startActivity(intent);
        } catch (Exception e) {
            ToastUtil.show(getActivity(), R.string.open_app_market_failed);
            e.printStackTrace();
            //打开应用商店失败后通过浏览器访问
            String url = "https://github.com/myxh";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "onAttach: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
        final long t1 = System.currentTimeMillis();
//        AdhocTracker.asyncGetFlag(200, new OnAdHocReceivedData() {
//            @Override
//            public void onReceivedData(ExperimentFlags experimentFlags) {
//                long t2 = System.currentTimeMillis();
//                Log.i("Main", "" + (t2 - t1));
//                int v = experimentFlags.getFlag("newvar001", -100);
//                new AlertDialog.Builder(getActivity()).setMessage("" + experimentFlags.getRawFlags() + "  " + v).show();
//                Toast.makeText(getActivity(), "async 返回" + experimentFlags.getRawFlags(), Toast.LENGTH_LONG).show();
//
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        setUserVisibleHint(false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
        AdhocTracker.track("flag001", 1);
    }
}
