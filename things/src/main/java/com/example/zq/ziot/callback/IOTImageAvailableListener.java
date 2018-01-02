package com.example.zq.ziot.callback;

import android.media.Image;
import android.media.ImageReader;
import android.os.SystemClock;
import android.util.Log;

import com.example.zq.ziot.service.IOTStorateManager;
import com.example.zq.ziot.util.IotUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by zq on 2017/12/28.
 */

public class IOTImageAvailableListener implements ImageReader.OnImageAvailableListener {
    private static final String TAG = "IOTImageAvailableListener";
    private OnCameraUICallback mCameraUICallback = null;

    public IOTImageAvailableListener(OnCameraUICallback cameraUICallback){
        mCameraUICallback = cameraUICallback;
    }
    @Override
    public void onImageAvailable(ImageReader imageReader) {
        // Get the raw image bytes
        Image image = imageReader.acquireLatestImage();
        ByteBuffer imageBuf = image.getPlanes()[0].getBuffer();
        final byte[] imageBytes = new byte[imageBuf.remaining()];
        imageBuf.get(imageBytes);
        image.close();
        onPictureTaken(imageBytes);
    }

    private void onPictureTaken(final byte[] imageBytes) {
        if (imageBytes != null) {
            File folder = IOTStorateManager.getInstance().getFolder(IOTStorateManager.IMAGE_IDR);
            if(folder == null){
                Log.d(TAG,"can not find imageFolder !");
                return;
            }else {
                File imageFile = new File(folder, SystemClock.currentThreadTimeMillis() + ".jpeg");
                BufferedOutputStream outputStream = null;
                try {
                    outputStream = new BufferedOutputStream(new FileOutputStream(imageFile));
                    outputStream.write(imageBytes);
                    outputStream.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(outputStream != null){
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
               mCameraUICallback.onPicBitMapRes( IotUtil.Bytes2Bimap(imageBytes));
            }
            // ...process the captured image...
        }
    }
}
