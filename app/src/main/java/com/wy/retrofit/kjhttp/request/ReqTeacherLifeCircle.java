package com.wy.retrofit.kjhttp.request;

import com.wy.retrofit.di.scopes.ActivityScope;
import javax.inject.Inject;

/**
 * Created by wuyong on 16/7/12.
 */
@ActivityScope
public class ReqTeacherLifeCircle extends BaseReq {
  public long teacher_id = 1;
  public int psize = 10;
  public int p = 1;

  @Inject public ReqTeacherLifeCircle() {
  }
}
