package com.hotupdatetest;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hotupdatetest.constants.FileConstants;

import java.io.File;

public class MyActivity extends FragmentActivity {

    private static final String TAG = "saul";

    private long mDownLoadId;
    private CompleteReceiver localReceiver;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        initUpgradeProcessFragment();

        registeReceiver();
        downloadBundle();

    }


    public void initUpgradeProcessFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        UpgradeProcessFragment fragment = new UpgradeProcessFragment();

        transaction.replace(R.id.upgrade_process_fragment_container, fragment);
        transaction.commit();
    }


    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void downloadBundle() {

        /*
        // 休眠
        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(true) return;
        */


        // 2.下载
        Log.d(TAG, "开始下载新版本");
        // 默认有新版本
        Toast.makeText(getApplicationContext(), "开始下载新版本",  Toast.LENGTH_LONG).show();


        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri myDownloads = Uri.parse(FileConstants.JS_BUNDLE_REMOTE_URL);
        DownloadManager.Request request = new DownloadManager.Request(myDownloads);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI);

        request.setDestinationUri(Uri.parse("file://" + FileConstants.JS_PATCH_LOCAL_PATH));

        Log.d(TAG, FileConstants.JS_PATCH_LOCAL_PATH);

        mDownLoadId = downloadManager.enqueue(request);


        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;
                while (downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    Cursor cursor = downloadManager.query(q);
                    cursor.moveToFirst();
                    int bytes_downloaded = cursor.getInt(cursor
                            .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }
                    final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                    // 已经可以获取进度，下一步思考该如何把进度到 RN 展现，又或者可以通过原生实现
                    Log.d("processOfDownload", Integer.toString(dl_progress));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // UI 进程
                            ProgressBar progressBar = findViewById(R.id.process_bar);
                            progressBar.setProgress(dl_progress);

                        }
                    });
                    cursor.close();
                }

            }
        }).start();
    }



    /**
     * 注册广播
     */
    private void registeReceiver() {
        localReceiver = new CompleteReceiver();
        registerReceiver(localReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    public class CompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            Log.d(TAG, "completeId");
            // Log.d(TAG, Integer.toString(completeId));
            if(completeId == mDownLoadId) {
                Log.d(TAG, "File download ok!!!!!!!");
                Toast.makeText(getApplicationContext(), "下载成功",  Toast.LENGTH_LONG).show();
            }

        }
    }


    /**
     * 更新 bundle 文件
     */
    public void updateBundle() {
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(localReceiver);
    }

}
