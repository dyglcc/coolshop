package adapter.test;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.myxh.coolshopping.entity.GoodsInfo;
import com.myxh.coolshopping.ui.adapter.GoodsListAdapter;

import java.util.List;

/**
 * Created by dongyuangui on 2018/5/10.
 * <p>
 */

public class AdhocAdapter extends GoodsListAdapter {
    public AdhocAdapter(Context context, List<GoodsInfo.ResultBean.GoodlistBean> goodlist, int headerCount) {
        super(context, goodlist, headerCount);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = super.getView(i, convertView, viewGroup);
        Log.i("hook", "view is hooked ");
        return view;
    }
}
