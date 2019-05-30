package com.hotupdatetest;

import android.app.Application;
import android.util.Log;

import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;
import com.hotupdatetest.constants.FileConstants;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

    /**
     * 控制 RN 加载 Bundle 文件
     * 默认返回 null 加载 assets 下的 bundle 文件，可以根据不同的条件加载不同目录下的 bundle 文件
     */
    @Nullable
    @Override
    protected String getJSBundleFile() {
      Log.d("getJSBundleFile", "getJSBundleFile");

      File file = new File(FileConstants.JS_BUNDLE_LOCAL_FILE);
      if(file != null && file.exists()) {
        return FileConstants.JS_BUNDLE_LOCAL_FILE;   // 加载我们远程下载的 bundle 文件
      } else {
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
          new MainReactPackage()
      );
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };



  @Override
  public ReactNativeHost getReactNativeHost() {
      return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
  }
}
