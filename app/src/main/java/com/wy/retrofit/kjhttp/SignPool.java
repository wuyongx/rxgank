package com.wy.retrofit.kjhttp;

import android.text.TextUtils;
import com.wy.retrofit.util.Util;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuyong on 16/7/12.
 * md5 加密缓存池
 */
public class SignPool {
  private static volatile SignPool instance;
  private Map<String, String> signPool = new HashMap<>();

  private SignPool() {
  }

  public static SignPool getInstance() {
    if (instance == null) {
      synchronized (SignPool.class) {
        if (instance == null) {
          instance = new SignPool();
        }
      }
    }
    return instance;
  }

  /**
   * @param app 接口的 app string
   * @param clazz 接口的 class
   * @return 接口加密后的 md5值
   */
  public String sign(String app, String clazz) {
    String key = app + clazz;
    String result = signPool.get(key);
    if (TextUtils.isEmpty(result)) {
      result = Util.encryptMD5(app + clazz + API.MIYAO);
      signPool.put(key, result);
    }
    return result;
  }
}
