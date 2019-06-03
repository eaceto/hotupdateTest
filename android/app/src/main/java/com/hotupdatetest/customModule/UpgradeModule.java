package com.hotupdatetest.customModule;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.hotupdatetest.MyActivity;
import com.hotupdatetest.constants.FileConstants;

import java.io.File;


public class UpgradeModule extends ReactContextBaseJavaModule {
    private static final String TAG = "saul";


    ReactApplicationContext aContext;
    public UpgradeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        aContext = reactContext;       // 保存 MainActivity 传来的上下文实例
    }


    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    @ReactMethod
    public void startHotUpgrade(String downloadUrl) {
        Log.d(TAG, downloadUrl);

        lanuchProcesDialog();

        // deleteLastFile();

        // updateBundle();

    }

    @ReactMethod
    public void deleteLastUpdate() {
        deleteLastFile();
    }


    /**
     * 清空旧的文件夹
     */
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


    /**
     * 启动进度弹窗
     */
    public void lanuchProcesDialog() {
        Log.d(TAG, "启动 processdialog");

        ReactApplicationContext context = getReactApplicationContext();

        Intent intent = new Intent(context, MyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        aContext.startActivity(intent);


        /*
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        UpgradeProcessFragment fragment = new UpgradeProcessFragment();

        transaction.replace(R.id.upgrade_process_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        */
    }




    @Override
    public String getName() {
        return "Upgrade";
    }
}

