package com.hotupdatetest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {
    private static final String TAG = "saul";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "我是 MainActivity");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected String getMainComponentName() {
        return "hotUpdateTest";
    }

}
