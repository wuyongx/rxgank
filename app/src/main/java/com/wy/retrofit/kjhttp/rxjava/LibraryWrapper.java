package com.wy.retrofit.kjhttp.rxjava;

import android.app.ActivityManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;
import com.wy.retrofit.BuildConfig;

/**
 * 初始化外部依赖库,可能有耗时操作
 */
public class LibraryWrapper {
  private static final String TAG = LibraryWrapper.class.getName();

  public static void init(Context context, @NonNull String className) {
    switch (className) {//根据 string 初始化 不同的第三方库
      case "Logger"://初始化 logger
        RxUtil.executeObservable(() -> Logger.initialize(new Settings().isShowMethodLink(true)
            .isShowThreadInfo(false)
            .setMethodOffset(0)
            .setLogPriority(BuildConfig.DEBUG ? Log.ERROR : Log.ASSERT)));
        break;
    }
  }

  /*判断进程是否是当前 app*/
  public static boolean isCurrentAppProcess(Context context) {
    int pid = android.os.Process.myPid();
    ActivityManager mActivityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
      if (appProcess.pid == pid && context.getPackageName().equals(appProcess.processName)) {
        return true;
      }
    }
    return false;
  }
}
