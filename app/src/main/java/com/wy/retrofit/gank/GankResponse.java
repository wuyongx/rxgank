package com.wy.retrofit.gank;

import java.util.List;

/**
 * Created by wuyong on 16/7/12.
 */
public class GankResponse<T> {
  public boolean error;
  public List<T> results;

  public boolean success() {
    return !(error || results == null || results.isEmpty());
  }
}
