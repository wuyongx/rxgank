package com.wy.retrofit.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class MyUtils {

  public static String getProcessName(Context cxt, int pid) {
    ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
    List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
    if (runningApps == null) {
      return null;
    }
    for (RunningAppProcessInfo procInfo : runningApps) {
      if (procInfo.pid == pid) {
        return procInfo.processName;
      }
    }
    return null;
  }

  public static void close(Closeable closeable) {
    try {
      if (closeable != null) {
        closeable.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static DisplayMetrics getScreenMetrics() {
    return  Resources.getSystem().getDisplayMetrics();
  }


  public static boolean isWifi(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

    return activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
  }

  public static void executeInThread(Runnable runnable) {
    new Thread(runnable).start();
  }
}
