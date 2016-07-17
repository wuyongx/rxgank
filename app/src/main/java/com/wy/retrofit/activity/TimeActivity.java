package com.wy.retrofit.activity;

import android.os.Bundle;
import android.view.View;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.wy.retrofit.gank.Beatuty;
import com.wy.retrofit.kjhttp.request.ReqTeacherLifeCircle;
import com.wy.retrofit.kjhttp.response.RespArrayWrapper;
import com.wy.retrofit.kjhttp.response.TeacherLifeCircleInfo;
import com.wy.retrofit.kjhttp.rxjava.ProgressSubscriber;
import com.wy.retrofit.kjhttp.rxjava.RetrofitClient;
import com.wy.retrofit.kjhttp.rxjava.SampleSubscriber;
import java.util.List;
import javax.inject.Inject;

public class TimeActivity extends BaseActivity {
  @Inject RetrofitClient client;
  @Inject ReqTeacherLifeCircle req;
  @Inject Gson gson;
  private int p = 1;
  //private ActivityTimeBinding binding;

  /**
   * RxJava  Observer 接口
   */

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override protected void initView(Bundle savedInstanceState) {

  }

  @Override protected void setListener() {

  }

  @Override protected void processLogic(Bundle savedInstanceState) {
    //binding = DataBindingUtil.setContentView(this, R.layout.activity_time);
    getAppComponent().newActivitySubComponent(getActivityModule()).inject(this);
    req.app = "Cas";
    req.clazz = "GetTeacherLifeCircle";
  }

  public void load(View view) {
    req.p = 1;
    getTeacherCirce();
  }

  public void add(View view) {
    req.p++;
    getTeacherCirce();
  }

  public void unsubscribe(View view) {
    unSubscribe();
  }

  public void load2(View view) {
    p = 1;
    getBeatuty();
  }

  public void add2(View view) {
    p++;
    getBeatuty();
  }

  private void getBeatuty() {
    client.getBeauties(p, new ProgressSubscriber<>(this, new SampleSubscriber<List<Beatuty>>() {
      @Override public void onError(Throwable e) {
      }

      @Override public void onNext(List<Beatuty> beatuties) {
        //binding.tvContent.setText(gson.toJson(beatuties));
        Logger.object(beatuties);
      }
    }));
  }

  private void getTeacherCirce() {
    client.getTeacherLifeCircle(req, new ProgressSubscriber<>(this,
        new SampleSubscriber<RespArrayWrapper<TeacherLifeCircleInfo>>() {
          @Override public void onNext(RespArrayWrapper<TeacherLifeCircleInfo> response) {
            List<TeacherLifeCircleInfo> infoList = response.info;
            //binding.tvContent.setText(gson.toJson(response));
          }
        }));
  }
}
