package adapter.test;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myxh.coolshopping.R;
import com.myxh.coolshopping.entity.GoodsInfo;
import com.myxh.coolshopping.ui.adapter.GoodsListAdapter;
import com.myxh.coolshopping.ui.fragment.HomeFragment;

import java.util.List;

/**
 * Created by dongyuangui on 2018/5/11.
 */

public class SubClass extends GoodsListAdapter {
    public SubClass(Context context, List<GoodsInfo.ResultBean.GoodlistBean> goodlist, int headerCount) {
        super(context, goodlist, headerCount);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        super.getView(i, convertView, viewGroup);
//        Log.i("hook", "getView: hooked position" + convertView);
//        if (convertView != null) {
//            TextView textView = (TextView) convertView.findViewById(R.id.good_tv_title);
//            if (textView != null) {
//                if (i == 4) {
//                    textView.setText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//                    textView.setTextSize(30);
//                } else {
//                    textView.setText("ccccccccccccccccc");
//                    textView.setTextSize(16);
//                }
//            }
//            ImageView imageView = (ImageView) convertView.findViewById(R.id.good_icon);
//            if (imageView != null) {
//                if (i == 4) {
//                    imageView.setImageResource(R.drawable.banner01);
//                }
//            }
//
//        }
        return convertView;
    }
}
