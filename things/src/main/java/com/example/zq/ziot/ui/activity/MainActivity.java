package com.example.zq.ziot.ui.activity;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.zq.ziot.ui.widget.IotAdapter;
import com.example.zq.ziot.callback.IotAdapterItemClickListener;
import com.example.zq.ziot.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private ListView mMenuList = null;
    private BaseAdapter mListAdapter = null;
    private List<String> mMenuItemSrcList = new ArrayList();
    private ArrayMap<String, Class<?>> targetActivityMap = new ArrayMap<String, Class<?>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMenuData();
        initView();
    }

    private void initView(){
        mMenuList = findViewById(R.id.list_menu);
        mListAdapter = new IotAdapter(this, mMenuItemSrcList);
        mMenuList.setAdapter(mListAdapter);
        mMenuList.setOnItemClickListener(new IotAdapterItemClickListener(this, targetActivityMap));
    }


    private void initMenuData(){
        targetActivityMap.put("Open Led Control Panel", PanelLedActivity.class);
        targetActivityMap.put("Open Switch Control Panel", PanelSwitchActivity.class);
        targetActivityMap.put("Open Camera Control Panel", PanelCameraActivity.class);
        targetActivityMap.put("Open Alarm Control Panel", null);

        Iterator iterator = targetActivityMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,String> entry = (Map.Entry<String, String>) iterator.next();
            mMenuItemSrcList.add(entry.getKey());
        }
        mBackBt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                MainActivity.this.finish();
                Log.d(TAG,"click");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}
