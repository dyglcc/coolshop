package adapter.test;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.myxh.coolshopping.R;

/**
 * Created by dongyuangui on 2018/5/10.
 */

public class BaseA implements ListAdapter {
    ListAdapter origin = null;

    public void setOrigin(ListAdapter origin) {
        this.origin = origin;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return origin.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return origin.isEnabled(position);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        origin.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        origin.unregisterDataSetObserver(observer);
    }

    @Override
    public int getCount() {
        return origin.getCount();
    }

    @Override
    public Object getItem(int position) {
        return origin.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return origin.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return origin.hasStableIds();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("hook", "getView: hooked position" + convertView);
        View view = origin.getView(position, convertView, parent);
        if (view != null) {
            TextView textView = (TextView) view.findViewById(R.id.good_tv_title);
            if (textView != null) {
                if (position == 4) {
                    textView.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                    textView.setTextSize(30);
                } else {
                    textView.setText("ccccccccccccccccc");
                    textView.setTextSize(16);
                }
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.good_icon);
            if (imageView != null) {
                if (position == 4) {
                    imageView.setImageResource(R.drawable.banner01);
                }
            }

        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return origin.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return origin.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return origin.isEmpty();
    }
}
