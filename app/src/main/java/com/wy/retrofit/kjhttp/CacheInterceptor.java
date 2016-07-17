package com.wy.retrofit.kjhttp;

import com.wy.retrofit.util.NetworkUtil;
import java.io.IOException;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wuyong on 16/4/21.
 * okhttp3拦截器
 */
public class CacheInterceptor implements Interceptor {

  @Override public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    if (!NetworkUtil.isNetworkAvailable()) {
      request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
    }

    Response response = chain.proceed(request);

    if (NetworkUtil.isNetworkAvailable()) {
      //有网的时候读取 请求@Headers里的配置
      String cacheControl = request.cacheControl().toString();
      return response.newBuilder()
          .removeHeader("Expires")
          .removeHeader("Cache-Control")
          .removeHeader("Pragma")
          .header("Cache-Control", cacheControl)
          .build();
    } else {
      int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
      return response.newBuilder()
          .removeHeader("Expires")
          .removeHeader("Cache-Control")
          .removeHeader("Pragma")
          .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
          .build();
    }
  }
}
