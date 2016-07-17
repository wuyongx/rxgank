package com.wy.retrofit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import butterknife.Unbinder;
import com.orhanobut.logger.Logger;
import com.wy.retrofit.App;
import com.wy.retrofit.di.components.AppComponent;
import com.wy.retrofit.di.modules.ActivityModule;
import me.yokeyword.fragmentation.SupportActivity;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BaseActivity extends SupportActivity {
  private ActivityModule mActivityModule;
  private CompositeSubscription mCompositeSubscription;
  protected static final String ARG_PARAM1 = "param1";
  protected static final String ARG_PARAM2 = "param2";
  protected String TAG;
  protected Unbinder unbinder;
  protected Context mContext;

  @Override protected void onCreate(Bundle savedInstanceState) {
    mContext = this;
    TAG = this.getClass().getCanonicalName();
    super.onCreate(savedInstanceState);
    initView(savedInstanceState);
    setListener();
    processLogic(savedInstanceState);
  }

  @Override protected void onDestroy() {
    unSubscribe();
    if (unbinder != null) {
      unbinder.unbind();
    }
    super.onDestroy();
  }

  protected ActivityModule getActivityModule() {
    if (mActivityModule == null) {
      mActivityModule = new ActivityModule(this);
    }
    return mActivityModule;
  }

  protected AppComponent getAppComponent() {
    return ((App) getApplication()).component();
  }

  protected void addSubscription(@NonNull Subscription s) {
    if (mCompositeSubscription == null) {
      mCompositeSubscription = new CompositeSubscription();
    }
    mCompositeSubscription.add(s);
  }

  /**
   * 查找View
   *
   * @param id 控件的id
   * @param <VT> View类型
   * @return View
   */
  @SuppressWarnings("unchecked") protected <VT extends View> VT getViewById(@IdRes int id) {
    return (VT) findViewById(id);
  }

  @SuppressWarnings("unchecked")
  protected <VT extends View> VT getViewById(View view, @IdRes int id) {
    return (VT) view.findViewById(id);
  }

  /**
   * 初始化布局以及View控件
   */
  protected abstract void initView(Bundle savedInstanceState);

  /**
   * 给View控件添加事件监听器
   */
  protected abstract void setListener();

  /**
   * 处理业务逻辑，状态恢复等操作
   */
  protected abstract void processLogic(Bundle savedInstanceState);

  /**
   * 取消订阅
   */
  protected void unSubscribe() {
    if (mCompositeSubscription != null) {
      if (!mCompositeSubscription.isUnsubscribed()) {
        Logger.e(TAG, "取消订阅");
        mCompositeSubscription.unsubscribe();
      }
    }
  }
}
