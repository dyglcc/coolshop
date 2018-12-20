package com.myxh.coolshopping.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myxh.coolshopping.R;
import com.myxh.coolshopping.entity.HomeGridInfo;

import java.util.List;

/**
 * Created by asus on 2016/8/31.
 */
public class GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeGridInfo> mData;

    public GridAdapter(Context context, List<HomeGridInfo> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) view.findViewById(R.id.grid_icon);
            holder.tv = (TextView) view.findViewById(R.id.grid_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Bitmap map = BitmapFactory.decodeResource(mContext.getResources(), mData.get(i).getGridIcon());
        Drawable drawable = new BitmapDrawable(mContext.getResources(), map);
        holder.iv.setImageDrawable(drawable);

        holder.tv.setText(mData.get(i).getGridTitle());

//        view = LayoutInflater.from(mContext).inflate(R.layout.item_gridview, null);
//        ImageView iv = (ImageView) view.findViewById(R.id.grid_icon);
//        TextView tv = (TextView) view.findViewById(R.id.grid_title);
//        iv.setImageResource(mData.get(i).getGridIcon());
//        tv.setText(mData.get(i).getGridTitle());
//
//        Log.i("gridView", "getView: " + mData.get(i).getGridTitle() + mData.toString());


        return view;
    }

    final class ViewHolder {
        private ImageView iv;
        private TextView tv;
    }
}
