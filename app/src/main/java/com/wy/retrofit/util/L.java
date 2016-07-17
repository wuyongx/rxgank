package com.wy.retrofit.util;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*简单的日志类*/
public class L {
  public static void e(String msg) {
    if (msg.contains("-")) {
      String[] strings = msg.split("-");
      Log.e(strings[0], strings[1]);
    } else {
      Log.e("okHttp", msg);
    }
  }

  public static void e(String key, Object obj) {
    Log.e(key, new MyGson().gson.toJson(obj));
  }

  public static void e(String key, String msg) {
    Log.e(key, msg);
  }

  private static class MyGson {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();//r统一日期请求格式
  }
}

