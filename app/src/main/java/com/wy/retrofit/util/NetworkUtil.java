package com.wy.retrofit.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.wy.retrofit.App;

/**
 * Created by wuyong on 16/4/14.
 */
public class NetworkUtil {

  // 判断是否有可用的网络连接
  public static boolean isNetworkAvailable() {
    App application = App.getApp();
    ConnectivityManager connectivityManager =
        (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnected();
  }
}