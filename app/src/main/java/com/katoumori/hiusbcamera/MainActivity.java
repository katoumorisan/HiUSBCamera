package com.katoumori.hiusbcamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}