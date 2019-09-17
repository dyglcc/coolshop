package com.myxh.coolshopping.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adhoc.adhocsdk.AdhocTracker;
import com.adhoc.adhocsdk.ExperimentFlags;
import com.adhoc.adhocsdk.OnAdHocReceivedData;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.myxh.coolshopping.R;
import com.myxh.coolshopping.common.AppConstant;
import com.myxh.coolshopping.entity.FilmInfo;
import com.myxh.coolshopping.entity.GoodsInfo;
import com.myxh.coolshopping.entity.HomeGridInfo;
import com.myxh.coolshopping.listener.ViewPagerListener;
import com.myxh.coolshopping.model.User;
import com.myxh.coolshopping.ui.activity.CityActivity;
import com.myxh.coolshopping.ui.activity.DetailActivity;
import com.myxh.coolshopping.ui.activity.MessageActivity;
import com.myxh.coolshopping.ui.adapter.BannerPagerAdapter;
import com.myxh.coolshopping.ui.adapter.GoodsListAdapter;
import com.myxh.coolshopping.ui.adapter.GridAdapter;
import com.myxh.coolshopping.ui.adapter.ViewPageAdapter;
import com.myxh.coolshopping.ui.base.BaseFragment;
import com.myxh.coolshopping.ui.widget.Indicator;
import com.myxh.coolshopping.util.ToastUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import adapter.test.ReflectAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by asus on 2016/8/27.
 */
public class HomeFragment extends BaseFragment {

    public static String TAG = HomeFragment.class.getSimpleName();
    private static final int GOOD_REQUEST = 0x01;
    private static final int FILM_REQUEST = 0x02;
    private static final int SCAN_QR_REQUEST = 103;
    public static final String GOODS_ID = "goodsId";
    public static final String GOODS_SEVEN_REFUND = "sevenRefund";
    public static final String GOODS_TIME_REFUND = "timeRefund";
    public static final String GOODS_BOUGHT = "bought";
    private static final int CITY_REQUEST_CODE = 4000;

    private int[] imgRes = new int[]{R.drawable.banner01, R.drawable.banner02, R.drawable.banner03};
    private Handler mHandler = new Handler();
    //广告轮播
    private ViewPager bannerPager;
    private Indicator bannerIndicator;
    private View mView;

    private List<HomeGridInfo> pageOneData = new ArrayList<>();
    private List<HomeGridInfo> pageTwoData = new ArrayList<>();
    private PullToRefreshListView mRefreshListView;
    private List<View> mViewList = new ArrayList<>();

    private List<GoodsInfo.ResultBean.GoodlistBean> mGoodlist = new ArrayList<>();
    private List<FilmInfo.ResultBean> mFilmList = new ArrayList<>();
    private LinearLayout mFilmLayout;
    private ListView mListView;
    private GoodsListAdapter mGoodsListAdapter;

    //是否正在刷新
    private boolean isRefreshing = false;
    private TextView mCityName;
    private View headView;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView: ");
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_home, null);
            initViews(mView);
            initData();
            antoScroll();
            mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
