package com.wy.retrofit.kjhttp;

import android.support.annotation.NonNull;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

  private RetrofitFactory() {
  }

  /**
   * @param clazz 目标接口 class
   * @param baseUrl BaseUrl  @return 返回目标接口的具体实例
   * @param client OKHttp client
   * @param gson Gson
   * @return RetrofitService
   */
  @SuppressWarnings("unchecked") public static <T> T createService(@NonNull Class<?> clazz,
      @NonNull String baseUrl, @NonNull OkHttpClient client, @NonNull Gson gson) {
    Retrofit retrofit = new Retrofit.Builder().client(client)
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    return (T) retrofit.create(clazz);
  }
}
