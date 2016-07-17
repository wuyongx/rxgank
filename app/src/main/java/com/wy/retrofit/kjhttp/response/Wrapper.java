package com.wy.retrofit.kjhttp.response;

/**
 * Created by wuyong on 16/4/21.
 */
public class Wrapper<T>{
  public int status;
  public String msg;

  public T data;

  public boolean success() {
    return status == 0;
  }

}