//                    Toast.makeText(getActivity(), "view change ", Toast.LENGTH_LONG).show();
                    Log.i("viewchange", "viewchange");
                }
            });
        }

        return mView;
    }

    private void exeNetWork() {
        AsyncTask<Void, Void, Object[]> task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {

//                Response response = requestRecommend();

                Object[] objects1 = new Object[2];
                try {
                    InputStream inputstream = HomeFragment.this.getContext().getAssets().open("spRecommend.txt");
                    long st = System.currentTimeMillis();
                    Log.i(TAG, "doInBackground: start read byte");
                    int x = inputstream.available();
                    byte[] bytes = new byte[x];
                    inputstream.read(bytes);

                    BufferedInputStream bis = new BufferedInputStream(inputstream);
//                    if (response == null || response.body() == null) {
//                        return null;
//                    }t
                    objects1[0] = new String(bytes, "utf8");
                    Log.i(TAG, "doInBackground: end read byte " + (System.currentTimeMillis() - st));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                objects1[1] = true;

                return objects1;
            }

            @Override
            protected void onPostExecute(Object response) {
                super.onPostExecute(response);
                if (response == null) {
                    return;
                }
                Object[] objects = (Object[]) response;
                String resString = (String) objects[0];
                Log.i("request", resString);
                boolean success = (boolean) objects[1];
                if (success) {
                    Gson gson = new Gson();
                    GoodsInfo goodsInfo = gson.fromJson(resString, GoodsInfo.class);
                    List<GoodsInfo.ResultBean.GoodlistBean> goodlistBeen = goodsInfo.getResult().getGoodlist();
//                    mGoodlist.clear();
//                    mGoodlist.addAll(goodlistBeen);
                    mGoodlist = goodlistBeen;
                    int headerViewsCount = mListView.getHeaderViewsCount();
                    mGoodsListAdapter = new GoodsListAdapter(getActivity(), mGoodlist, headerViewsCount);
                    mListView.setAdapter(mGoodsListAdapter);
                    Toast.makeText(getActivity(), "刷新完成", Toast.LENGTH_LONG).show();
//                    mGoodsListAdapter.notifyDataSetChanged();
                } else {
                    if (isRefreshing) {
                        mRefreshListView.onRefreshComplete();
                        isRefreshing = false;
                        ToastUtil.show(getActivity(), "刷新失败");
                    }
                }
            }
        };
        task.execute();


        @SuppressLint("StaticFieldLeak") AsyncTask taskhotfilm = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Object[] objects1 = null;
                try {
                    InputStream inputStream = HomeFragment.this.getActivity().getAssets().open("filmHot_refresh.txt");
                    int aviliable = inputStream.available();
                    byte[] bytes = new byte[aviliable];
                    inputStream.read(bytes);
                    String strings = new String(bytes, "utf8");
                    objects1 = new Object[2];

                    objects1[0] = strings;
                    objects1[1] = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return objects1;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (o == null) {
                    return;
                }
                Object[] objects = (Object[]) o;
                String resString = (String) objects[0];
                boolean success = (boolean) objects[1];
                if (success) {
                    Gson filmGson = new Gson();
                    FilmInfo filmInfo = null;
                    filmInfo = filmGson.fromJson(resString, FilmInfo.class);
                    List<FilmInfo.ResultBean> filmList = filmInfo.getResult();
                    mFilmList.clear();
                    mFilmList.addAll(filmList);
                    mFilmLayout.removeAllViews();
                    if (mFilmList == null) {
                        return;
                    }

                    for (int i = 0; i < mFilmList.size(); i++) {
//                        if(getActivity() == null){
//                            return;
//                        }
                        View filmItemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_film, null);
                        SimpleDraweeView filmIcon = (SimpleDraweeView) filmItemView.findViewById(R.id.home_film_icon);
                        TextView filmTitle = (TextView) filmItemView.findViewById(R.id.home_film_title);
                        TextView filmGrade = (TextView) filmItemView.findViewById(R.id.home_film_grade);
                        filmIcon.setImageURI(Uri.parse(mFilmList.get(i).getPosterUrl()));
                        filmTitle.setText(mFilmList.get(i).getFilmName());
                        filmGrade.setText(mFilmList.get(i).getGrade() + "分");
                        mFilmLayout.addView(filmItemView);
                    }
                } else {
                    if (isRefreshing) {
                        mRefreshListView.onRefreshComplete();
                        isRefreshing = false;
                        ToastUtil.show(getActivity(), "刷新失败");
                    }
                }
            }
        };
        taskhotfilm.execute();
    }


    private void initData() {
        String[] gridTitles = getResources().getStringArray(R.array.home_bar_labels);
        TypedArray typedArray = getResources().obtainTypedArray(R.array.home_bar_icon);
        for (int i = 0; i < gridTitles.length; i++) {
            if (i < 8) {
                pageOneData.add(new HomeGridInfo(typedArray.getResourceId(i, 0), gridTitles[i]));
            } else {
                pageTwoData.add(new HomeGridInfo(typedArray.getResourceId(i, 0), gridTitles[i]));
            }
        }

        exeNetWork();

    }

    private Response requestRecommend() {
        OkHttpClient client = new OkHttpClient();
//        Auth auth = Auth.create("p-miksYPHqTgd7GHb7NFIbr8V264KLnNrlHx3K4i", "0IABtf4JEDmVgbQHg96aa_uia8lw9_qnsP6a5iux");
//        String url = auth.privateDownloadUrl(AppConstant.RECOMMEND_URL);
        Request rq = new Request.Builder().url(AppConstant.RECOMMEND_URL).build();
        try {
            Response response = client.newCall(rq).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    private Response hotfilm() {
        OkHttpClient client = new OkHttpClient();
        Request rq = new Request.Builder().url(AppConstant.HOT_FILM_URL).build();
        try {
            Response response = client.newCall(rq).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    private void antoScroll() {
        if (mHandler == null) {
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = bannerPager.getCurrentItem();
                if (bannerPager != null) {
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        bannerPager.setCurrentItem(currentItem + 1, true);
                        if (mHandler != null) {
                            mHandler.postDelayed(this, 2000);
                        }
                    }
                }
            }
        }, 2000);
    }

    ViewPager headPager = null;

    private void initViews(View view) {
        //titleBar
        View titleView = view.findViewById(R.id.home_titlebar);
        initTitlebar(titleView);
        mRefreshListView = (PullToRefreshListView) view.findViewById(R.id.home_pull_to_refresh_listView);
        //header头部
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.home_head_page, null);
        //banner
        View bannerView = headView.findViewById(R.id.home_head_include_banner);
        bannerPager = (ViewPager) bannerView.findViewById(R.id.home_banner_pager);
        bannerIndicator = (Indicator) bannerView.findViewById(R.id.home_banner_indicator);
        bannerPager.setAdapter(new BannerPagerAdapter(getChildFragmentManager(), imgRes));
        bannerPager.addOnPageChangeListener(new ViewPagerListener(bannerIndicator));
        bannerPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Log.w("tag", "onPageSelected: ");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        headPager = (ViewPager) headView.findViewById(R.id.home_head_pager);
        Indicator headIndicator = (Indicator) headView.findViewById(R.id.home_head_indicator);
        //第一页
        View pageOne = LayoutInflater.from(getActivity()).inflate(R.layout.home_gridview, null);
        GridView gridView1 = (GridView) pageOne.findViewById(R.id.home_gridView);
        gridView1.setAdapter(new GridAdapter(getActivity(), pageOneData));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
//                    Dialog dialog1 = new Dialog(getActivity(), R.style.DialogMy);
//                    dialog1.setTitle("提示");
//                    dialog1.setContentView(R.layout.dialog_meishi);
//                    dialog1.show();
                    new MaterialDialog.Builder(getActivity())
                            .title("提示")
                            .content("美食")
                            .show();
                }

            }
        });
        //第二页
        View pageTwo = LayoutInflater.from(getActivity()).inflate(R.layout.home_gridview, null);
        GridView gridView2 = (GridView) pageTwo.findViewById(R.id.home_gridView);
        gridView2.setAdapter(new GridAdapter(getActivity(), pageTwoData));
        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        mViewList.add(pageOne);
        mViewList.add(pageTwo);
        headPager.setAdapter(new ViewPageAdapter(mViewList));
        headPager.addOnPageChangeListener(new ViewPagerListener(headIndicator));
        headPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("a", "aaaa");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //热门电影
        View filmView = headView.findViewById(R.id.home_head_include_film);
        mFilmLayout = (LinearLayout) filmView.findViewById(R.id.home_film_ll);

