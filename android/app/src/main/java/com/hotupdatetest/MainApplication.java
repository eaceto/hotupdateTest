package com.hotupdatetest;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactApplication;
import com.avishayil.rnrestart.ReactNativeRestartPackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.hotupdatetest.customModule.UpgradePackage;
import com.hotupdatetest.constants.FileConstants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainApplication extends Application implements ReactApplication {
  private static final String TAG = "saul";

  private static Context mContext;

  private static MainApplication instance;
  public String mBundleFilePath = "";

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

    /**
     * 控制 RN 加载 Bundle 文件
     * 默认返回 null 加载 assets 下的 bundle 文件，可以根据不同的条件加载不同目录下的 bundle 文件
     */
    @Nullable
    @Override
    protected String getJSBundleFile() {
      mBundleFilePath = super.getJSBundleFile();

      /*
      if(true) {
        return mBundleFilePath;
      }
      */

      File file = new File(FileConstants.JS_PATCH_LOCAL_PATH);
      if(file != null && file.exists()) {
        Log.d(TAG, "存在资源包，正在加载资源包启动");
        Log.d(TAG, "正在 MainApplication 加载新的资源");
        return FileConstants.JS_PATCH_LOCAL_PATH;
      } else {
        Log.d(TAG, "正在以默认的方式加载包");
        return super.getJSBundleFile();
      }
    }



    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
            new ReactNativeRestartPackage(),
              new UpgradePackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };


  /**
   * 重启app
   * @return
   */
  public static void lanuchSelf() {
    MainApplication.getInstance().mBundleFilePath = FileConstants.JS_PATCH_LOCAL_PATH;
    Intent intent = new Intent(mContext, ReactActivity.class);
    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
    mContext.startActivity(intent);
  }


  @Override
  public ReactNativeHost getReactNativeHost() {
      return mReactNativeHost;
  }


  @Override
  public void onCreate() {
    Log.d(TAG, "我是 MainApplication");
    super.onCreate();
    mContext = this.getApplicationContext();
    instance = this;
    SoLoader.init(this, /* native exopackage */ false);
  }


  /**
   *包名
   */
  public String getAppPackageName() {
    return this.getPackageName();
  }


  /**
   * 获取Application实例
   */
  public static MainApplication getInstance() {
    return instance;
  }
}
