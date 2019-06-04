package com.hotupdatetest;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactInstanceManagerBuilder;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactRootView;

import com.facebook.react.bridge.JSBundleLoader;
import com.facebook.react.bridge.JSCJavaScriptExecutorFactory;
import com.facebook.react.bridge.JavaScriptExecutor;
import com.facebook.react.bridge.JavaScriptExecutorFactory;
import com.facebook.react.bridge.ProxyJavaScriptExecutor;
import com.hotupdatetest.constants.FileConstants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MyActivity extends FragmentActivity {

    private static final String TAG = "saul";

    private long mDownLoadId;
    private CompleteReceiver localReceiver;


    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "我是 myActivity");
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



    public void downloadBundle() {
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

                            TextView textView = findViewById(R.id.process_bar_percent_text);
                            textView.setText("已完成" + Integer.toString(dl_progress) + "%");

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
            if(completeId == mDownLoadId) {
                Log.d(TAG, "File download ok!!!!!!!");
                Toast.makeText(getApplicationContext(), "下载成功",  Toast.LENGTH_LONG).show();

                // jreloadReactApp();
            }

        }
    }


    /**
     * 重启 RN Context
     *
     */
    public void reloadReactApp() {

        // 报错： {com.hotupdatetest/com.facebook.react.ReactActivity}; have you declared this activity in your AndroidManifest.xml?
        // MainApplication.lanuchSelf();



        /*
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        */



        /*
        Activity currentActivity = ((MainApplication)getApplicationContext()).getCurrentActivity();
        currentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentActivity.recreate();
            }
        });
        */

        ReactNativeHost reactNativeHost = MainApplication.getInstance().getReactNativeHost();
        reactNativeHost.clear();    // 清除 Host!!!

        ReactInstanceManager mReactInstanceManager = reactNativeHost.getReactInstanceManager();
        mReactInstanceManager.createReactContextInBackground();  // 重新创建 Context
        mReactInstanceManager.recreateReactContextInBackground();


        Intent intent = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);



            /*
        try {
            Class<?> RIManagerClazz = mReactInstanceManager.getClass();
            Method method = RIManagerClazz.getDeclaredMethod("recreateReactContextInBackground",
                    JavaScriptExecutor.class, JSBundleLoader.class);
            method.setAccessible(true);
            method.invoke(mReactInstanceManager,
                    new JSCJavaScriptExecutor(),
                    JSBundleLoader.createFileLoader(FileConstants.JS_PATCH_LOCAL_PATH));


            Class<?> RIManagerClazz = mReactInstanceManager.getClass();
            Method method = RIManagerClazz.getDeclaredMethod("recreateReactContextInBackground",
                    JavaScriptExecutorFactory.class, JSBundleLoader.class);
            method.setAccessible(true);
            method.invoke(mReactInstanceManager,
                    JavaScriptExecutorFactory.class,
                    JSBundleLoader.createFileLoader(FileConstants.JS_PATCH_LOCAL_PATH));


        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
                    */
    }


    /**
     * 更新 bundle 文件
     */
    public void updateBundle() {
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(localReceiver);
    }

}
