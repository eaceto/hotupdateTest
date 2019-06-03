package com.hotupdatetest;

import android.os.Bundle;
import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {
    private static final String TAG = "saul";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
