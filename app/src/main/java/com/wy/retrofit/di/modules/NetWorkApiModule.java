package com.wy.retrofit.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wy.retrofit.App;
import com.wy.retrofit.kjhttp.API;
import com.wy.retrofit.kjhttp.ApiService;
import com.wy.retrofit.kjhttp.CacheInterceptor;
import com.wy.retrofit.kjhttp.RetrofitFactory;
import com.wy.retrofit.util.FileUtil;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by wuyong on 16/4/3.
 * 网络请求参数 之 模块
 */
@Module public class NetWorkApiModule {
  @Singleton @Provides public static Gson provideGson() {
    //return new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
    return new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm").create();
  }

  @Singleton @Provides public static OkHttpClient provideOkHttpClient() {

    OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true);
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      File cacheFile = FileUtil.getDiskCacheDir(App.getApp(), "OkCache");
      Cache cache = new Cache(cacheFile, 1024 * 1024 * 20);
      CacheInterceptor cacheInterceptor = new CacheInterceptor();
      builder.addInterceptor(logging)
          .cache(cache)
          .addInterceptor(cacheInterceptor)
          .addNetworkInterceptor(cacheInterceptor)
          .build();
    return builder.build();
  }

  @Singleton @Provides public static ApiService provideAPIService(OkHttpClient client,Gson  gson) {
    return RetrofitFactory.createService(ApiService.class, API.BASE_URL, client,gson);
  }
}
