package com.wy.retrofit.kjhttp.response;

import java.util.List;

/**
 * Created by wuyong on 16/7/12.
 */
public class RespArrayWrapper<T> {

  public int currentpage;
  public int totalnum;
  public int totalpage;
  public int yearchange;

  public List<T> info;

  public boolean isEmpty() {
    return info == null || info.isEmpty();
  }
}
