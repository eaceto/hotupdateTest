package com.hotupdatetest.hotupdate;

import android.content.Context;
import android.util.Log;

import com.hotupdatetest.constants.AppConstants;
import com.hotupdatetest.constants.FileConstants;

import java.io.File;

public class HotUpdate {
    public static void checkPackage(Context context, String filePath) {
        // 1.下载前检查SD卡是否存在更新包文件夹,FIRST_UPDATE来标识是否为第一次下发更新包
        File bundleFile = new File(filePath);
        if(bundleFile != null && bundleFile.exists()) {
            ACache.get(context).put(AppConstants.FIRST_UPDATE,false);
        } else {
            ACache.get(context).put(AppConstants.FIRST_UPDATE,true);
        }
    }

    public static void handleZIP(final Context context) {
        // 开启单独线程，解压，合并。
        new Thread(new Runnable() {
            @Override
            public void run() {

                // boolean result = (Boolean) ACache.get(context).getAsObject(AppConstants.FIRST_UPDATE);

                /*
                if (result) {
                    // 解压到根目录
                    FileUtils.decompression(FileConstants.JS_PATCH_LOCAL_FOLDER);
                    // 合并
                    Log.d("mergePat", "xxxxxxxxxxxxxxxxx");
                    // mergePatAndAsset(context);
                } else {
                    // 解压到future目录
                    FileUtils.decompression(FileConstants.FUTURE_JS_PATCH_LOCAL_FOLDER);
                    // 合并
                    Log.d("mergePat", "jjjjjjjjjjjjjjjjjjjjjjjj");
                    // mergePatAndBundle();
                }
                // 删除ZIP压缩包
                FileUtils.deleteFile(FileConstants.JS_PATCH_LOCAL_PATH);
                */
            }
        }).start();

    }

}
