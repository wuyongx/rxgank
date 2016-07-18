package com.wy.retrofit.kjhttp.rxjava;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuyong on 16/7/18.
 * RxUtil 生成 Observable 工具类
 */
public class RxUtil {
  private static final String TAG = "RxUtil";
  /**
   * @param target runnable
   * @return Observable
   */
  public static Observable<Boolean> newObservable(@NonNull Runnable target) {
    return Observable.create(new Observable.OnSubscribe<Boolean>() {
      @Override public void call(Subscriber<? super Boolean> subscriber) {
        if (!subscriber.isUnsubscribed()) {
          try {
            target.run();
            subscriber.onNext(true);
            subscriber.onCompleted();
          } catch (Exception e) {
            subscriber.onError(e);
          }
        }
      }
    });
  }

  /**
   * @param target runnable
   * @param scheduler runnable 执行的Scheduler,为空 默认 Schedulers.io().指定一个即可
   */
  public static void executeObservable(@NonNull Runnable target, @Nullable Scheduler... scheduler) {
    Observable.create(new Observable.OnSubscribe<Boolean>() {
      @Override public void call(Subscriber<? super Boolean> subscriber) {
        if (!subscriber.isUnsubscribed()) {
          try {
            target.run();
            subscriber.onNext(true);
            subscriber.onCompleted();
          } catch (Exception e) {
            subscriber.onError(e);
          }
        }
      }
    }).subscribeOn(
            scheduler == null || scheduler.length == 0 || scheduler[0] == null ? Schedulers.io()
                : AndroidSchedulers.mainThread())
        .subscribe(result -> {
          Log.e(TAG, Thread.currentThread().getName());
          Log.e(TAG, String.valueOf(result));
        }, e -> Log.e(TAG, e.getMessage()));
  }
}
