package com.katoumori.hiusbcamera;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.katoumori.libusbcamera.UVCCameraFactory;
import com.serenegiant.usb.common.AbstractUVCCameraHandler;

public class USBCamera2Activity extends AppCompatActivity {
    private static final String TAG = USBCamera2Activity.class.getSimpleName();
    private UVCCameraFactory mCameraFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usbcamera2);

        mCameraFactory = UVCCameraFactory.getInstance();
        mCameraFactory.init(this,R.id.camera_view2);
        mCameraFactory.setOnPreviewFrameListener(new AbstractUVCCameraHandler.OnPreViewResultListener() {
            @Override
            public void onPreviewResult(byte[] data) {
                Log.d(TAG, "onPreviewResult: "+data.length);
            }
        });
    }
}
