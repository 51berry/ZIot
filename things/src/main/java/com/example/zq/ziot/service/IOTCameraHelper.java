package com.example.zq.ziot.service;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.Collections;

/**
 * Created by zq on 2017/12/28.
 */

public class IOTCameraHelper {
    private static final String TAG = "IOTCameraHelper";
    private static IOTCameraHelper instance = null;
    // Camera image parameters (device-specific)
    private static final int IMAGE_WIDTH  = 480;
    private static final int IMAGE_HEIGHT = 480;
    private static final int MAX_IMAGES   = 5;

    // Image result processor
    private ImageReader mImageReader;
    // Active camera device connection
    private CameraDevice mCameraDevice;
    // Active camera capture session
    private CameraCaptureSession mCaptureSession;

    private static  Object mLock = new Object();

    private IOTCameraHelper(){

    }

    public static IOTCameraHelper getInstance(){
        if(instance == null){
            synchronized (mLock){
                if(instance == null){
                    instance = new IOTCameraHelper();
                }
            }
        }

        return instance;
    }

    // Initialize a new camera device connection
    public void initializeCamera(Context context,
                                 Handler backgroundHandler,
                                 ImageReader.OnImageAvailableListener imageAvailableListener) {

        // Discover the camera instance
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String[] camIds = {};
        try {
            camIds = manager.getCameraIdList();
        } catch (CameraAccessException e) {
            Log.d(TAG, "Cam access exception getting IDs", e);
        }
        if (camIds.length < 1) {
            Log.d(TAG, "No cameras found");
            Toast.makeText(context,"No cameras found", Toast.LENGTH_LONG).show();
            return;
        }
        String id = camIds[0];
        Log.d(TAG, "Using camera id " + id);
        // Initialize image processor
        mImageReader = ImageReader.newInstance(IMAGE_WIDTH, IMAGE_HEIGHT,ImageFormat.JPEG, MAX_IMAGES);
        mImageReader.setOnImageAvailableListener(imageAvailableListener, backgroundHandler);
        // Open the camera resource
        try {
            manager.openCamera(id, mStateCallback, backgroundHandler);
        } catch (CameraAccessException cae) {
            Log.d(TAG, "Camera access exception", cae);
            Toast.makeText(context,"Camera access exception", Toast.LENGTH_LONG).show();
        }
    }

    // Callback handling devices state changes
    private final CameraDevice.StateCallback mStateCallback =
            new CameraDevice.StateCallback() {


                @Override
                public void onOpened( CameraDevice cameraDevice) {
                    mCameraDevice = cameraDevice;
                }

                @Override
                public void onDisconnected( CameraDevice cameraDevice) {

                }

                @Override
                public void onError( CameraDevice cameraDevice, int i) {

                }
            };

    public void takePicture(Context context) {
        if (mCameraDevice == null) {
            Toast.makeText(context,"Cannot capture image. Camera not initialized.",Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Cannot capture image. Camera not initialized.");
            return;
        }

        // Here, we create a CameraCaptureSession for capturing still images.
        try {
            mCameraDevice.createCaptureSession(
                    Collections.singletonList(mImageReader.getSurface()),
                    mSessionCallback,
                    null);
        } catch (CameraAccessException cae) {
            Log.d(TAG, "access exception while preparing pic", cae);
        }
    }

    // Callback handling session state changes
    private CameraCaptureSession.StateCallback mSessionCallback =
            new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    // The camera is already closed
                    if (mCameraDevice == null) {
                        return;
                    }

                    // When the session is ready, we start capture.
                    mCaptureSession = cameraCaptureSession;
                    triggerImageCapture();
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Log.w(TAG, "Failed to configure camera");
                }
            };

    private void triggerImageCapture() {
        try {
            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON);
            Log.d(TAG, "Session initialized.");
            mCaptureSession.capture(captureBuilder.build(), mCaptureCallback, null);
        } catch (CameraAccessException cae) {
            Log.d(TAG, "camera capture exception");
        }
    }

    // Callback handling capture progress events
    private final CameraCaptureSession.CaptureCallback mCaptureCallback =
            new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session,
                                               CaptureRequest request,
                                               TotalCaptureResult result) {
                    if (session != null) {
                        session.close();
                        mCaptureSession = null;
                        Log.d(TAG, "CaptureSession closed");
                    }
                }
            };

    // Close the camera resources
    public void shutDown() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
        }
    }
}

