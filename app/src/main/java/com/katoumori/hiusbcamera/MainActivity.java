package com.katoumori.hiusbcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.katoumori.libusbcamera.UVCCameraHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void button1OnClick(View view) {
        Intent intent = new Intent(MainActivity.this, USBCameraActivity.class);
        startActivity(intent);
        finish();
    }

    public void button2OnClick(View view) {
        Intent intent = new Intent(MainActivity.this, USBCamera2Activity.class);
        startActivity(intent);
        finish();
    }

    public void button3OnClick(View view) {
        boolean res = UVCCameraHelper.getInstance().isCameraConnect(this);
        Toast.makeText(this, "isCameraConnect:"+res, Toast.LENGTH_SHORT).show();
    }
}