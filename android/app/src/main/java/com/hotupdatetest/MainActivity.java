package com.hotupdatetest;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {

    private long mDownLoadId;
    private CompleteReceiver localReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registeReceiver();
    }


    public void load(View v) {
        Log.d("LoadSaul", "loaddingggggggggggggggggggggg");

    }

    /**
     * 注册广播
     */
    private void registeReceiver() {
        localReceiver = new CompleteReceiver();
        // 注册，等另一个任务完成后执行
        registerReceiver(localReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    public class CompleteReceiver extends BroadcastReceiver {
        // 类似回调函数
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(localReceiver);
    }


    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "hotUpdateTest";
    }
}
