package com.wy.retrofit.gank;

import com.wy.retrofit.App;

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

  public void saveLastDate(String date) {
    editor.lastDate().put(date);
  }

  public String getCacheMaxAge(String date, String cacheType) {
    String cacheMaxAge = "public, max-age=";
    switch (cacheType) {
      case "daily_meizi":
        String cache = editor.lastDate().get("0");//取出保存的数据日期

        if (cache.compareTo(date) < 0) {//有新数据
          cacheMaxAge += "0";
          CacheManager.getInstance().saveLastDate(date);//保存最新 数据日期
        } else {//已经是最新数据
          cacheMaxAge += 60 * 60 * 60 * 6;//6小时
        }
        break;
    }
    return cacheMaxAge;
  }
}
