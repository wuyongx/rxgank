package com.wy.retrofit.kjhttp.rxjava;

/**
 * Created by wuyong on 16/4/18.
 * api 返回非0
 */
public class NetWorkException extends RuntimeException {
  public NetWorkException(String errorMessage) {
    super(errorMessage);
  }
}