//        mRefreshListView.addHeaderView(bannerView);
        mListView = mRefreshListView.getRefreshableView();
        mListView.addHeaderView(headView);
        mListView.setHeaderDividersEnabled(false);
        int headerViewsCount = mListView.getHeaderViewsCount();
        mGoodsListAdapter = new GoodsListAdapter(getActivity(), mGoodlist, headerViewsCount);
        mRefreshListView.setAdapter(mGoodsListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString(GOODS_ID, mGoodlist.get(i - 2).getGoods_id());
                bundle.putString(GOODS_SEVEN_REFUND, mGoodlist.get(i - 2).getSeven_refund());
                bundle.putInt(GOODS_TIME_REFUND, mGoodlist.get(i - 2).getTime_refund());
                bundle.putInt(GOODS_BOUGHT, mGoodlist.get(i - 2).getBought());
                openActivity(DetailActivity.class, bundle);
            }
        });

        //下拉刷新
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                isRefreshing = true;
                exeNetWork();
            }
        });
//        deal();
    }

    public void deal() {

        mGoodsListAdapter = (GoodsListAdapter) mListView.getAdapter();

    }

    private void initTitlebar(View view) {
        LinearLayout cityLayout = (LinearLayout) view.findViewById(R.id.titleBar_location_lay);
        mCityName = (TextView) view.findViewById(R.id.titleBar_city_name);
        cityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),CityActivity.class);
//                intent.putExtra(AppConstant.KEY_CITY,mCityName.getText().toString());
//                startActivityForResult(intent,CITY_REQUEST_CODE);
            }
        });

        ImageView scanQR = (ImageView) view.findViewById(R.id.titleBar_scan_img);
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                getActivity().startActivityForResult(intent, SCAN_QR_REQUEST);
            }
        });

        ImageView messageBox = (ImageView) view.findViewById(R.id.titleBar_msg_iv);
        messageBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = User.getCurrentUser(User.class);
                if (user != null) {
                    openActivity(MessageActivity.class);
                } else {
                    ToastUtil.show(getActivity(), R.string.me_nologin_not_login);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SCAN_QR_REQUEST) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    ToastUtil.show(getActivity(), "解析结果:" + result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtil.show(getActivity(), "解析二维码失败");
                }
            }
        } else if (requestCode == CITY_REQUEST_CODE && resultCode == CityActivity.CITY_RESULT_CODE) {
            if (data != null) {
                String cityname = data.getStringExtra(AppConstant.KEY_CITY);
                if (cityname != null) {
                    mCityName.setText(cityname);
                }
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");

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

        final TextView tv = (TextView) getActivity().findViewById(R.id.titleBar_city_name);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(getActivity(), "track zzle");
                AdhocTracker.track("zzle", 1);
            }
        });
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        AdhocTracker.asyncGetFlag(1000, new OnAdHocReceivedData() {
            @Override
            public void onReceivedData(ExperimentFlags experimentFlags) {

                boolean flag = experimentFlags.getFlag("aa", true);

                if (flag) {
//                    tv.setText("默认版本");
                } else {
//                    tv.setText("实验版本");
                }
//                        Toast.makeText(getActivity(), experimentFlags.getRawFlags().toString(), Toast.LENGTH_LONG).show();
            }
        });
//            }
//        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.i(TAG, "onDetach: ");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
    }


}
