package com.wy.retrofit.kjhttp;

import com.wy.retrofit.gank.GankInfo;
import com.wy.retrofit.gank.GankResponse;
import com.wy.retrofit.kjhttp.response.RespArrayWrapper;
import com.wy.retrofit.kjhttp.response.TeacherLifeCircleInfo;
import com.wy.retrofit.kjhttp.response.Wrapper;
import java.util.Map;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ApiService {

  @POST("api") @FormUrlEncoded
  Observable<Wrapper<RespArrayWrapper<TeacherLifeCircleInfo>>> getTeacherLifeCircle(
      @FieldMap Map<String, String> param);

  @GET("api") Observable<Wrapper<RespArrayWrapper<TeacherLifeCircleInfo>>> getTeacherLifeCircle2(
      @QueryMap Map<String, String> param);


  @GET("http://gank.io/api/data/{all}/20/{page}") Observable<GankResponse<GankInfo>> getGankList(
      @Path("all") String type, @Path("page") int page,@Header("Cache-Control") String cacheControl);

  // 搜索流量大概2M
  @GET("http://gank.io/api/data/{type}/1000000000/1") Observable<GankResponse<GankInfo>> searchGank(
      @Path("type") String type,@Header("Cache-Control") String cacheControl);

  @Headers("Cache-Control: public, max-age=0") // 不缓存,app 每次 启动获取最新数据
  @GET("http://gank.io/api/day/history") Observable<GankResponse<String>> getGankDate();

  @GET("http://gank.io/api/history/content/day/{date}")//代码控制 缓存策略
  Observable<GankResponse<GankInfo>> getGankMeizi(@Path("date") String date,
      @Header("Cache-Control") String cacheControl);
}
