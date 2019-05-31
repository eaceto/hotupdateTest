package com.hotupdatetest.constants;

import android.os.Environment;

import com.hotupdatetest.MainApplication;

import java.io.File;

public class FileConstants {
    /**
     * bundle 文件名
     */
    public static final String JS_BUNDLE_LOCAL_FILE = "index.android.bundle";

    /**
     * 远程文件下载地址
     */
    public static final String JS_BUNDLE_REMOTE_URL = "http://106.75.233.162/index.android.bundle";

    /**
     * 本地储存的文件夹
     */
    public static final String JS_PATCH_LOCAL_FOLDER = Environment.getExternalStorageDirectory().toString()
            + File.separator + MainApplication.getInstance().getAppPackageName();
    /**
     * 本地 bundle 储存路径
     */
    public static final String JS_PATCH_LOCAL_PATH = JS_PATCH_LOCAL_FOLDER + File.separator + JS_BUNDLE_LOCAL_FILE;
}
