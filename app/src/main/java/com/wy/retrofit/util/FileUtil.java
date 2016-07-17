package com.wy.retrofit.util;

import android.content.Context;
import android.os.Environment;
import java.io.File;

/**
 * Created by wuyong on 16/7/13.
 */
public class FileUtil {

  public static File getDiskCacheDir(Context context, String fileName) {
    return new File(getCacheDir(context) + File.separator + fileName);
  }

  public static boolean hasExternalStorage() {
    return !Environment.isExternalStorageRemovable() && Environment.getExternalStorageState()
        .equals(Environment.MEDIA_MOUNTED);
  }

  /**
   * get cache dir External prefer, if not intern
   */
  private static File getCacheDir(Context context) {
    File cacheFolder;
    if (hasExternalStorage()) {
      cacheFolder = context.getExternalCacheDir();
    } else {
      cacheFolder = context.getCacheDir();
    }
    if (cacheFolder != null && !cacheFolder.exists()) {
      //noinspection ResultOfMethodCallIgnored
      cacheFolder.mkdirs();
    }
    return cacheFolder;
  }
}
