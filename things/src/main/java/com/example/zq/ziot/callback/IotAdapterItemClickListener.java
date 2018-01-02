package com.example.zq.ziot.callback;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zq.ziot.R;

import java.util.Map;

/**
 * Created by zq on 2017/12/27.
 */

public class IotAdapterItemClickListener implements AdapterView.OnItemClickListener {
    private Map<String, Class<?>> targetActivityMap = null;
    private Context context = null;

    public IotAdapterItemClickListener(Context activity , Map<String, Class<?>> targetActivityMap) {
        this.targetActivityMap = targetActivityMap;
        this.context = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(queryClassNamebyMenuItem(view) == null){
            Toast.makeText(context, "unimplement", Toast.LENGTH_SHORT).show();
            return;
        }
        this.context.startActivity(new Intent(context, queryClassNamebyMenuItem(view)));
    }

    private Class<?> queryClassNamebyMenuItem( View v){
        TextView titleView = v.findViewById(R.id.sub_item_title);
        return  targetActivityMap.get(titleView.getText().toString());
    }
}
