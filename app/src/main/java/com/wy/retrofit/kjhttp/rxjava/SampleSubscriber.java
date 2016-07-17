package com.wy.retrofit.kjhttp.rxjava;

/**
 * Created by wuyong on 16/4/19.
 * SampleSubscriber
 */
public abstract class SampleSubscriber<T> {
  public SampleSubscriber() {
  }
  public abstract void onNext(T t);

  public void onCompleted() {
  }

  public void onError(Throwable e) {
  }
}
