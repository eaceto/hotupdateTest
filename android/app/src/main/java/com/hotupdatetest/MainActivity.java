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
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import com.facebook.react.ReactActivity;
import com.hotupdatetest.constants.FileConstants;
import com.hotupdatetest.MainApplication;
import com.hotupdatetest.hotupdate.FileUtils;
import com.hotupdatetest.hotupdate.HotUpdate;

public class MainActivity extends ReactActivity {

    private long mDownLoadId;
    private CompleteReceiver localReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registeReceiver();
        load();
    }


    /**
     * 在首个活动中来执行新版本bundle文件下载任务，后期挪到原生模块暴露给 RN 的行调用
     */
    public void load() {
        checkVersion();
    }

    public void checkVersion() {
        // 默认有新版本
        Toast.makeText(this, "开始下载",  Toast.LENGTH_LONG).show();
        downloadBundle();
    }

    public void downloadBundle() {
        // 1.下载钱检车 SD 卡是否存在更新文件夹
        // 2.下载
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri myDownloads = Uri.parse(FileConstants.JS_BUNDLE_REMOTE_URL);
        DownloadManager.Request request = new DownloadManager.Request(myDownloads); // 远程 zip 文件

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE| DownloadManager.Request.NETWORK_WIFI);

        request.setDestinationUri(Uri.parse("file://" + FileConstants.JS_PATCH_LOCAL_PATH));

        Log.d("js_patch_local_path", FileConstants.JS_PATCH_LOCAL_PATH);

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
                Log.d("saul", "File download ok!!!!!!!");

                /**
                 * 先不优化
                 */
                // HotUpdate.handleZIP(getApplicationContext());

                // 1.解压
                FileUtils.decompression();
                // zipfile.delete();
            }

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
