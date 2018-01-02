package com.example.zq.ziot.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.zq.ziot.R;
import com.example.zq.ziot.ui.activity.BaseActivity;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

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
public class  PanelLedActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "PanelLedActivity";
    private Gpio mLedPinLowValidate = null;
    private final String LED_PIN_NAME = "GPIO2_IO00";
    private PeripheralManagerService mPeripheralManagerService;
    private ImageButton mLedSwitch = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_led);
        init();
    }

    private void init(){
        mLedSwitch = findViewById(R.id.bt_led);
        mLedSwitch.setOnClickListener(this);
        mPeripheralManagerService  = new PeripheralManagerService();
        try {
            mLedPinLowValidate = mPeripheralManagerService.openGpio(LED_PIN_NAME);
            mLedPinLowValidate.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            mLedPinLowValidate.setValue(true);
            updateLedStateView();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(mLedPinLowValidate == null){
                mLedSwitch.setBackgroundResource(R.drawable.led_def);
                Toast.makeText(this, "init failed", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanelLedActivity.this.finish();
                Log.d(TAG,"click");
            }
        });
    }

    private void updateLedStateView(){
        boolean state = false;
        try {
            state = mLedPinLowValidate.getValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(state){
            mLedSwitch.setBackgroundResource(R.drawable.led_off);
        }else {
            mLedSwitch.setBackgroundResource(R.drawable.led_on);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(mLedPinLowValidate != null){
                mLedPinLowValidate.close();
            }
            mPeripheralManagerService = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try{
            switch (view.getId()){
                case R.id.bt_led:
                    mLedPinLowValidate.setValue(!mLedPinLowValidate.getValue());
                    updateLedStateView();
                    break;

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
