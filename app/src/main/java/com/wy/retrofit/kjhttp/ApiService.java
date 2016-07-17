package com.wy.retrofit.kjhttp;

import com.wy.retrofit.gank.Beatuty;
import com.wy.retrofit.gank.GankInfo;
import com.wy.retrofit.gank.GankResponse;
import com.wy.retrofit.kjhttp.response.RespArrayWrapper;
import com.wy.retrofit.kjhttp.response.TeacherLifeCircleInfo;
import com.wy.retrofit.kjhttp.response.Wrapper;
import java.util.Map;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

  @Headers("Cache-Control: public, max-age=3600") //max-age单位秒有些接口返回的内容更新频次不是很频繁可以缓存
  @GET("http://gank.io/api/data/福利/{number}/{page}")
  Observable<GankResponse<Beatuty>> getBeauties(
      @Path("number") int number, @Path("page") int page);

  @Headers("Cache-Control: public, max-age=3600") // 正常列表
  @GET("http://gank.io/api/data/{all}/20/{page}")
  Observable<GankResponse<GankInfo>> getGankList(
      @Path("all") String type, @Path("page") int page);

  @Headers("Cache-Control: public, max-age=604800") // 搜索 本地保存7天
  @GET("http://gank.io/api/data/{type}/1000000000/1")
  Observable<GankResponse<GankInfo>> searchGank(@Path("type") String type);

  @Headers("Cache-Control: public, max-age=86400") // 保存1天
  @GET("http://gank.io/api/day/history")
  Observable<GankResponse<String>> getGankDate();

  @Headers("Cache-Control: public, max-age=86400") // 保存1天
  @GET("http://gank.io/api/history/content/day/{year}/{month}/{day}")
  Observable<GankResponse<GankInfo>> getGankMeizi(@Path("year") String year,@Path("month") String month,@Path("day") String day);
}
