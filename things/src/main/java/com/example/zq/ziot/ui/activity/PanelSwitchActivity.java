package com.example.zq.ziot.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.zq.ziot.R;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
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
public class PanelSwitchActivity extends BaseActivity {
    private static final String TAG = "PanelSwitchActivity";
    private Gpio mSwitchPinLowValidate = null;
    private final String SWITCH_PIN_NAME = "GPIO2_IO05";
    private PeripheralManagerService mPeripheralManagerService;
    private GpioCallback mGpioCallback = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_switch);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(mGpioCallback !=null && mSwitchPinLowValidate != null){
                mSwitchPinLowValidate.unregisterGpioCallback(mGpioCallback);
                mSwitchPinLowValidate.close();
            }
        }catch (IOException e){e.printStackTrace();}
    }

    private void init(){
        mPeripheralManagerService  = new PeripheralManagerService();
        try {
            mSwitchPinLowValidate = mPeripheralManagerService.openGpio(SWITCH_PIN_NAME);
            mSwitchPinLowValidate.setDirection(Gpio.DIRECTION_IN);
            mSwitchPinLowValidate.setActiveType(Gpio.ACTIVE_LOW);
            mSwitchPinLowValidate.setEdgeTriggerType(Gpio.EDGE_FALLING);
            mGpioCallback = new SwitchGpioCallback();
            mSwitchPinLowValidate.registerGpioCallback(mGpioCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(mSwitchPinLowValidate == null){
                Toast.makeText(this, "init failed", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanelSwitchActivity.this.finish();
                Log.d(TAG,"click");
            }
        });
    }
    private class SwitchGpioCallback extends GpioCallback{
        public SwitchGpioCallback() {
            super();
        }

        @Override
        public boolean onGpioEdge(Gpio gpio) {
            try{
                if(!gpio.getValue()){
                    Toast.makeText(PanelSwitchActivity.this, "Switch pressed", Toast.LENGTH_SHORT).show();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return super.onGpioEdge(gpio);
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
            Log.d(TAG,"onGpioError : " + error);
            super.onGpioError(gpio, error);
        }
    }
}

