package com.example.zq.ziot.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zq.ziot.R;

import java.util.List;

/**
 * Created by zq on 2017/12/27.
 */

public class IotAdapter extends BaseAdapter {
    private Context context;
    private List<String> mMenuList = null;

    public IotAdapter(Context activity, List<String> menuList) {
        this.context = activity;
        this.mMenuList = menuList;
    }

    @Override
    public int getCount() {
        return mMenuList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMenuList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView =  view.inflate(context, R.layout.menu_item_layout,null);
        TextView titleView = rootView.findViewById(com.example.zq.ziot.R.id.sub_item_title);
        titleView.setText((String)getItem(i));
        return  rootView;
    }


}
