package com.hotupdatetest;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.hotupdatetest.constants.FileConstants;
import com.hotupdatetest.MainApplication;
import com.hotupdatetest.hotupdate.FileUtils;
import com.hotupdatetest.hotupdate.HotUpdate;

import java.io.File;

public class MainActivity extends ReactActivity {
    private static final String TAG = "saul";

    private long mDownLoadId;
    private CompleteReceiver localReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        registeReceiver();
        /**
         * 从 RN 中发送消息过来通知原生app开始下载 bundle 文件更新
         * 由于 RN 是根据本地储存的版本号和 server 交互来确定是否需要更新的
         * 所以原生 app 中不需要进一步检查，直接干
         * RN 只需要发下载的 url
         */
        hotUpgrade();
    }

    private void newAc(){
        this.runOnUiThread(new Thread(){

        });
    }
    /**
     * 在首个活动中来执行新版本bundle文件下载任务，后期挪到原生模块暴露给 RN 的自行调用
     */
    public void hotUpgrade() {
        deleteLastFile();
        downloadBundle();

        // Log.d(TAG, "准备重新加载 bundle");
        updateBundle();
    }


    public void deleteLastFile() {
        Log.d(TAG, "准备尝试删除旧的包");
        // 1. 清空上次文件
        File file = new File(FileConstants.JS_PATCH_LOCAL_PATH);
        if(file != null && file.exists()) {
            Log.d(TAG, "存在旧的包文件,正在清空上次的下载包");
            boolean deleted = file.delete();
            if(deleted) Log.d(TAG, "已成功清除上次下载的包");
        } else {
            Log.d(TAG, "不存在旧的包");
        }
    }

    public void downloadBundle() {
        // 先进入另外一个 activity
        Intent intent = new Intent(MainActivity.this, MyActivity.class);
        startActivity(intent);

        // 休眠
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 2.下载
        Log.d(TAG, "开始下载新版本");
        // 默认有新版本
        Toast.makeText(this, "开始下载新版本",  Toast.LENGTH_LONG).show();


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
        // 注册，等另一个任务完成后执行
        registerReceiver(localReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    public class CompleteReceiver extends BroadcastReceiver {
        // 类似回调函数
        @Override
        public void onReceive(Context context, Intent intent) {
            long completeId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if(completeId == mDownLoadId) {
                Log.d(TAG, "File download ok!!!!!!!");
                Toast.makeText(MainActivity.this, "下载成功",  Toast.LENGTH_LONG).show();

                // showCompleteModal();
            }

        }
    }

    public void showCompleteModal() {
        /*
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.baidu.com"));
        startActivity(intent);
        */


        Intent intent = new Intent(MainActivity.this, MyActivity.class);
        startActivity(intent);

        /*
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        UpgradeProcessFragment fragment = new UpgradeProcessFragment();

        transaction.add(R.id.upgrade_process_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        */
    }

    public void updateBundle() {
        // 方案未定
        /* 先不考虑已存在的文件，先完成 RN 的加载
        if(file != null && file.exists()) {
            Log.i(TAG, "newest bundle exists!");
            return;
        }
        */

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
