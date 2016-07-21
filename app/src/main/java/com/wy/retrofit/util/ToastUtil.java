package com.wy.retrofit.util;

import android.widget.Toast;
import com.wy.retrofit.App;
import java.lang.ref.WeakReference;

/**
 * Created by wuyong on 16/7/13.
 */
public class ToastUtil {
  private static WeakReference<Toast> sToast;

  public static void show(CharSequence content) {
    if (sToast != null && sToast.get() != null) {
      sToast.get().show();
    } else {
      Toast toast = Toast.makeText(App.getApp(), content, Toast.LENGTH_SHORT);
      sToast = new WeakReference<>(toast);
      toast.show();
    }
  }

  public static void cancel() {
    if (sToast != null && sToast.get() != null) {
      sToast.get().cancel();
    }
  }
}
