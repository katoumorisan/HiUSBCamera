package com.katoumori.libusbcamera;

import android.hardware.usb.UsbDevice;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.serenegiant.usb.common.AbstractUVCCameraHandler;
import com.serenegiant.usb.widget.CameraViewInterface;

public class UVCCameraFactory implements LifecycleObserver {
    private static final String TAG = UVCCameraFactory.class.getSimpleName();
    private static UVCCameraFactory mCameraFactory;
    private AppCompatActivity activity;
    public View mTextureView;
    private UVCCameraHelper mCameraHelper;
    private CameraViewInterface mUVCCameraView;
    private boolean isRequest;
    private boolean isPreview;

    public static UVCCameraFactory getInstance() {
        if (mCameraFactory == null) {
            mCameraFactory = new UVCCameraFactory();
        }
        return mCameraFactory;
    }

    public void init(AppCompatActivity activity, int resId) {
        activity.getLifecycle().addObserver(mCameraFactory);
        this.activity = activity;
        mTextureView = activity.findViewById(resId);
        mUVCCameraView = (CameraViewInterface) mTextureView;
        mUVCCameraView.setCallback(callback);
        mCameraHelper = UVCCameraHelper.getInstance();
        mCameraHelper.setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_MJPEG);
        mCameraHelper.initUSBMonitor(activity, mUVCCameraView, listener);

    }

    public void setOnPreviewFrameListener(AbstractUVCCameraHandler.OnPreViewResultListener mListner) {
        if (mCameraHelper == null) {
            return;
        }
        mCameraHelper.setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] nv21Yuv) {
                Log.d(TAG, "onPreviewResult: "+nv21Yuv.length);
                mListner.onPreviewResult(nv21Yuv);
            }
        });
    }

    private UVCCameraHelper.OnMyDevConnectListener listener = new UVCCameraHelper.OnMyDevConnectListener() {

        @Override
        public void onAttachDev(UsbDevice device) {
            // request open permission
            if (!isRequest) {
                isRequest = true;
                if (mCameraHelper != null) {
                    mCameraHelper.requestPermission(0);
                }
            }
        }

        @Override
        public void onDettachDev(UsbDevice device) {
            // close camera
            if (isRequest) {
                isRequest = false;
                mCameraHelper.closeCamera();
                showShortMsg(device.getDeviceName() + " is out");
            }
        }

        @Override
        public void onConnectDev(UsbDevice device, boolean isConnected) {
            if (!isConnected) {
                showShortMsg("fail to connect,please check resolution params");
                isPreview = false;
            } else {
                isPreview = true;
                showShortMsg("connecting");

            }
        }

        @Override
        public void onDisConnectDev(UsbDevice device) {
            showShortMsg("disconnecting");
        }
    };

    private CameraViewInterface.Callback callback = new CameraViewInterface.Callback() {
        @Override
        public void onSurfaceCreated(CameraViewInterface view, Surface surface) {
            if (!isPreview && mCameraHelper.isCameraOpened()) {
                mCameraHelper.startPreview(mUVCCameraView);
                isPreview = true;
            }
        }

        @Override
        public void onSurfaceChanged(CameraViewInterface view, Surface surface, int width, int height) {

        }

        @Override
        public void onSurfaceDestroy(CameraViewInterface view, Surface surface) {
            if (isPreview && mCameraHelper.isCameraOpened()) {
                mCameraHelper.stopPreview();
                isPreview = false;
            }
        }
    };

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        if (mCameraHelper != null) {
            mCameraHelper.registerUSB();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (mCameraHelper != null) {
            mCameraHelper.unregisterUSB();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (mCameraHelper != null) {
            mCameraHelper.release();
        }
    }

    private void showShortMsg(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
