package com.wy.retrofit.kjhttp.rxjava;

/**
 * Created by wuyong on 16/4/18.
 * api 返回非期望数据
 */
public class DataException extends RuntimeException {
  public DataException(String errorMessage) {
    super(errorMessage);
  }
}
