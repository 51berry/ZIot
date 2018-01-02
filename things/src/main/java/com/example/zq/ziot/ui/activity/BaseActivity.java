package com.example.zq.ziot.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.zq.ziot.ui.widget.DragButton;
import com.example.zq.ziot.util.IotUtil;
import com.example.zq.ziot.R;

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
public class BaseActivity extends Activity {

        private int mScreenW = 0;
        private int mScreenH = 0;
        protected  DragButton mBackBt = null;
        protected FrameLayout mContentParent = null;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_base);
            mScreenW = IotUtil.getScreenWidth(this);
            mScreenH = IotUtil.getScreenHeight(this);
            mBackBt = new DragButton(this);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mContentParent != null && mBackBt != null){
            mContentParent.removeView(mBackBt);
        }
    }

    @Override
        protected void onResume() {
            super.onResume();
            mBackBt.setTextColor(Color.DKGRAY);
            mBackBt.setText("Back");
            mBackBt.setTextSize(14);
            mBackBt.setPadding(10,10,10,10);
            mBackBt.setBackgroundColor(6);
            mBackBt.setGravity(Gravity.CENTER);
            mBackBt.setBackgroundColor(Color.GRAY);
            mBackBt.setLayoutParams( new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mBackBt.setX(mScreenW - 100);
            mBackBt.setY(mScreenH - 100);
            if(mContentParent == null){
                mContentParent = (FrameLayout) getWindow().getDecorView().findViewById(android.R.id.content);
                mContentParent.addView(mBackBt);
            }
        }
}
