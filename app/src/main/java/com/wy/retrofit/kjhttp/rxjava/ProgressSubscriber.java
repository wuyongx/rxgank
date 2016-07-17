package com.wy.retrofit.kjhttp.rxjava;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import com.wy.retrofit.util.ToastUtil;
import com.wy.retrofit.view.CustomProgressDialog;
import com.wy.retrofit.view.DialogHelper;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 封装带进度条的Subscriber,进度条结束可以自动取消订阅
 */
public class ProgressSubscriber<T> extends Subscriber<T>
    implements DialogInterface.OnCancelListener {
  private final String TAG = this.getClass().getCanonicalName();
  /*入参,实际的Subscriber*/
  private SampleSubscriber<T> sampleSubscriber;
  private DialogHelper mHelper;
  /*显示进度条标志  默认显示*/
  private boolean showDialog = true;
  private String dialogMsg = CustomProgressDialog.MSG;

  /**
   * 请求Subscriber 回调
   *
   * @param context 当前 context
   * @param sampleSubscriber 内层 Subscriber 回调
   * @param loadingMsg 加载对话框消息内容
   */
  public ProgressSubscriber(Context context, @NonNull SampleSubscriber<T> sampleSubscriber,
      String... loadingMsg) {
    this.sampleSubscriber = sampleSubscriber;
    mHelper = new DialogHelper(context);
    if (loadingMsg != null && loadingMsg.length > 0 && !TextUtils.isEmpty(loadingMsg[0])) {
      dialogMsg = loadingMsg[0];
    }
  }

  public ProgressSubscriber(Context context, @NonNull Action1<T> onNext,@Nullable Action1<Throwable> onError,
      String... loadingMsg) {
    this.sampleSubscriber = new SampleSubscriber<T>() {
      @Override public void onNext(T t) {
        onNext.call(t);
      }

      @Override public void onError(Throwable e) {
        if (onError != null) {
          onError.call(e);
        }
      }
    };
    mHelper = new DialogHelper(context);
    if (loadingMsg != null && loadingMsg.length > 0 && !TextUtils.isEmpty(loadingMsg[0])) {
      dialogMsg = loadingMsg[0];
    }
  }

  /**
   * 请求开始是否显示进度条,默认开启
   *
   * @param showDialog 是否显示
   */
  public void showDialog(boolean showDialog) {
    this.showDialog = showDialog;
  }

  @Override public void onStart() {
    if (showDialog) {
      mHelper.startProgressDialog(dialogMsg);
      //注册 Dialog onDismiss 监听器
      mHelper.getProgressDialog().setOnCancelListener(this);
      Log.e(TAG, "onStart()");
    }
  }

  @Override public void onCompleted() {
    sampleSubscriber.onCompleted();
    Log.e(TAG, "onCompleted()");
    mHelper.stopProgressDialog();
    unsubscribeSelf();
  }

  @Override public void onError(Throwable e) {
    Log.e(TAG, "onError() " + e.getClass().getName() + " : " + e.getMessage());
    String toastMsg = TextUtils.isEmpty(e.getMessage()) ? "查询失败" : e.getMessage();
    ToastUtil.show(toastMsg);
    sampleSubscriber.onError(e);
    mHelper.stopProgressDialog();
    unsubscribeSelf();
  }

  @Override public void onNext(T t) {
    sampleSubscriber.onNext(t);
  }

  /**
   * 取消订阅
   */
  private void unsubscribeSelf() {
    if (!isUnsubscribed()) {
      Log.e(TAG, "取消订阅");
      unsubscribe();
    }
  }

  /**
   * Dialog onCancel 会自动取消订阅
   */
  @Override public void onCancel(DialogInterface dialog) {
    Log.e(TAG, "onCancel");
    unsubscribeSelf();
  }
}