package com.example.zq.ziot.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.zq.ziot.service.IOTCameraHelper;
import com.example.zq.ziot.callback.IOTImageAvailableListener;
import com.example.zq.ziot.callback.OnCameraUICallback;
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
public class PanelCameraActivity extends BaseActivity implements View.OnClickListener, OnCameraUICallback {

    private static final String TAG = "PanelCameraActivity";
    /**
     * A Handler for running tasks in the background.
     */
    private Handler mCameraHandler;
    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mCameraThread;

    private IOTCameraHelper mIOTCameraHelper = null;

    private ImageView mPicPreview = null;

    private ImageButton mTakePicBt = null;

    private Handler mUIhandle = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_camera);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraThread.quitSafely();
        mIOTCameraHelper.shutDown();
    }

    private void init(){
        mUIhandle = new Handler(getMainLooper());
        mPicPreview = findViewById(R.id.image_preview);
        mTakePicBt = findViewById(R.id.bt_take_pic);
        mTakePicBt.setOnClickListener(this);
        mCameraThread = new HandlerThread("CameraBackground");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());
        mIOTCameraHelper = IOTCameraHelper.getInstance();
        mIOTCameraHelper.initializeCamera(this, mCameraHandler, new IOTImageAvailableListener(this));
        mBackBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanelCameraActivity.this.finish();
                Log.d(TAG,"click");
            }
        });
    }

    @Override
    public void onPicBitMapRes(final Bitmap bitmap) {
        mUIhandle.post(new Runnable() {
            @Override
            public void run() {
                mPicPreview.setImageBitmap(bitmap);
            }
        });
    }


    @Override
    public void onClick(View view) {
        mIOTCameraHelper.takePicture(this);
    }
}
