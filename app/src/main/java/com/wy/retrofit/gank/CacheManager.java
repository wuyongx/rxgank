package com.wy.retrofit.gank;

import android.support.annotation.NonNull;
import com.wy.retrofit.App;
import com.wy.retrofit.util.NetworkUtil;

/**
 * Created by wuyong on 16/7/18.
 * gank 的 OkHttp 缓存管理
 */
public class CacheManager {
  private Spf_GankCache editor;

  private CacheManager() {
    editor = Spf_GankCache.create(App.getApp());
  }

  private static volatile CacheManager instance;

  public static CacheManager getInstance() {
    if (instance == null) {
      synchronized (CacheManager.class) {
        if (instance == null) {
          instance = new CacheManager();
        }
      }
    }
    return instance;
  }

  private void saveLastDate(String date) {
    editor.lastDate().put(date);
  }

  public String getCacheMaxAge(@NonNull String date, String cacheType) {
    String cacheMaxAge = "public, max-age=";
    int anHour = 60 * 60;
    if ("gank_history".equals(cacheType)) {
      if (NetworkUtil.isNetworkAvailable()) {
        cacheMaxAge += 0;
      } else {
        cacheMaxAge += anHour * 12;//12小时
      }
      return cacheMaxAge;
    }

    String cache = editor.lastDate().get("0");//取出保存的数据日期
    if (cache.compareTo(date) < 0) {//有新数据
      cacheMaxAge += 0;
      saveLastDate(date);//保存最新 数据日期
      return cacheMaxAge;
    }

    //已经是最新数据
    switch (cacheType) {
      case "daily_meizi":
      case "gank_list":
        cacheMaxAge += anHour * 12;//12小时
        break;
      case "gank_search":
        cacheMaxAge += anHour * 24 * 7;// 7天
        break;
    }
    return cacheMaxAge;
  }
}
