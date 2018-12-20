package adapter.test;

import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * Created by dongyuangui on 2018/5/10.
 */

public class ReflectAdapter {

    public static void reflectAdapter(ListView listView) {
        try {
            Field field = ReflectionUtil.getFieldByName(listView.getClass(), "mAdapter", "android.widget.AbsListView");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}
