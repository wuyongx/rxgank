package com.wy.retrofit.util;

import android.app.Activity;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.jude.swipbackhelper.SwipeBackPage;
import java.lang.ref.WeakReference;

/**
 * Created by wuyong on 16/5/27.
 * 滑动返回帮助类 封装
 */
public class SwipeBackWrapper {
  private static WeakReference<Activity> sWeakRef;
  private static WeakReference<Boolean> sWeakRefBoolean;

  /**
   * 当前 Activity 是否启用 滑动返回
   *
   * @param activity 目标 Activity
   * @param enableSwipeBack 是否启用
   */
  public static void init(Activity activity, boolean... enableSwipeBack) {
    sWeakRef = new WeakReference<Activity>(activity);
    if (enableSwipeBack != null && enableSwipeBack.length > 0) {
      sWeakRefBoolean = new WeakReference<Boolean>(enableSwipeBack[0]);
    } else {
      //默认 true
      sWeakRefBoolean = new WeakReference<Boolean>(true);
    }

    onCreate();
  }

  private static void onCreate() {
    Activity activity = sWeakRef.get();
    boolean enable = sWeakRefBoolean.get();
    if (activity != null) {
      if (enable) {
        SwipeBackHelper.onCreate(activity);
        SwipeBackPage currentPage = SwipeBackHelper.getCurrentPage(activity);
        L.e(activity.getClass().getName(), "SwipeBackHelper");
        currentPage.setSwipeBackEnable(true);
        currentPage.setScrimColor(ResourceUtil.getColor(android.R.color.transparent))//底层阴影颜色
            .setSwipeRelateEnable(true);//是否与下一级activity联动(微信效果)。默认关
        //底层阴影颜色
      }
    }
  }

  public static void onPostCreate() {
    Activity activity = sWeakRef.get();
    boolean enable = sWeakRefBoolean.get();
    if (activity != null) {
      if (enable) {
        SwipeBackHelper.onPostCreate(activity);
      }
    }
  }

  public static void onDestroy() {
    Activity activity = sWeakRef.get();
    boolean enable = sWeakRefBoolean.get();
    if (activity != null) {
      if (enable) {
        SwipeBackHelper.onDestroy(activity);
      }
    }
  }
}
