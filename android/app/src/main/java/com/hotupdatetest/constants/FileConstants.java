package com.hotupdatetest.constants;

import android.os.Environment;

import com.hotupdatetest.MainApplication;

import java.io.File;

public class FileConstants {
    /**
     * zip的文件名
     */
    public static final String ZIP_NAME = "saul";

    /**
     * 第一次解压zip后的文件目录
     */
    public static final String JS_PATCH_LOCAL_FOLDER = Environment.getExternalStorageDirectory().toString()
            + File.separator + MainApplication.getInstance().getAppPackageName();

    /**
     * 除第一次外，未来解压zip后的文件目录
     */
    public static final String FUTURE_JS_PATCH_LOCAL_FOLDER = JS_PATCH_LOCAL_FOLDER+"/future";

    public static final String FUTURE_DRAWABLE_PATH = FUTURE_JS_PATCH_LOCAL_FOLDER + "/"+ ZIP_NAME + "/drawable-mdpi/";
    public static final String FUTURE_PAT_PATH = FUTURE_JS_PATCH_LOCAL_FOLDER+"/saul/"+"bundle.pat";

    /**
     * bundle 文件名
     */
    public static final String JS_BUNDLE_LOCAL_FILE = "index.android.bundle";

    /**
     * 下载 URL
     */
    public static final String JS_BUNDLE_REMOTE_URL = "https://file-examples.com/wp-content/uploads/2017/02/zip_2MB.zip";
    // public static final String JS_BUNDLE_REMOTE_URL = "https://file-examples.com/wp-content/uploads/2017/02/zip_10MB.zip";

    /**
     * zip文件
     */
    public static final String JS_PATCH_LOCAL_PATH = JS_PATCH_LOCAL_FOLDER + File.separator + ZIP_NAME + ".zip";
}
